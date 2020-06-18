import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    id("com.diffplug.gradle.spotless") version "4.3.1"
    id("io.gitlab.arturbosch.detekt") version "1.9.1"
    id("com.github.ben-manes.versions") version "0.28.0"
}

val javaVersion = JavaVersion.VERSION_1_8.toString()

group = "de.breuco"
version = "0.1.0-SNAPSHOT"

spotless {
    val ktlintVersion = "0.37.2"
    kotlin {
        ktlint(ktlintVersion).userData(
            mapOf(
                Pair("max_line_length", "120")
            )
        )
    }
    kotlinGradle {
        target("*.gradle.kts")

        ktlint(ktlintVersion).userData(
            mapOf(
                Pair("max_line_length", "120")
            )
        )
    }
}

detekt {
    failFast = true
    buildUponDefaultConfig = true
    config = files("$projectDir/detekt.yaml")
    baseline = file("$projectDir/detekt-baseline.xml")
}

tasks {
    withType<Detekt> {
        this.jvmTarget = javaVersion
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

repositories {
    mavenCentral()
    jcenter()
}

configurations.all {
    // Excluded, because we only want to use JUnit 5
    exclude("junit")
    exclude("org.junit.vintage")
}

dependencies {
    val kotestVersion = "4.0.6"
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-arrow-jvm:$kotestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.1")

    testImplementation("io.mockk:mockk:1.10.0")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = javaVersion
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = javaVersion
}
