plugins {
    java
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

var targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}
