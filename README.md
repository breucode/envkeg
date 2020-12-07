[![GitHub release (latest by date)](https://img.shields.io/github/v/release/breucode/envkeg?style=flat-square)](https://github.com/breucode/envkeg/releases)
[![License](dependencies-badge.svg)](build.gradle.kts)
[![License](https://img.shields.io/github/license/breucode/envkeg?style=flat-square)](LICENSE)

# envkeg
A very small boilerplate-free kotlin library to read values from environment variables in a typesafe way.

## Usage
### Single values
To read an environment variable simply call:

```kotlin
parseFromEnv("myEnvInt", 30)
```

to get `myEnvInt` from the environment as an `Int` or

```kotlin
val nullableInt: Int? = parseFromEnv("myNullableEnvInt")
```
to get `myNullableEnvInt` from the environment as an `Int?`.

If the variable is not present, can't be parsed or the target type is not supported, `30` is returned in the first case, whereas `null` is returned in 
the second case.

### Lists
You can also convert an environment variable to a `List`. Let's say the variable is named `myIntList` and the content is `"1,2,3"`.

```kotlin
val intList: List<Int> = parseListFromEnv("myIntList")
```

returns a `List<Int>` of `[1,2,3]`.

You can also define your own separator by calling for example `parseListFromEnv("myOtherIntList", ';')`.

## Install dependency
This dependency is available at jcenter.

### Gradle
```kotlin
repositories {
    ...
    jcenter()
    ...
}

dependencies {
    ...
    implementation("de.breuco:envkeg:0.5.0")
    ...
}
```

### Maven
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    ...
    <repositories>
        <repository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>central</id>
            <name>bintray</name>
            <url>https://jcenter.bintray.com</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>de.breuco</groupId>
            <artifactId>envkeg</artifactId>
            <version>0.5.0</version>
        </dependency>
    </dependencies>
    ...
</project>
```

## Supported types
| Type           | Parsing function                                                                                                                   |
| -------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Byte           | [String.toByte()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-byte.html)                                           |
| Short          | [String.toShort()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-short.html)                                         |
| Int            | [String.toInt()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-int.html)                                             |
| Long           | [String.toLong()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-long.html)                                           |
| Float          | [String.toFloat()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-float.html)                                         |
| Double         | [String.toDouble()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-double.html)                                       |
| Boolean        | [String.toBoolean()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-boolean.html)                                     |
| LocalDate      | [LocalDate.parse(...)](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html#parse-java.lang.CharSequence-)           |
| LocalDateTime  | [LocalDateTime.parse(...)](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html#parse-java.lang.CharSequence-)   |
| OffsetTime     | [OffsetTime.parse(...)](https://docs.oracle.com/javase/8/docs/api/java/time/OffsetTime.html#parse-java.lang.CharSequence-)         |
| OffsetDateTime | [OffsetDateTime.parse(...)](https://docs.oracle.com/javase/8/docs/api/java/time/OffsetDateTime.html#parse-java.lang.CharSequence-) |
| ZonedDateTime  | [ZonedDateTime.parse(...)](https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html#parse-java.lang.CharSequence-)   |
| Instant        | [Instant.parse(...)](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html#parse-java.lang.CharSequence-)               |
| String         | none                                                                                                                               |
