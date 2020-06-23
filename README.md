# envkeg
A very small boilerplate-free kotlin library to read values from environment variables in a typesafe way.

## Usage
To read an environment variable simply call:

```kotlin
Envkeg.getFromEnvTyped("myEnvInt", 30)
```

to get `myEnvInt` from the environment as an `Int` or

```kotlin
val nullableInt: Int? = Envkeg.getFromEnvTyped("myNullableEnvInt")
```
to get `myNullableEnvInt` from the environment as an `Int?`.

If the variable is not present, can't be parsed or the target type is not supported, `30` is returned in the first case, whereas `null` is returned in 
the second case.

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
    implementation("de.breuco:envkeg:0.2.0")
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
            <version>0.2.0</version>
        </dependency>
    </dependencies>
    ...
</project>
```

## Supported types
| Type    | Parsing function                                                                               |
| ------- | ---------------------------------------------------------------------------------------------- |
| Byte    | [String.toByte()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-byte.html)       |
| Short   | [String.toShort()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-short.html)     |
| Int     | [String.toInt()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-int.html)         |
| Long    | [String.toLong()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-long.html)       |
| Float   | [String.toFloat()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-float.html)     |
| Double  | [String.toDouble()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-double.html)   |
| Boolean | [String.toBoolean()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-boolean.html) |
| String  | none                                                                                           |