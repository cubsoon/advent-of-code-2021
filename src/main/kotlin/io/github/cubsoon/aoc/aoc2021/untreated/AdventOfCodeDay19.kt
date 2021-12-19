package io.github.cubsoon.aoc.aoc2021.untreated

import io.github.cubsoon.aoc.input
import kotlin.math.absoluteValue

data class Position(val x: Int, val y: Int, val z: Int) {
    fun rotated(rotation: Rotation): Position {
        return when (rotation.type) {
            1 -> Position(x, y, z)
            2 -> Position(x, z, -y)
            3 -> Position(x, -y, -z)
            4 -> Position(x, -z, y)
            5 -> Position(y, -x, z)
            6 -> Position(y, z, x)
            7 -> Position(y, x, -z)
            8 -> Position(y, -z, -x)
            9 -> Position(-x, -y, z)
            10 -> Position(-x, -z, -y)
            11 -> Position(-x, y, -z)
            12 -> Position(-x, z, y)
            13 -> Position(-y, x, z)
            14 -> Position(-y, -z, x)
            15 -> Position(-y, -x, -z)
            16 -> Position(-y, z, -x)
            17 -> Position(z, y, -x)
            18 -> Position(z, x, y)
            19 -> Position(z, -y, x)
            20 -> Position(z, -x, -y)
            21 -> Position(-z, -y, -x)
            22 -> Position(-z, -x, y)
            23 -> Position(-z, y, x)
            24 -> Position(-z, x, -y)
            else -> throw IllegalArgumentException()
        }
    }

    fun translated(x: Int, y: Int, z: Int): Position {
        return Position(this.x + x, this.y + y, this.z + z)
    }
}

class Rotation private constructor(val type: Int) {
    companion object {
        fun allRotations(): List<Rotation> {
            return (1..24).map { Rotation(it) }
        }
    }
}

class Scanner(val beacons: Set<Position>, val scanners: Set<Position>) {
    fun merge(scanner: Scanner): Scanner? {
        Rotation.allRotations().forEach { rotation ->
            this.beacons.forEach { beaconToMatch ->
                val rotatedBeacons = scanner.beacons.map { it.rotated(rotation) }
                rotatedBeacons.forEach { beacon ->
                    val x = beaconToMatch.x - beacon.x
                    val y = beaconToMatch.y - beacon.y
                    val z = beaconToMatch.z - beacon.z
                    val translated = rotatedBeacons.map { it.translated(x, y, z) }
                    val match = translated.filter { this.beacons.contains(it) }.size >= 12
                    if (match) {
                        return Scanner(beacons + translated, scanners + Position(x, y, z))
                    }
                }
            }
        }
        return null
    }
}

fun main() {
    val input = input("d19_2021.txt")
    val beaconLists: MutableList<MutableList<Position>> = mutableListOf()
    var current = -1
    input.lines().forEach { line ->
        if (line.startsWith("---")) {
            current++
            beaconLists.add(mutableListOf())
        } else if (line.isNotBlank()) {
            beaconLists[current].add(line.trim().split(',').let {
                Position(it[0].toInt(), it[1].toInt(), it[2].toInt())
            })
        }
    }
    val scanners = beaconLists.map {
        Scanner(it.toSet(), setOf(Position(0, 0, 0)))
    }

    var merged = scanners.first()
    val notMerged = scanners.drop(1).toMutableList()
    while (notMerged.isNotEmpty()) {
        var mergedAnything = false
        notMerged.toList().forEach {
            val mergeResult = merged.merge(it)
            if (mergeResult != null) {
                println("Merged a scanner.")
                merged = mergeResult
                notMerged.remove(it)
                mergedAnything = true
            }
        }
        if (!mergedAnything) {
            val other = notMerged.first()
            notMerged.remove(other)
            notMerged.add(merged)
            merged = other
        }
    }

    println(merged.beacons.size)
    println(merged.scanners.flatMap { one ->
        merged.scanners.map { two ->
            (two.x - one.x).absoluteValue + (two.y - one.y).absoluteValue + (two.z - one.z).absoluteValue
        }
    }.sortedBy { it })
}