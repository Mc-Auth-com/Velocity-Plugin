plugins {
    java
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.runVelocity)
    alias(libs.plugins.shadow)
}

version = "1.0.0-SNAPSHOT"

sonar {
    properties {
        property("sonar.projectName", rootProject.name)
        property("sonar.projectKey", "Mc-Auth-com_Velocity-Plugin")
        property("sonar.organization", "mc-auth-com")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

repositories {
    mavenCentral()
    maven {
        name = "sprax-repo"
        url = uri("https://repo.sprax2013.de/repository/maven-public/")
    }
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)

    implementation(libs.sprax.lime)
    implementation(libs.postgresql)
    implementation(libs.j256.twoFactorAuth)
}

tasks {
    shadowJar {
        archiveBaseName = rootProject.name.replace(' ', '-')
        archiveClassifier = ""

        enableRelocation = true
        relocationPrefix = "com.mc_auth.velocity_plugin.libs"
        mergeServiceFiles()

        minimize {
            exclude(dependency(libs.postgresql))
        }
    }

    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}

var targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

val generateTemplates by tasks.registering(Copy::class) {
    val props = mapOf(
        "version" to project.version,
        "projectName" to rootProject.name,
    )
    from(file("src/main/templates"))
    into(layout.buildDirectory.dir("generated/sources/templates"))
    expand(props)
    inputs.properties(props)
}

sourceSets.main {
    java.srcDir(generateTemplates.map { it.destinationDir })
}
