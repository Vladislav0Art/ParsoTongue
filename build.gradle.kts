plugins {
    kotlin("jvm") version "2.1.10"
}

group = "parsotongue"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}