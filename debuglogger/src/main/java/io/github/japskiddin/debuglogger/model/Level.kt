package io.github.japskiddin.debuglogger.model

enum class Level(private val value: String) {
    INFO("INFO"),
    WARN("WARN"),
    ERROR("ERROR"),
    DEBUG("DEBUG");

    override fun toString(): String = value
}