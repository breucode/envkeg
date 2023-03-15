package de.breuco.envkeg

import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlin.random.Random
import org.junit.jupiter.api.Test
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables

class EnvKegListTest {

  @Test
  fun oneValueInList() {
    val envVarName = "oneIntListVar"
    val envVarValue: Int = Random.nextInt()
    EnvironmentVariables(envVarName, envVarValue.toString()).execute {
      val result: List<Int> = parseListFromEnv(envVarName)

      result shouldHaveSize 1
      result[0] shouldBe envVarValue
      result[0].shouldBeTypeOf<Int>()
    }
  }

  @Test
  fun noValueInList() {
    val envVarName = "emptyIntListVar"
    EnvironmentVariables(envVarName, "").execute {
      val result: List<Int> = parseListFromEnv(envVarName)

      result shouldHaveSize 0
    }
  }

  @Test
  fun multipleValuesInList() {
    val values = (0..4).map { Random.nextInt() }
    val envVarName = "multipleIntListVar"

    EnvironmentVariables(envVarName, values.joinToString()).execute {
      val result: List<Int> = parseListFromEnv(envVarName)

      result shouldHaveSize values.size
      result shouldContainInOrder values
    }
  }

  @Test
  fun brokenValuesInList() {
    val values = listOf(1, 2, 4)
    val envVarName = "brokenIntListVar"

    EnvironmentVariables(envVarName, "1,2,whatsup,4").execute {
      val result: List<Int> = parseListFromEnv(envVarName)

      result shouldHaveSize values.size
      result shouldContainInOrder values
    }
  }

  @Test
  fun emptyValuesInList() {
    val values = listOf(1, 2, 4)
    val envVarName = "emptyItemsInIntListVar"

    EnvironmentVariables(envVarName, "1,2,,4").execute {
      val result: List<Int> = parseListFromEnv(envVarName)

      result shouldHaveSize values.size
      result shouldContainInOrder values
    }
  }
}
