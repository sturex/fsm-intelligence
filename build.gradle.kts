plugins {
    id("java")
}

group = "dev.sturex"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}