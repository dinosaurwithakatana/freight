package io.dwak.freight.processor.extension


fun String.capitalizeFirst() : String {
  return "${this[0].toUpperCase()}${this.substring(1, this.length)}"
}

fun String.ifEmpty(default: String) : String {
  return if(isEmpty()) default else this
}

