package io.github.cubsoon.aoc.aoc2021.untreated

import io.github.cubsoon.aoc.input

import java.lang.Integer.max
import java.lang.Integer.min

data class Cuboid(
    val xmin: Int,
    val xmax: Int,
    val ymin: Int,
    val ymax: Int,
    val zmin: Int,
    val zmax: Int
) {
    companion object {
        fun create(repr: String): Cuboid {
            val parts = repr.split(",").flatMap { it.substring(2).split("..") }
            return Cuboid(
                parts[0].toInt(), parts[1].toInt(), parts[2].toInt(), parts[3].toInt(), parts[4].toInt(),
                parts[5].toInt()
            )
        }
    }

    fun notEmpty(): Boolean {
        return xmin <= xmax && ymin <= ymax && zmin <= zmax
    }

    fun intersect(cuboid: Cuboid): Cuboid? {
        return Cuboid(
            max(xmin, cuboid.xmin),
            min(xmax, cuboid.xmax),
            max(ymin, cuboid.ymin),
            min(ymax, cuboid.ymax),
            max(zmin, cuboid.zmin),
            min(zmax, cuboid.zmax),
        ).takeIf { it.notEmpty() }
    }

    fun volume(): Long {
        return (xmax - xmin + 1L) * (ymax - ymin + 1L) * (zmax - zmin + 1L)
    }
}

fun main() {
    val input = input("d22_2021.txt")
    val seq = input.lines()
        .map { it.trim().split(" ") }
        .map {
            when (it.first()) {
                "on" -> true
                else -> false
            } to Cuboid.create(it.last())
        }

    val cuboids = mutableListOf<Pair<Boolean, Cuboid>>()
    for (current in seq) {
        val intersections = cuboids.mapNotNull { current.second.intersect(it.second)?.let { i -> it.first to i } }
            .map { !it.first to it.second }

        cuboids.addAll(intersections)
        if (current.first) cuboids.add(current)
    }

    println(cuboids.sumOf { it.second.volume() * if (it.first) 1 else -1 })
}