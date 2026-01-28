plugins {
    java
    id("io.quarkus")
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("com.github.ben-manes.versions") version "0.51.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-kubernetes-config")
    implementation("io.quarkus:quarkus-kubernetes")
    implementation("io.quarkus:quarkus-container-image-jib")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-messaging-kafka")
    implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-micrometer")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-elytron-security-properties-file")
    implementation("io.vavr:vavr:0.10.4")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.quarkus:quarkus-test-kafka-companion")
    testImplementation("uk.co.jemos.podam:podam:8.0.2.RELEASE")
    testImplementation("com.github.mifmif:generex:1.0.2")

    annotationProcessor("io.soabase.record-builder:record-builder-processor:44")
    compileOnly("io.soabase.record-builder:record-builder-core:44")
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
