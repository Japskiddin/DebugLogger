package io.github.japskiddin.debuglogger.model

public enum class Level(
  private val value: String
) {
  INFO("I"),
  WARN("W"),
  ERROR("E"),
  DEBUG("D");

  override fun toString(): String = value
}
