import dagger
from dagger import dag, function, object_type
import random

@object_type
class SpringPetclinic:
    MAVEN_IMAGE = "maven:3.9.9-eclipse-temurin-21"

    @function
    def build_env(self) -> dagger.Container:
        project = (
            dag
            .git("https://github.com/spring-projects/spring-petclinic")
            .commit("332abbcb8a37bf5430c0927b2109a04cc8ac0e93")
            .tree()
        )
        m2_cache = dag.cache_volume("spring-petclinic-m2")
        return (
            dag.container()
            .from_(SpringPetclinic.MAVEN_IMAGE)
            .with_directory("/app", project)
            .with_mounted_cache("/root/.m2", m2_cache)
            .with_workdir("/app")
        )

    @function
    def build(self) -> dagger.Directory:
        return (
            self.build_env()
            .with_exec(["mvn", "clean", "package", "-DskipTests"])
            .directory("/app/target")
        )

    @function
    async def test(self) -> str:
        return await (
            self.build_env()
            .with_exec(["mvn", "test"])
            .stdout()
        )

    @function
    async def publish(self) -> str:
        await self.test()
        return await (
            dag.container()
            .from_(SpringPetclinic.MAVEN_IMAGE)
            .with_directory("/app", self.build())
            .with_entrypoint(["sh", "-c", "java -jar /app/*.jar"])
            .publish(f"ttl.sh/spring-petclinic-{random.randint(0, 100_000_000)}")
        )
