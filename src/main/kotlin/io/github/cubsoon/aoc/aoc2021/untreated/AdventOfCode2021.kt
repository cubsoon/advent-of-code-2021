@file:Suppress("FunctionName", "DuplicatedCode")

/**
 * This file and package contains raw, untreated solutions that "just worked" at the time.
 * I was trying to solve the puzzles as fast as I can, so please disregard style and efficiency.
 */

package io.github.cubsoon.aoc.aoc2021.untreated

import io.github.cubsoon.aoc.input
import java.util.PriorityQueue
import java.util.regex.Pattern
import kotlin.math.absoluteValue
import kotlin.math.sign


fun d1_2021_a() {
    val a = input("d1_2021.txt")

    val ints = a.lines()
        .map { it.toInt() }
        .toList()

    var count = 0
    for (i in IntRange(1, ints.size - 1)) {
        if (ints[i] > ints[i - 1]) count++
    }

    println(count)
}

fun d1_2021_b() {
    val a = input("d1_2021.txt")

    val ints = a.lines()
        .map { it.toInt() }
        .toList()

    var count = 0
    for (i in IntRange(3, ints.size - 1)) {
        val currentWindow =
            listOf(ints.getOrNull(i), ints.getOrNull(i - 1), ints.getOrNull(i - 2)).filterNotNull().sum()
        val prevWindow =
            listOf(ints.getOrNull(i - 1), ints.getOrNull(i - 3), ints.getOrNull(i - 2)).filterNotNull().sum()
        if (currentWindow > prevWindow) {
            count++
        }
    }

    println(count)
}

fun d2_2021_a() {
    val input = input("d2_2021.txt")

    var d = 0
    var h = 0
    val list = input.lines().filter { it.isNotBlank() }
        .map { it.split(' ') }
        .forEach {
            d += when (it[0]) {
                "up" -> it[1].toInt()
                "down" -> -it[1].toInt()
                else -> 0
            }
            h += when (it[0]) {
                "forward" -> it[1].toInt()
                else -> 0
            }
        }
    var result = d * h

    println("result a $result")
}

fun d2_2021_b() {
    val input = input("d2_2021.txt")

    var d = 0
    var h = 0
    var aim = 0
    val list = input.lines().filter { it.isNotBlank() }
        .map { it.split(' ') }
        .forEach {
            aim += when (it[0]) {
                "up" -> it[1].toInt()
                "down" -> -it[1].toInt()
                else -> 0
            }
            h += when (it[0]) {
                "forward" -> it[1].toInt()
                else -> 0
            }
            d += when (it[0]) {
                "forward" -> it[1].toInt() * aim
                else -> 0
            }
        }
    var result = d * h

    println("result a $result")
}


fun day4_2021_b() {
    val input = input("d4_2021.txt")

    val calls: List<Int> = input.lines().first().split(',').map { it.toInt() }

    val boards: List<List<List<Int>>> = input.lines().drop(1).filter { it.isNotBlank() }
        .chunked(5)
        .map { it.map { it.split(Pattern.compile("\\s+")).filter { it.isNotBlank() } }.map { it.map { it.toInt() } } }

    println("")

    fun countScore(called: List<Int>, board: List<List<Int>>): Int {
        var sum = 0
        for (i in IntRange(0, 4)) {
            for (j in IntRange(0, 4)) {
                if (!called.contains(board[i][j])) {
                    sum += board[i][j]
                }
            }
        }
        return sum * called.last()
    }

    fun checkBoard(called: List<Int>, board: List<List<Int>>): Int {
        for (i in IntRange(0, 4)) {
            var rowOk = true
            for (j in IntRange(0, 4)) {
                if (!called.contains(board[i][j])) {
                    rowOk = false
                    break
                }
            }
            if (rowOk) {
                return countScore(called, board)
            }
        }

        for (j in IntRange(0, 4)) {
            var columnOk = true
            for (i in IntRange(0, 4)) {
                if (!called.contains(board[i][j])) {
                    columnOk = false
                    break
                }
            }
            if (columnOk) {
                return countScore(called, board)
            }
        }

//        var mainDiagOk = true
//        var otherDiagOk = true
//        for (i in IntRange(0, 4)) {
//            if (!called.contains(board[i][i])) {
//                mainDiagOk = false
//            }
//            if (!called.contains(board[i][4-i])) {
//                otherDiagOk = false
//            }
//        }
//        if (mainDiagOk || otherDiagOk) {
//            return countScore(called, board)
//        }

        return -1
    }

    val remainingBoards = boards.indices.toMutableSet()
    loop@ for (i in calls.indices) {
        val partCalls = calls.subList(0, i + 1)
        for (boardIx in remainingBoards.toList()) {
            val board = boards[boardIx]
            val result = checkBoard(partCalls, board)
            if (result > 0) {
                println("BINGO! board $boardIx score $result round $i")
                remainingBoards.remove(boardIx)
            }
        }
    }

}

fun day4_2021_a() {
    val input = input("d4_2021.txt")

    val calls: List<Int> = input.lines().first().split(',').map { it.toInt() }

    val boards: Set<List<List<Int>>> = input.lines().drop(1).filter { it.isNotBlank() }
        .chunked(5)
        .map { it.map { it.split(Pattern.compile("\\s+")).filter { it.isNotBlank() } }.map { it.map { it.toInt() } } }
        .toSet()

    println("")

    fun countScore(called: List<Int>, board: List<List<Int>>): Int {
        var sum = 0
        for (i in IntRange(0, 4)) {
            for (j in IntRange(0, 4)) {
                if (!called.contains(board[i][j])) {
                    sum += board[i][j]
                }
            }
        }
        return sum * called.last()
    }

    fun checkBoard(called: List<Int>, board: List<List<Int>>): Int {
        for (i in IntRange(0, 4)) {
            var rowOk = true
            for (j in IntRange(0, 4)) {
                if (!called.contains(board[i][j])) {
                    rowOk = false
                    break
                }
            }
            if (rowOk) {
                return countScore(called, board)
            }
        }

        for (j in IntRange(0, 4)) {
            var columnOk = true
            for (i in IntRange(0, 4)) {
                if (!called.contains(board[i][j])) {
                    columnOk = false
                    break
                }
            }
            if (columnOk) {
                return countScore(called, board)
            }
        }

        var mainDiagOk = true
        var otherDiagOk = true
        for (i in IntRange(0, 4)) {
            if (!called.contains(board[i][i])) {
                mainDiagOk = false
            }
            if (!called.contains(board[i][4 - i])) {
                otherDiagOk = false
            }
        }
        if (mainDiagOk || otherDiagOk) {
            return countScore(called, board)
        }

        return -1
    }

    loop@ for (i in calls.indices) {
        val partCalls = calls.subList(0, i + 1)
        for (board in boards) {
            val result = checkBoard(partCalls, board)
            if (result > 0) {
                println(result)
                break@loop
            }
        }
    }
}

fun day3_2021_b() {
    val input = input("d3_2021.txt")

    fun getbb(rem: List<String>): List<Int> {
        val pos: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        rem.map { it.toCharArray() }
            .forEach {
                it.toList().forEachIndexed { index, c ->
                    pos.set(
                        index, (pos.getOrNull(index) ?: 0) + when (c) {
                            '1' -> 1
                            else -> 0
                        }
                    )
                }
            }
        return pos
    }

    var remo2 = input.lines()
    var remco2 = input.lines()
    val reso2 = mutableListOf<String>()
    val resco2 = mutableListOf<String>()
    for (i in IntRange(0, 11)) {
        val bbo2 = getbb(remo2)
        val o2 = bbo2.joinToString("") { if (it * 2 >= remo2.size) "1" else "0" }
        remo2 = remo2.filter { it.substring(i, i + 1) == o2[i].toString() }
        reso2.addAll(remo2)

        val bbco2 = getbb(remco2)
        val co2 = bbco2.map { if (it * 2 >= remco2.size) "0" else "1" }
            .joinToString("")
        remco2 = remco2.filter { it.substring(i, i + 1) == co2[i].toString() }
        resco2.addAll(remco2)
    }

    println("${Integer.valueOf(reso2.last(), 2) * Integer.valueOf(resco2.last(), 2)}")
}

fun d5_2021_b() {
    val input = input("d5_2021.txt")


    data class Point(val x: Int, val y: Int)

    fun String.toPoint(): Point {
        val parts = trim().split(",")
        return Point(parts.first().toInt(), parts.last().toInt())
    }

    val lines = input.lines().map {
        val parts = it.trim().split(" -> ")
        parts.first().toPoint() to parts.last().toPoint()
    }

    val allPoints = mutableListOf<Point>()
    lines.forEach {
        val deltax = it.second.x - it.first.x
        val deltay = it.second.y - it.first.y

        if (deltax.absoluteValue == deltay.absoluteValue) {
            for (i in 0..deltax.absoluteValue) {
                val p = Point(it.first.x + i * deltax.sign, it.first.y + i * deltay.sign)
                allPoints.add(p)
            }
        } else if (deltax == 0 || deltay == 0) {

            if (deltax != 0) {
                for (i in IntProgression.fromClosedRange(0, deltax, deltax.sign)) {
                    val p = Point(it.first.x + i, it.first.y)
                    allPoints.add(p)
                }
            }
            if (deltay != 0) {
                for (i in IntProgression.fromClosedRange(0, deltay, deltay.sign)) {
                    val p = Point(it.first.x, it.first.y + i)
                    allPoints.add(p)
                }
            }

        }
    }

    val overlapPoint = allPoints.groupBy { it }.entries.filter { it.value.size > 1 }
        .size
    println("size of overlaps ${overlapPoint}")
}

fun d5_2021_a() {
    val input = input("d5_2021.txt")

    data class Point(val x: Int, val y: Int)

    fun String.toPoint(): Point {
        val parts = trim().split(",")
        return Point(parts.first().toInt(), parts.last().toInt())
    }

    val lines = input.lines().map {
        val parts = it.trim().split(" -> ")
        parts.first().toPoint() to parts.last().toPoint()
    }

    val hvlines = lines.filter { it.first.x == it.second.x || it.first.y == it.second.y }

    val allPoints = mutableListOf<Point>()
    hvlines.forEach {
        val deltax = it.second.x - it.first.x
        val deltay = it.second.y - it.first.y

        if (deltax == 0 && deltay == 0) {
            val p = Point(it.first.x, it.first.y)
            allPoints.add(p)
        }

        if (deltax != 0) {
            for (i in IntProgression.fromClosedRange(0, deltax, deltax.sign)) {
                val p = Point(it.first.x + i, it.first.y)
                allPoints.add(p)
            }
        }
        if (deltay != 0) {
            for (i in IntProgression.fromClosedRange(0, deltay, deltay.sign)) {
                val p = Point(it.first.x, it.first.y + i)
                allPoints.add(p)
            }
        }
    }

    val overlapPoint = allPoints.groupBy { it }.entries.filter { it.value.size > 1 }
        .size
    println("size of overlaps ${overlapPoint}")
}

fun d6_2021_b() { // 45 min late
    val input = input("d6_2021.txt")

    val state = input.split(',').filter { it.isNotBlank() }
        .map { it.trim().toInt() }
        .groupBy { it }.map { it.key to it.value.size.toLong() }.toMap()

    fun newState(state: Map<Int, Long>): Map<Int, Long> {
        val newState = mutableMapOf<Int, Long>()
        for (elem in state) {
            if (elem.key == 0) {
                newState[6] = elem.value + (newState[6] ?: 0)
                newState[8] = elem.value + (newState[8] ?: 0)
            } else {
                newState[elem.key - 1] = elem.value + (newState[elem.key - 1] ?: 0)
            }
        }
        return newState
    }

    fun stateSize(state: Map<Int, Long>) = state.map { it.value }.sum()

    var currentState = state
    println("Initial:  ${stateSize(currentState)} state $currentState")
    for (day in 1..256) {
        currentState = newState(currentState)
        println("After day $day: ${stateSize(currentState)} state $currentState")
    }
}

fun d6_2021_a() {
    val input = input("d6_2021.txt")

    val state = input.split(',').filter { it.isNotBlank() }.map { it.trim().toInt() }

    fun newState(state: List<Int>): List<Int> {
        val newState = mutableListOf<Int>()
        for (elem in state) {
            when (elem) {
                0 -> {
                    newState.add(8)
                    newState.add(6)
                }
                else -> {
                    newState.add(elem - 1)
                }
            }
        }
        return newState
    }

    println("Initial: ${state.size}")
    var currentState = state
    for (day in 1..80) {
        currentState = newState(currentState)
        println("After day $day: ${currentState.size}")
    }
}

fun d7_2021_a() {
    val input = input("d7_2021.txt")

    val pos = input.trim().split(',').filter { it.isNotBlank() }.map { it.toInt() }


    val avg = pos.sorted()[pos.size / 2]

    val fuelNeed = pos.map { (it - avg).absoluteValue }.sum()
    println("fuel needed: $fuelNeed")
}

fun d7_2021_b() { // 10 min late
    val input = input("d7_2021.txt")

    val pos = input.trim().split(',').filter { it.isNotBlank() }.map { it.toInt() }

    val min = pos.minOrNull()!!
    val max = pos.maxOrNull()!!

    fun fuel(start: Int, target: Int): Long {
        val diff = (target - start).absoluteValue
        return (diff * diff + diff) / 2L
    }

    println("0 1: ${fuel(0, 1)}, 3 2: ${fuel(3, 2)}")
    println("0 4: ${fuel(0, 4)}, 3 0: ${fuel(3, 0)}")

    var bestPosition: Int? = null
    var bestUsage = Long.MAX_VALUE
    for (i in min..max) {
        val usage = pos.sumOf { fuel(it, i) }
        if (usage < bestUsage) {
            bestPosition = i
            bestUsage = usage
        }
    }
    println("usage: $bestUsage, position: $bestPosition")
}

fun d8_2021_b() { // 40 min late
    val input = input("d8_2021.txt").lines()
        .map { line -> line.split('|').filter { it.isNotBlank() }.map { it.trim() } }
    val allPatterns: List<Set<Set<Char>>> = input.map { row ->
        row.first().split(' ').filter { it.isNotBlank() }.map { it.trim() }
            .map { it.toCharArray().toSet() }.toSet()
    }
    val allOutputs: List<List<Set<Char>>> = input.map { row ->
        row.last().split(' ').filter { it.isNotBlank() }.map { it.trim() }
            .map { it.toCharArray().toSet() }
    }

    var sum = 0
    for (i in allOutputs.indices) {
        val outputs = allOutputs[i]
        val patterns = allPatterns[i]


        val one = patterns.find { it.size == 2 }
        val four = patterns.find { it.size == 4 }
        val seven = patterns.find { it.size == 3 }
        val eight = patterns.find { it.size == 7 }

        val two = patterns.filter { it.size == 5 }.find { it.intersect(four!!).size == 2 }
        val five = patterns.filter { it.size == 5 }.filter { it != two }.find { it.intersect(one!!).size == 1 }
        val three = patterns.filter { it.size == 5 }.find { it != two && it != five }

        val six = patterns.filter { it.size == 6 }.find { it.intersect(one!!).size == 1 }
        val nine = patterns.filter { it.size == 6 }.filter { it != six }.find { it.intersect(four!!).size == 4 }
        val zero = patterns.filter { it.size == 6 }.find { it != six && it != nine }

        val number = outputs.map {
            when (it) {
                one -> "1"
                two -> "2"
                three -> "3"
                four -> "4"
                five -> "5"
                six -> "6"
                seven -> "7"
                eight -> "8"
                nine -> "9"
                zero -> "0"
                else -> "?"
            }
        }.joinToString("") { it }.toInt()
        sum += number
    }

    println("sum: $sum")
}

fun d9_2021_b() {
    val input = input("d9_2021.txt").lines()
        .map { it.toCharArray().map { char -> char.toString().toInt() } }

    fun isLowPoint(x: Int, y: Int): Boolean {
        val adj = setOf(
            x - 1 to y,
            x + 1 to y,
            x to y - 1,
            x to y + 1
        )
            .map { it to input.getOrNull(it.second)?.getOrNull(it.first) }
            .filterNot { it.second == null }
            .map { it.first to it.second!! }

        val point = input[y][x]

        return adj.all { it.second > point }
    }

    fun doBasin(x: Int, y: Int, basin: MutableSet<Pair<Int, Int>>) {
        val adj = setOf(
            x - 1 to y,
            x + 1 to y,
            x to y - 1,
            x to y + 1
        )
            .filterNot { basin.contains(it) }
            .map { it to input.getOrNull(it.second)?.getOrNull(it.first) }
            .filterNot { it.second == null }
            .map { it.first to it.second!! }

        val point = input[y][x]

        adj.filter { it.second > point && it.second != 9 }
            .map {
                basin.add(it.first)
                it
            }
            .forEach {
                doBasin(it.first.first, it.first.second, basin)
            }
    }

    val lowPoints = mutableListOf<Pair<Int, Int>>()
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (isLowPoint(x, y)) lowPoints.add(x to y)
        }
    }

    println("lowPoints $lowPoints")

    val basins: MutableList<Set<Pair<Int, Int>>> = mutableListOf()
    for (point in lowPoints) {
        val basin = mutableSetOf(point)
        doBasin(point.first, point.second, basin)
        basins.add(basin)
    }

    basins.sortedByDescending { it.size }
        .forEach { println(it.size) }

}

fun d17_2021_b() {
    val xbound = 102 to 157
    val ybound = -146 to -90

    class Probe(
        var vx: Int,
        var vy: Int
    ) {
        var x = 0
        var y = 0

        var maxy = 0

        fun step() {
            x += vx
            y += vy
            vx -= 1 * vx.sign
            vy -= 1
            maxy = if (y > maxy) {
                y
            } else {
                maxy
            }
        }

        fun overshooty(): Boolean {
            return y < ybound.first
        }

        fun intargety(): Boolean {
            return ybound.first <= y && y <= ybound.second
        }

        fun intarget(): Boolean {
            return intargety() && xbound.first <= x && x <= xbound.second
        }

        fun overshoot(): Boolean {
            return overshooty() || x > xbound.second
        }

    }

    val intarget = mutableListOf<Probe>()
    (-1500..1500).forEach { vy ->
        (1..1500).forEach { vx ->
            val p = Probe(vx, vy)
            while (!p.intarget() && !p.overshoot()) {
                p.step()
            }
            if (p.intarget()) {
                println("For vx=$vx vy=$vy intarget")
                intarget.add(p)
            }
        }

    }

    println(intarget.size)

}

fun d15_2021_b() {
    val input = input("d15_2021.txt")
    val initMap = input.lines().map { line -> line.toCharArray().map { it.digitToInt() } }

    fun valueInPos(size: Int = 100, pos: Pair<Int, Int>): Int {
        val times = pos.let { it.first / size to it.second / size }
        val mod = pos.let { it.first % size to it.second % size }
        val value = initMap[mod.second][mod.first]
        val toAdd = times.first + times.second
        return (value + toAdd - 1) % 9 + 1
    }

    val map = (0..499).map { y ->
        (0..499).map { x -> valueInPos(pos = x to y) }
    }

    fun neighbors(node: Pair<Int, Int>): List<Pair<Pair<Int, Int>, Int>> {
        return listOf(
            node.first - 1 to node.second,
            node.first + 1 to node.second,
            node.first to node.second + 1,
            node.first to node.second - 1
        )
            .map { it to map.getOrNull(it.second)?.getOrNull(it.first) }
            .filter { it.second != null }
            .map { it.first to it.second!! }
    }

    class QueueItem(val pos: Pair<Int, Int>, val dist: Int = -1) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as QueueItem

            if (pos != other.pos) return false

            return true
        }

        override fun hashCode(): Int {
            return pos.hashCode()
        }
    }

    fun dijkstra(target: Pair<Int, Int>) {
        val q = PriorityQueue<QueueItem>(Comparator.comparing { it.dist })
        val dist = mutableMapOf<Pair<Int, Int>, Int>()
        val prev = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>()
        map.indices.forEach { y ->
            map[y].indices.forEach { x ->
                dist[x to y] = Int.MAX_VALUE
                prev[x to y] = null
                q.add(QueueItem(x to y, Int.MAX_VALUE))
            }
        }
        dist[0 to 0] = 0
        q.remove(QueueItem(0 to 0))
        q.add(QueueItem(0 to 0, 0))

        while (q.isNotEmpty()) {
            val u = q.poll().pos

            if (u == target) {
                println(dist[u])
            }

            for (neighbor in neighbors(u)) {
                val alt = dist[u]!! + neighbor.second
                if (alt < dist[neighbor.first]!!) {
                    dist[neighbor.first] = alt
                    prev[neighbor.first] = u
                    q.remove(QueueItem(neighbor.first))
                    q.add(QueueItem(neighbor.first, alt))
                }
            }
        }
    }

    dijkstra(499 to 499)
}

fun d14_2021_b() {
    val input = input("d14_2021.txt")

    val start = input.lines().first().trim()

    val insertions = input.lines().drop(2).map { line ->
        val elements = line.split(" -> ").map { it.trim() }
        elements.first() to elements.last().toCharArray().first()
    }.toMap()

    var charCounts = start.toCharArray().groupBy { it }.map { it.key to it.value.size.toLong() }.toMap().toMutableMap()
    var pairCounts = start.toCharArray().toList().windowed(2, 1)
        .map { it.joinToString("") }.groupBy { it }.map { it.key to it.value.size.toLong() }.toMap().toMutableMap()

    fun step() {
        val pairs = pairCounts.keys
        val newPairCounts = pairCounts.toMutableMap()
        val newCharCounts = charCounts.toMutableMap()
        pairs.forEach { pair ->
            if (insertions.containsKey(pair)) {
                val count = pairCounts[pair]!!
                val ins = insertions[pair]!!
                val inss = ins.toString()
                newPairCounts[pair] = newPairCounts[pair]!! - count
                newPairCounts[pair.first() + inss] = (newPairCounts[pair.first() + inss] ?: 0) + count
                newPairCounts[inss + pair.last()] = (newPairCounts[inss + pair.last()] ?: 0) + count
                newCharCounts[ins] = (newCharCounts[ins] ?: 0) + count
            }
        }
        charCounts = newCharCounts
        pairCounts = newPairCounts
    }

    for (i in 1..40) {
        step()
        println("After step $i.")
    }

    val grouped = charCounts.toList().sortedBy { it.second }
    println(grouped)
    println(grouped.last().second - grouped.first().second)
}

fun d14_2021_a() {
    val input = input("d14_2021.txt")

    val start = input.lines().first().trim()

    val insertions = input.lines().drop(2).map { line ->
        val elements = line.split(" -> ").map { it.trim() }
        elements.first() to elements.last()
    }.toMap()


    fun step(template: String): String {
        var result = ""
        for (i in 0..template.length - 2) {
            val ins = insertions[template[i].toString() + template[i + 1]]
            if (ins != null) {
                result += template[i].toString() + ins
            } else {
                result += template[i]
            }
        }
        result += template[template.length - 1]
        return result
    }

    var currentTemplate = start
    for (i in 1..40) {
        currentTemplate = step(currentTemplate)
        println("After step $i")
    }

    val grouped = currentTemplate.toCharArray().groupBy { it }.map { it.key to it.value.size }
        .toList().sortedBy { it.second }

    println("First: ${grouped.first()}, Last: ${grouped.last()}")
}

fun d13_2021() {
    val input = input("d13_2021.txt")

    val dots = input.lines().asSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .filter { !it.startsWith("fold along ") }
        .map {
            val split = it.split(',')
            split.first().toInt() to split.last().toInt()
        }.toSet()

    val folds = input.lines().asSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .filter { it.startsWith("fold along ") }
        .map { it.substring(11) }
        .map {
            val split = it.split('=')
            split.first() to split.last().toInt()
        }.toList()

    println("begin")

    fun fold(dots: Set<Pair<Int, Int>>, fold: Pair<String, Int>): Set<Pair<Int, Int>> {
        val maxx = dots.map { it.first }.maxOrNull()!!
        val maxy = dots.map { it.first }.maxOrNull()!!

        val noChange = dots.filter {
            if (fold.first == "x") {
                it.first < fold.second
            } else if (fold.first == "y") {
                it.second < fold.second
            } else {
                throw IllegalArgumentException()
            }
        }.toSet()
        val folded = dots.filter {
            when (fold.first) {
                "x" -> {
                    it.first >= fold.second
                }
                "y" -> {
                    it.second >= fold.second
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }.map {
            when (fold.first) {
                "x" -> {
                    2 * fold.second - it.first to it.second
                }
                "y" -> {
                    it.first to 2 * fold.second - it.second
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }.toSet()
        return noChange + folded
    }

    println(fold(dots, folds.first()).size)

    var currentDots = dots
    folds.forEach {
        currentDots = fold(currentDots, it)
    }

    val display: MutableList<MutableList<String>> =
        IntRange(0, 20).map { IntRange(0, 100).map { "  " }.toMutableList() }.toMutableList()
    for (dot in currentDots) {
        display[dot.second][dot.first] = "##"
    }
    display.forEach {
        println(it.joinToString(""))
    }
}

fun d12_2021() {
    val input = input("d12_2021.txt")

    val connectionList = input.lines().map { line ->
        val split = line.trim().split("-")
        split.first() to split.last()
    }

    fun isSmall(cave: String): Boolean {
        return cave.first().isLowerCase()
    }

    fun getConnected(cave: String): Set<String> {
        return (connectionList.filter { it.first == cave }.map { it.second } +
            connectionList.filter { it.second == cave }.map { it.first }).toSet()
    }

    val pathes = mutableListOf<List<String>>()
    fun finderoo(current: List<String>, visited: List<String>, final: String) {
        val now = current.last()

        if (now == final) {
            pathes += current
            return
        }

        val newVisited = if (isSmall(now)) visited + now else visited

        for (cave in getConnected(now)) {
            var small = isSmall(cave)
            if (cave != "start" && (!small ||
                    small && !newVisited.contains(cave) ||
                    small && newVisited.size == newVisited.toSet().size)
            ) {
                finderoo(current + cave, newVisited, final)
            }
        }
    }

    finderoo(listOf("start"), listOf(), "end")
    println(pathes.size)
}

fun d11_2021_b() {
    val input = input("d11_2021.txt")

    val octopee = input.lines().map { line ->
        line.toCharArray().map { it.digitToInt() }.toMutableList()
    }.toMutableList()

    fun adj(pos: Pair<Int, Int>, octopee: MutableList<MutableList<Int>>, flashed: MutableSet<Pair<Int, Int>>) {
        val neighbors: Set<Pair<Int, Int>> = listOf(
            pos.first + 1 to pos.second,
            pos.first - 1 to pos.second,
            pos.first to pos.second + 1,
            pos.first to pos.second - 1,
            pos.first - 1 to pos.second + 1,
            pos.first - 1 to pos.second - 1,
            pos.first + 1 to pos.second + 1,
            pos.first + 1 to pos.second - 1
        )
            .filter { octopee.getOrNull(it.first)?.getOrNull(it.second) != null }
            .toSet()

        for (neighbor in neighbors) {
            if (!flashed.contains(neighbor)) {
                octopee[neighbor.first][neighbor.second] += 1
                if (octopee[neighbor.first][neighbor.second] > 9) {
                    flashed.add(neighbor)
                    adj(neighbor, octopee, flashed)
                }
            }
        }
    }

    fun step(step: Int, octopee: MutableList<MutableList<Int>>): Int {
        octopee.forEachIndexed { i, line ->
            line.indices.forEach { j ->
                octopee[i][j] += 1
            }
        }

        val flashed: MutableSet<Pair<Int, Int>> = mutableSetOf()
        octopee.forEachIndexed { i, line ->
            line.forEachIndexed { j, octo ->
                if (octo > 9) flashed.add(i to j)
            }
        }

        for (f in flashed.toList()) {
            adj(f, octopee, flashed)
        }

        octopee.forEachIndexed { i, line ->
            line.forEachIndexed { j, octo ->
                if (octo > 9) octopee[i][j] = 0
            }
        }

        println("Step $step flashes: ${flashed.size}")
        return flashed.size
    }

    println((1..100).sumOf { step(it, octopee) })

    var i = 1
    var res = 0
    while (res != 100) {
        res = step(i, octopee)
        i++
    }
}

fun d10_2021_b() {
    val input = input("d10_2021.txt")

    val braces = mapOf(
        '[' to ']',
        '(' to ')',
        '<' to '>',
        '{' to '}'
    )

    val scores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    val incompleteScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )
    var corruptedScoreSum = 0
    var incompleteScoreList = mutableListOf<Long>()

    input.lines().forEachIndexed line@{ lnum, line ->
        val stack = mutableListOf<Char>()
        line.toCharArray().forEach { char ->
            if (braces.keys.contains(char)) {
                stack.add(char)
            } else if (braces.values.contains(char)) {
                val last = stack.lastOrNull()
                if (last != null && braces[last] == char) {
                    stack.removeLast()
                } else {
                    val lineScore = scores[char]!!
                    corruptedScoreSum += lineScore
                    println("Syntax error at line $lnum: corrupted ($lineScore)")
                    return@line
                }
            } else {
                println("Syntax error at line $lnum: bad char")
                return@line
            }
        }
        if (stack.isNotEmpty()) {
            val completion = stack.map { braces[it]!! }.reversed()
            var lineScore = 0L
            completion.forEach {
                lineScore *= 5
                lineScore += incompleteScores[it]!!
            }
            incompleteScoreList.add(lineScore)
            println("Syntax error at line $lnum: incomplete ${completion.joinToString("")} ($lineScore)")
            return@line
        }
    }
    println("Corrupted score sum: $corruptedScoreSum")
    println("Middle incomplete score: ${incompleteScoreList.sorted()[incompleteScoreList.size / 2]}")
}