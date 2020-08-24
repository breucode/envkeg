import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date

plugins {
    kotlin("jvm") version "1.4.0"
    id("com.diffplug.spotless") version "5.1.2"
    id("io.gitlab.arturbosch.detekt") version "1.11.2"
    id("com.github.ben-manes.versions") version "0.29.0"
    id("maven-publish")
    id("org.jetbrains.dokka") version "0.10.1"
    id("com.jfrog.bintray") version "1.8.5"
}

val javaVersion = JavaVersion.VERSION_1_8

val pomDesc = "A very small boilerplate-free kotlin library to read values " +
    "from environment variables in a typesafe way"
val artifactName = "envkeg"
val artifactGroup = "de.breuco"
group = artifactGroup
val artifactVersion = "0.3.0"
version = artifactVersion

spotless {
    val ktlintVersion = "0.38.0"
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
    val kotestVersion = "4.2.0"
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion") {
        exclude("junit")
        exclude("org.junit.vintage")
    }
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-arrow-jvm:$kotestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

    testImplementation("io.mockk:mockk:1.10.0")
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
                    }
                }
            }
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("bintray")

    pkg.apply {
        repo = "breucode"
        name = artifactName
        userOrg = "breucode"
        setLicenses("MIT")
        vcsUrl = "https://github.com/breucode/envkeg.git"
        version.apply {
            name = artifactVersion
            desc = pomDesc
            released = Date().toString()
            vcsTag = artifactVersion
        }
    }
}
