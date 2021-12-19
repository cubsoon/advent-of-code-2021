package io.github.cubsoon.aoc.aoc2015.untreated

import io.github.cubsoon.aoc.input

fun d1_2015_a() {
    var floor = 0
    input("id1_2015.txt").toCharArray().forEach { char ->
        floor += when (char) {
            '(' -> 1
            ')' -> -1
            else -> 0
        }
    }
    println(floor)
}

fun d1_2015_b() {
    var floor = 0
    var count = 1
    input("id1_2015.txt").toCharArray().forEach { char ->
        floor += when (char) {
            '(' -> 1
            ')' -> -1
            else -> 0
        }
        if (floor < 0) println("basement: $count")
        count++
    }
    println(floor)
}

fun d2_2015_a() {
    println(
        input("id2_2015.txt")
            .split("\n")
            .filter { line -> line.isNotEmpty() }
            .map { line -> line.split('x').map { it.toInt() }.sorted() }
            .map { s -> 3 * s[0] * s[1] + 2 * s[1] * s[2] + 2 * s[0] * s[2] }
            .reduce { acc, i -> acc + i })
}

fun d2_2015_b() {
    println(
        input("id2_2015.txt")
            .split("\n")
            .filter { line -> line.isNotEmpty() }
            .map { line -> line.split('x').map { it.toInt() }.sorted() }
            .map { s -> s[0] + s[0] + s[1] + s[1] + s[0] * s[1] * s[2] }
            .reduce { acc, i -> acc + i })
}

fun d3_2015_a() {
    var x = 0
    var y = 0
    val set = mutableSetOf(x to y)
    input("id3_2015.txt").toCharArray().forEach {
        when (it) {
            '>' -> x += 1
            '<' -> x -= 1
            '^' -> y += 1
            'v' -> y -= 1
        }
        set.add(x to y)
    }
    println(
        set.size
    )
}

fun d3_2015_b() {
    var x = 0
    var y = 0
    var z = 0
    var a = 0
    val set = mutableSetOf(x to y)
    input("id3_2015.txt").toCharArray().forEachIndexed { idx, dir ->
        if (idx % 2 == 0) when (dir) {
            '>' -> x += 1
            '<' -> x -= 1
            '^' -> y += 1
            'v' -> y -= 1
        } else when (dir) {
            '>' -> z += 1
            '<' -> z -= 1
            '^' -> a += 1
            'v' -> a -= 1
        }
        set.add(x to y)
        set.add(z to a)
    }
    println(
        set.size
    )
}