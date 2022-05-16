package de.breuco.envkeg

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime

/**
 * Reads and returns an environment variable based on the type of <code>default</code>
 *
 * @param name the name of the environment variable to read from
 * @param default the default value, which will be returned The type of <code>default</code>
 * determines the return type
 *
 * @return the parsed environment variable or the default, if the environment variable is not
 * present or cannot be parsed
 */
public inline fun <reified R> parseFromEnv(name: String, default: R): R =
  parseFromEnv<R>(name) ?: default

/**
 * Reads and returns an environment variable based on type R
 *
 * @param name the name of the environment variable to read from
 *
 * @return the parsed environment variable or null, if the environment variable is not present or
 * cannot be parsed
 */
public inline fun <reified R> parseFromEnv(name: String): R? =
  System.getenv(name)?.let { convertToType(it) }

public inline fun <reified R : Any> parseFromEnv(name: String, parser: (value: String) -> R): R? =
  parseFromEnv<String>(name)?.let { parser.invoke(it) }

public inline fun <reified R : Any> parseFromEnv(
  name: String,
  default: R,
  parser: (value: String) -> R
): R = parseFromEnv<String>(name)?.let { parser.invoke(it) } ?: default

/**
 * Reads and returns an environment variable as a List<R>
 *
 * @param name the name of the environment variable to read from
 * @param separator the separator which splits the values of the environment variable
 *
 * @return the values of the environment variable split by <code>separator</code>
 */
public inline fun <reified R : Any> parseListFromEnv(name: String, separator: Char = ','): List<R> =
  System.getenv(name)?.let { envVar ->
    envVar
      .split(separator)
      .map { it.trim() }
      .filter { it.isNotBlank() }
      .mapNotNull { convertToType(it) }
  }
    ?: emptyList()

public inline fun <reified R : Any> parseListFromEnv(
  name: String,
  separator: Char = ',',
  parser: (value: String) -> R
): List<R> =
  System.getenv(name)?.let { envVar ->
    envVar.split(separator).map { it.trim() }.filter { it.isNotBlank() }.map { parser.invoke(it) }
  }
    ?: emptyList()

/**
 * Converts the <code>value</code> String to an R?
 *
 * @param value the value to convert
 *
 * @return value as an R or null, if the target type is not supported or can't be parsed
 */
@Suppress("SwallowedException", "ComplexMethod")
public inline fun <reified R> convertToType(value: String): R? =
  runCatching {
      when (R::class) {
        Byte::class -> value.toByte() as R
        Short::class -> value.toShort() as R
        Int::class -> value.toInt() as R
        Long::class -> value.toLong() as R
        Float::class -> value.toFloat() as R
        Double::class -> value.toDouble() as R
        Boolean::class -> value.toBoolean() as R
        String::class -> value as R
        LocalDate::class -> LocalDate.parse(value) as R
        LocalDateTime::class -> LocalDateTime.parse(value) as R
        OffsetTime::class -> OffsetTime.parse(value) as R
        OffsetDateTime::class -> OffsetDateTime.parse(value) as R
        ZonedDateTime::class -> ZonedDateTime.parse(value) as R
        Instant::class -> Instant.parse(value) as R
        else -> null
      }
    }
    .getOrNull()
