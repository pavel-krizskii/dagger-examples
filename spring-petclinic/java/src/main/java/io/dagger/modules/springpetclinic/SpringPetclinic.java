package io.dagger.modules.springpetclinic;

import io.dagger.client.CacheVolume;
import io.dagger.client.Container;
import io.dagger.client.Directory;
import io.dagger.client.Service;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;

import java.util.List;
import java.util.Random;

import static io.dagger.client.Dagger.dag;

@Object
public class SpringPetclinic {

    private static final String MAVEN_IMAGE = "maven:3.9.9-eclipse-temurin-21";

    @Function
    public Container buildEnv() {
        Directory project = dag()
                .git("https://github.com/spring-projects/spring-petclinic")
                .commit("332abbcb8a37bf5430c0927b2109a04cc8ac0e93")
                .tree();
        CacheVolume m2Cache = dag().cacheVolume("spring-petclinic-m2");
        return dag().container()
                .from(MAVEN_IMAGE)
                .withDirectory("/app", project)
                .withMountedCache("/root/.m2", m2Cache)
                .withWorkdir("/app");
    }

    @Function
    public Directory build() throws Exception {
        return this
                .buildEnv()
                .withExec(List.of("mvn", "clean", "package", "-DskipTests"))
                .directory("/app/target");
    }

    @Function
    public String test() throws Exception {
        return this
                .buildEnv()
                .withExec(List.of("mvn", "test"))
                .stdout();
    }

    @Function
    public String publish() throws Exception {
        this.test();
        Random rand = new Random();
        return dag()
                .container()
                .from(MAVEN_IMAGE)
                .withDirectory("/app", build())
                .withEntrypoint(List.of("sh", "-c", "java -jar /app/*.jar"))
                .publish("ttl.sh/spring-petclinic-" + rand.nextInt(100_000_000));
    }
}
