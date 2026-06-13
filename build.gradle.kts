plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    application
}

group = "org.unirio.bsi.coordenacao"
version = "1.0.0"

val propertiesFile = "app.properties"

repositories {
    mavenCentral()
}

dependencies {
    val exposedVersion = "1.3.0"

    implementation("org.apache.poi:poi-ooxml:5.5.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.3.21")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.11")
    implementation("com.github.ajalt.clikt:clikt:5.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
    compilerOptions {
        // Define a versão da linguagem (language version) e da API
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_4)
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_4)
        jvmToolchain(23)
    }
}

application {
    mainClass.set("main.MainKt")
}

distributions {
    main {
        contents {
            from(propertiesFile) {
                into("bin")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Main-Class" to "main.MainKt",
            "Class-Path" to configurations.runtimeClasspath.get().map { it -> it.name }.joinToString(" ")
        ))
    }
}

tasks.register<Copy>("copyDependencies") {
    from(configurations.runtimeClasspath)
    into(layout.buildDirectory.dir("libs"))
}

tasks.register<Copy>("copyProperties") {
    from(propertiesFile)
    into(layout.buildDirectory.dir("libs"))
}

tasks.named("build") {
    finalizedBy("copyProperties")
    finalizedBy("copyDependencies")
}