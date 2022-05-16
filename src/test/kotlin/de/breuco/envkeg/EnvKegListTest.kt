package de.breuco.envkeg

import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import kotlin.random.Random
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class EnvKegListTest {
  @AfterEach
  fun resetMocks() {
    clearAllMocks()
  }

  private fun mockEnvVariable(name: String, value: String) {
    mockkStatic(System::class)
    every { System.getenv(name) } returns value
  }

  @Test
  fun oneValueInList() {
    val envVarName = "oneIntListVar"
    val envVarValue: Int = Random.nextInt()
    mockEnvVariable(envVarName, envVarValue.toString())

    val result: List<Int> = parseListFromEnv(envVarName)

    result shouldHaveSize 1
    result[0] shouldBe envVarValue
    result[0].shouldBeTypeOf<Int>()
  }

  @Test
  fun noValueInList() {
    val envVarName = "emptyIntListVar"
    mockEnvVariable(envVarName, "")

    val result: List<Int> = parseListFromEnv(envVarName)

    result shouldHaveSize 0
  }

  @Test
  fun multipleValuesInList() {
    val values = (0..4).map { Random.nextInt() }
    val envVarName = "multipleIntListVar"
    mockEnvVariable(envVarName, values.joinToString())

    val result: List<Int> = parseListFromEnv(envVarName)

    result shouldHaveSize values.size
    result shouldContainInOrder values
  }

  @Test
  fun brokenValuesInList() {
    val values = listOf(1, 2, 4)
    val envVarName = "brokenIntListVar"
    mockEnvVariable(envVarName, "1,2,whatsup,4")

    val result: List<Int> = parseListFromEnv(envVarName)

    result shouldHaveSize values.size
    result shouldContainInOrder values
  }

  @Test
  fun emptyValuesInList() {
    val values = listOf(1, 2, 4)
    val envVarName = "emptyItemsInIntListVar"
    mockEnvVariable(envVarName, "1,2,,4")

    val result: List<Int> = parseListFromEnv(envVarName)

    result shouldHaveSize values.size
    result shouldContainInOrder values
  }
}
