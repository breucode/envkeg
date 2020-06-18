package de.breuco.envkeg

class Envkeg private constructor() {
    companion object {
        /**
         * Reads, parses and returns an environment variable based on the type of <code>default</code>
         *
         * @param name the name of the environment variable to read from
         * @param default the default value, which will be returned, if the environment variable is not
         * present or cannot be parsed. The type of <code>default</code> determines the return type
         *
         * @return the parsed environment variable or the default
         */
        inline fun <reified R> getFromEnvTyped(name: String, default: R): R {
            return try {
                val envVar: String? = System.getenv(name)
                checkNotNull(envVar)

                when (R::class) {
                    Byte::class -> envVar.toByte() as R
                    Short::class -> envVar.toShort() as R
                    Int::class -> envVar.toInt() as R
                    Long::class -> envVar.toLong() as R

                    Float::class -> envVar.toFloat() as R
                    Double::class -> envVar.toDouble() as R

                    Boolean::class -> envVar.toBoolean() as R
                    String::class -> envVar as R
                    else -> error("Unsupported type")
                }
            } catch (e: Throwable) {
                default
            }
        }
    }
}
