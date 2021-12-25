package io.github.cubsoon.aoc.aoc2021.untreated

import io.github.cubsoon.aoc.input

fun main() {
    val input = input("d25_2021.txt")
    val map = input.lines().map { it.trim() }.map { it.toCharArray().toList() }
    val ysize = map.size
    val xsize = map.first().size

    fun step(i: Int, m: List<List<Char>>): List<List<Char>> {
        val indices = m.indices.flatMap { y -> m[y].indices.map { x -> x to y } }
        val stepsRight = indices.filter { m[it.second][it.first] == '>' }
            .filter { m[it.second][(it.first + 1) % xsize] == '.' }.toSet()
        val m1 = m.mapIndexed { y, row ->
            row.mapIndexed { x, item ->
                if (item == '>' && stepsRight.contains(x to y)) '.'
                else if (item == '.' && stepsRight.contains((x - 1 + xsize) % xsize to y)) '>'
                else item
            }
        }

        val stepsDown = indices.filter { m1[it.second][it.first] == 'v' }
            .filter { m1[(it.second + 1) % ysize][it.first] == '.' }.toSet()

        if (stepsRight.isEmpty() && stepsDown.isEmpty()) {
            println("no change: $i")
        }

        return m1.mapIndexed { y, row ->
            row.mapIndexed { x, item ->
                if (item == 'v' && stepsDown.contains(x to y)) '.'
                else if (item == '.' && stepsDown.contains(x to (y - 1 + ysize) % ysize)) 'v'
                else item
            }
        }
    }

    fun print(i: Int, m: List<List<Char>>) {
        println(" === Step $i ===")
        m.forEach { row -> println(row.joinToString("")) }
    }

    var current = map
    //print(0, current)
    for (i in 1..1000) {
        current = step(i, current)
        //print(i, current)
    }
}