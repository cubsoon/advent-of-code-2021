package io.github.cubsoon.aoc

private val TOP_LEVEL_CLASS = object {}.javaClass

fun input(filename: String): String = TOP_LEVEL_CLASS.getResource("/$filename")?.readText()
    ?: throw AddResourceDumbassException("No input file $filename.")

class AddResourceDumbassException(message: String?) : Exception(message)
