plugins {
    id("com.github.node-gradle.node") version "7.0.2"
}

node {
    version.set("20.11.0")
    npmVersion.set("10.2.4")
    download.set(true)
    workDir.set(file("${project.projectDir}/.gradle/nodejs"))
    npmWorkDir.set(file("${project.projectDir}/.gradle/npm"))
    nodeProjectDir.set(file("${project.projectDir}"))
}

tasks {
    val npmInstall by existing(com.github.gradle.node.npm.task.NpmTask::class) {
        args.set(listOf("install"))
        inputs.file("package.json")
        outputs.dir("node_modules")
    }

    val npmBuild by registering(com.github.gradle.node.npm.task.NpmTask::class) {
        dependsOn(npmInstall)
        args.set(listOf("run", "build"))
        inputs.dir("src")
        inputs.file("package.json")
        inputs.file("vite.config.ts")
        inputs.file("tsconfig.json")
        outputs.dir("build")
    }

    val npmStart by registering(com.github.gradle.node.npm.task.NpmTask::class) {
        dependsOn(npmInstall)
        args.set(listOf("run", "dev"))
    }

    val assemble by registering {
        dependsOn(npmBuild)
    }

    val clean by registering(Delete::class) {
        delete("build")
        delete("node_modules")
    }
}
