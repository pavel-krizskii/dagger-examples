import random
from typing import Annotated

import dagger
from dagger import DefaultPath, dag, function, object_type


@object_type
class PythonApp:
    @function
    def build_env(
        self,
        source: Annotated[dagger.Directory, DefaultPath(".")],
    ) -> dagger.Container:
        fixed_source = (
            source
            .without_directory("__pycache__")
            .without_directory(".venv")
        )
        return (
            dag.container()
            .from_("python:3.13.3-slim") 
            .with_directory("/app", fixed_source)
            .with_workdir("/app")
            .with_exec(["pip", "install", "-r", "requirements.txt"])
        )
        
    @function
    async def test(
        self,
        source: Annotated[dagger.Directory, DefaultPath(".")],
    ) -> str:
        return await (
            self.build_env(source)
            .with_exec(
                ["python", "-u", "-m", "unittest", "discover", "-s", "tests", "-v"],
                expect = dagger.ReturnType.ANY,
            )
            .stderr()
        )

    @function
    async def publish(
        self,
        source: Annotated[dagger.Directory, DefaultPath(".")],
    ) -> str:
        await self.test(source)
        return await (
            self.build_env(source)
            .with_entrypoint(["python", "app.py"])
            .publish(f"ttl.sh/python-app-{random.randrange(10**8)}")
        )
