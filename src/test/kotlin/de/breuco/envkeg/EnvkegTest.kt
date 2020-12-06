package de.breuco.envkeg

import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime

class EnvkegTest {

    @AfterEach
    fun resetMocks() {
        clearAllMocks()
    }

    private fun mockEnvVariable(name: String, value: String) {
        mockkStatic(System::class)
        every { System.getenv(name) } returns value
    }

    @Test
    fun readByte() {
        val envVarName = "byteVar"
        val envVarValue: Byte = 2
        val default: Byte = 3
        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv(envVarName, default)

        result shouldBe envVarValue
        result.shouldBeTypeOf<Byte>()
    }

    @Test
    fun readShort() {
        val envVarName = "shortVar"
        val envVarValue: Short = 2
        val default: Short = 3
        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv(envVarName, default)

        result shouldBe envVarValue
        result.shouldBeTypeOf<Short>()
    }

    @Test
    fun readInt() {
        val envVarName = "intVar"
        val envVarValue = 2
        val default = 3
        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv(envVarName, default)

        result shouldBe envVarValue
        result.shouldBeTypeOf<Int>()
    }

    @Test
    fun readLong() {
        val envVarName = "longVar"
        val envVarValue = 2L
        val default = 3L
        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv(envVarName, default)

        result shouldBe envVarValue
        result.shouldBeTypeOf<Long>()
    }

    @Test
    fun readFloat() {
        val envVarName = "floatVar"
        val envVarValue = 2.0f
        val default = 3.0f
        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv(envVarName, default)

        result shouldBe envVarValue
        result.shouldBeTypeOf<Float>()
    }

    @Test
    fun readDouble() {
        val envVarName = "doubleVar"
        val envVarValue = 2.0
        val default = 3.0
        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv(envVarName, default)

        result shouldBeExactly envVarValue
        result.shouldBeTypeOf<Double>()
    }

    @Test
    fun readBoolean() {
        val envVarName = "booleanVar"
        val envVarValue = true
        val default = false
        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv(envVarName, default)

        result shouldBe envVarValue
        result.shouldBeTypeOf<Boolean>()
    }

    @Test
    fun readString() {
        val envVarName = "stringVar"
        val envVarValue = "helloWorld"
        val default = "defaultValue"
        mockEnvVariable(envVarName, envVarValue)

        val result = parseFromEnv(envVarName, default)

        result shouldBe envVarValue
        result.shouldBeTypeOf<String>()
    }

    @Test
    fun readNullableStringNotPresent() {
        val envVarName = "stringVar"

        val result: String? = parseFromEnv(envVarName)

        result.shouldBeNull()
    }

    @Test
    fun readByteNotPresent() {
        val envVarName = "byteVarNotPresent"
        val default: Byte = 3

        val result = parseFromEnv(envVarName, default)

        result shouldBe default
        result.shouldBeTypeOf<Byte>()
    }

    @Test
    fun readShortNotPresent() {
        val envVarName = "shortVarNotPresent"
        val default: Short = 3

        val result = parseFromEnv(envVarName, default)

        result shouldBe default
        result.shouldBeTypeOf<Short>()
    }

    @Test
    fun readIntNotPresent() {
        val envVarName = "intVarNotPresent"
        val default = 3

        val result = parseFromEnv(envVarName, default)

        result shouldBe default
        result.shouldBeTypeOf<Int>()
    }

    @Test
    fun readLongNotPresent() {
        val envVarName = "longVarNotPresent"
        val default = 3L

        val result = parseFromEnv(envVarName, default)

        result shouldBe default
        result.shouldBeTypeOf<Long>()
    }

    @Test
    fun readFloatNotPresent() {
        val envVarName = "floatVarNotPresent"
        val default = 3.0f

        val result = parseFromEnv(envVarName, default)

        result shouldBe default
        result.shouldBeTypeOf<Float>()
    }

    @Test
    fun readDoubleNotPresent() {
        val envVarName = "doubleVarNotPresent"
        val default = 3.0

        val result = parseFromEnv(envVarName, default)

        result shouldBeExactly default
        result.shouldBeTypeOf<Double>()
    }

    @Test
    fun readBooleanNotPresent() {
        val envVarName = "booleanVarNotPresent"
        val default = false

        val result = parseFromEnv(envVarName, default)

        result shouldBe default
        result.shouldBeTypeOf<Boolean>()
    }

    @Test
    fun readStringNotPresent() {
        val envVarName = "stringVarNotPresent"
        val default = "defaultValue"

        val result = parseFromEnv(envVarName, default)

        result shouldBe default
        result.shouldBeTypeOf<String>()
    }

    @Test
    fun unsupportedType() {
        val envVarName = "unsupportedTypeVar"
        val envVarValue = UnsupportedTypeForTesting("envVarValue")
        val default = UnsupportedTypeForTesting("defaultValue")

        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv(envVarName, default)

        result shouldBe default
        result.shouldBeTypeOf<UnsupportedTypeForTesting>()
    }

    @Test
    fun localDateType() {
        val envVarName = "localDateVar"
        val envVarValue = LocalDate.now()

        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv<LocalDate>(envVarName)

        result shouldBe envVarValue
        result.shouldBeTypeOf<LocalDate>()
    }

    @Test
    fun localDateTimeType() {
        val envVarName = "localDateTimeVar"
        val envVarValue = LocalDateTime.now()

        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv<LocalDateTime>(envVarName)

        result shouldBe envVarValue
        result.shouldBeTypeOf<LocalDateTime>()
    }

    @Test
    fun offsetTimeType() {
        val envVarName = "offsetTimeVar"
        val envVarValue = OffsetTime.now()

        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv<OffsetTime>(envVarName)

        result shouldBe envVarValue
        result.shouldBeTypeOf<OffsetTime>()
    }

    @Test
    fun offsetDateTimeType() {
        val envVarName = "offsetDateTimeVar"
        val envVarValue = OffsetDateTime.now()

        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv<OffsetDateTime>(envVarName)

        result shouldBe envVarValue
        result.shouldBeTypeOf<OffsetDateTime>()
    }

    @Test
    fun zonedDateTimeType() {
        val envVarName = "zonedDateTimeVar"
        val envVarValue = ZonedDateTime.now()

        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv<ZonedDateTime>(envVarName)

        result shouldBe envVarValue
        result.shouldBeTypeOf<ZonedDateTime>()
    }

    @Test
    fun instantType() {
        val envVarName = "instantVar"
        val envVarValue = Instant.now()

        mockEnvVariable(envVarName, envVarValue.toString())

        val result = parseFromEnv<Instant>(envVarName)

        result shouldBe envVarValue
        result.shouldBeTypeOf<Instant>()
    }
}
