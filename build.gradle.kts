import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
    id("com.diffplug.spotless") version "5.8.2"
    id("io.gitlab.arturbosch.detekt") version "1.14.2"
    id("com.github.ben-manes.versions") version "0.36.0"
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.4.20"
}

val javaVersion = JavaVersion.VERSION_1_8

val pomDesc = "A very small boilerplate-free kotlin library to read values " +
    "from environment variables in a typesafe way"
val artifactName = "envkeg"
val artifactGroup = "de.breuco"
group = artifactGroup
val artifactVersion = "0.4.1"
version = artifactVersion

spotless {
    val ktlintVersion = "0.40.0"
    kotlin {
        ktlint(ktlintVersion)
    }
    kotlinGradle {
        target("*.gradle.kts")

        ktlint(ktlintVersion)
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
        this.jvmTarget = javaVersion.toString()
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
    rejectVersionIf {
        isNonStable(candidate.version)
    }

    revision = "release"
    gradleReleaseChannel = "current"
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val kotestVersion = "4.3.1"
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion") {
        exclude("junit")
        exclude("org.junit.vintage")
    }
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-arrow-jvm:$kotestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    testImplementation("io.mockk:mockk:1.10.0")
}

kotlin {
    explicitApi()
}

tasks.test {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = javaVersion.toString()

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions.jvmTarget = javaVersion.toString()

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("bintray") {
            groupId = artifactGroup
            artifactId = artifactName
            version = version
            from(components["java"])

            artifact(sourcesJar)
            artifact(dokkaJar)

            pom {
                name.set(artifactName)
                description.set(pomDesc)
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
                        name.set("Pascal Breuer")
                        email.set("pbreuer@breuco.de")
                    }
                }
                scm {
                    url.set("https://github.com/breucode/envkeg.git")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://api.bintray.com/maven/breucode/breucode/envkeg")
            credentials {
                username = System.getenv("BINTRAY_USER")
                password = System.getenv("BINTRAY_KEY")
            }
        }
    }
}
