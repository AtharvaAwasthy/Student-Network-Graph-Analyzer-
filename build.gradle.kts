
plugins {
    id("java")
    application
}

group = "br.com.unipe.linkedingraph"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("br.com.unipe.linkedingraph.LinkedInApp")
}

tasks.test {
    useJUnitPlatform()
}
