import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.ui.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.CheckoutMode
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

version = "2025.03"

project {
    vcsRoot(DaggerExamplesVcsRoot)

    buildType(BuildTestPublishOneStep)
}

object BuildTestPublishOneStep : BuildType({
    name = "Build, test, publish in one step"

    params {
        password("env.DAGGER_CLOUD_TOKEN", "credentialsJSON:fbf1dc1e-e4a2-4b69-85a6-d78acd8670a6")
    }

    steps {
        script {
            name = "Install Dagger"
            scriptContent = """
                curl -fsSL https://dl.dagger.io/dagger/install.sh | BIN_DIR=/tmp/dagger/bin DAGGER_VERSION=0.18.2 sh
            """.trimIndent()
        }
        script {
            name = "Build, test, publish"
            scriptContent = """
                cd %teamcity.build.checkoutDir%/spring-petclinic/python
                dagger call publish
            """.trimIndent()
        }
    }

    vcs {
        root(DaggerExamplesVcsRoot)
        cleanCheckout = true
        checkoutMode = CheckoutMode.ON_AGENT
        showDependenciesChanges = true
    }

    requirements {
        exists("docker.server.version")
    }
})

object DaggerExamplesVcsRoot : GitVcsRoot({
    name = "dagger-examples"
    url = "https://github.com/pavel-krizskii/dagger-examples.git"
    branch = "main"
    branchSpec = "+:refs/heads/*"
    authMethod = anonymous()
})