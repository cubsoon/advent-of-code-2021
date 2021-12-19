package io.github.cubsoon.aoc.aoc2021.untreated

import io.github.cubsoon.aoc.aoc2021.untreated.Snailnum.Companion.parse
import io.github.cubsoon.aoc.input

sealed class Snailnum {

    lateinit var parent: SnailnumPair

    companion object {
        fun parse(text: String): Snailnum {
            if (text.startsWith('[')) {
                var level = 0
                var comma: Int? = null
                text.toCharArray().forEachIndexed { i, char ->
                    when (char) {
                        '[' -> level += 1
                        ']' -> level -= 1
                        ',' -> if (level == 1) comma = i
                    }
                }
                val sub1 = text.subSequence(1, comma!!).toString()
                val sub2 = text.subSequence(comma!! + 1, text.length - 1).toString()
                return SnailnumPair(parse(sub1), parse(sub2))
            } else if (text.first().isDigit()) {
                return SnailnumLiteral(text.toInt())
            }
            throw IllegalArgumentException()
        }
    }

    abstract fun all(level: Int = 0): List<Pair<Snailnum, Int>>

    fun add(sn: Snailnum): Snailnum = SnailnumPair(this, sn)

    abstract fun value(): Int

    abstract fun magnitude(): Long

    abstract fun repr(): String

    fun reduce() {
        while (explode() || split()) {
            // this loop is intentionally left blank
        }
    }

    private fun explode(): Boolean {
        val tree = all()
        val toExplode = tree
            .filter { it.second == 4 }
            .map { it.first }
            .filterIsInstance<SnailnumPair>()
            .firstOrNull() ?: return false

        val index = tree.indexOfFirst {
            it.first == toExplode
        }
        val leftLiteral = tree.subList(0, index)
            .map { it.first }
            .filter { it != toExplode.left && it != toExplode.right }
            .filterIsInstance<SnailnumLiteral>()
            .lastOrNull()
        val rightLiteral = tree.subList(index + 1, tree.size)
            .map { it.first }
            .filter { it != toExplode.left && it != toExplode.right }
            .filterIsInstance<SnailnumLiteral>()
            .firstOrNull()
        if (leftLiteral != null) {
            leftLiteral.v += toExplode.left.value()
        }
        if (rightLiteral != null) {
            rightLiteral.v += toExplode.right.value()
        }
        if (toExplode.parent.left == toExplode) {
            toExplode.parent.left = SnailnumLiteral(0)
        } else {
            toExplode.parent.right = SnailnumLiteral(0)
        }
        return true
    }

    private fun split(): Boolean {
        val tree = all()
        val toSplit = tree.map { it.first }
            .filterIsInstance<SnailnumLiteral>()
            .firstOrNull { it.value() >= 10 } ?: return false
        if (toSplit.parent.left == toSplit) {
            toSplit.parent.left = SnailnumPair(
                SnailnumLiteral(toSplit.value() / 2), SnailnumLiteral((toSplit.value() + 1) / 2)
            )
        } else {
            toSplit.parent.right = SnailnumPair(
                SnailnumLiteral(toSplit.value() / 2), SnailnumLiteral((toSplit.value() + 1) / 2)
            )
        }
        return true
    }

}

class SnailnumLiteral(var v: Int) : Snailnum() {
    override fun all(level: Int): List<Pair<Snailnum, Int>> {
        return listOf(this to level)
    }

    override fun value(): Int {
        return v
    }

    override fun magnitude(): Long {
        return v.toLong()
    }

    override fun repr(): String {
        return v.toString()
    }
}

class SnailnumPair(var left: Snailnum, var right: Snailnum) : Snailnum() {
    override fun all(level: Int): List<Pair<Snailnum, Int>> {
        left.parent = this
        right.parent = this
        return left.all(level + 1) + listOf(this to level) + right.all(level + 1)
    }

    override fun value(): Int {
        TODO("Not yet implemented")
    }

    override fun magnitude(): Long {
        return 3 * left.magnitude() + 2 * right.magnitude()
    }

    override fun repr(): String {
        return "[${left.repr()},${right.repr()}]"
    }
}

fun main() {
    val input = input("d18_2021.txt")
    var sum = parse(input.lines().first().trim())
    input.lines().drop(1).map { it.trim() }.forEach {
        val toAdd = parse(it)
        sum = sum.add(toAdd)
        sum.reduce()
    }
    println(sum.magnitude())

    val magnitudes: MutableSet<Triple<String, String, Long>> = mutableSetOf()
    input.lines().map { it.trim() }.flatMap { x ->
        input.lines().map { it.trim() }.map { x to it }
    }.forEach {
        val second = parse(it.second)
        val sum = parse(it.first).add(second)
        sum.reduce()
        magnitudes.add(Triple(it.first, it.second, sum.magnitude()))
    }
    println(magnitudes.sortedByDescending { it.third })
}