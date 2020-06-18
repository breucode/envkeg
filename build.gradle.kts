import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    id("com.diffplug.gradle.spotless") version "4.3.1"
    id("io.gitlab.arturbosch.detekt") version "1.9.1"
    id("com.github.ben-manes.versions") version "0.28.0"
    id("maven-publish")
    id("org.jetbrains.dokka") version "0.10.1"
}

val javaVersion = JavaVersion.VERSION_1_8.toString()

val groupString = "de.breuco"
group = groupString
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

dependencies {
    val kotestVersion = "4.0.6"
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion") {
        exclude("junit")
        exclude("org.junit.vintage")
    }
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

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            groupId = groupString
            artifactId = "envkeg"
            version = version
            from(components["java"])

            artifact(sourcesJar)
            artifact(dokkaJar)

            pom {
                name.set("envkeg")
                description.set(
                    "A very small boilerplate-free kotlin library to read values " +
                        "from environment variables in a typesafe way"
                )
                url.set("https://github.com/breucode/envkeg")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/breucode/envkeg/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("breucode")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/breucode/envkeg")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
