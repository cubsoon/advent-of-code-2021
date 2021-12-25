package io.github.cubsoon.aoc.aoc2021.untreated

import java.util.LinkedList
import kotlin.math.absoluteValue

/**
 * #############
 * #...........#
 * ###D#D#C#C###
 *   #D#C#B#A#
 *    #D#B#A#C#
 *   #B#A#B#A#
 *   #########
 */

data class Rooms(
    val hallway: List<Char> = "...........".toCharArray().toList(),
    val rooms: List<List<Char>> = listOf(
        listOf('D', 'D', 'D', 'B'),
        listOf('D', 'C', 'B', 'A'),
        listOf('C', 'B', 'A', 'B'),
        listOf('C', 'A', 'C', 'A')
    )
) {

    fun finish(): Boolean {
        return rooms[0].all { it == 'A' } && rooms[1].all { it == 'B' } &&
            rooms[2].all { it == 'C' } && rooms[3].all { it == 'D' }
    }

    fun moves(): List<Move> {
        val outmoves = rooms.indices.filter { room -> !rooms[room].all { it == '.' } }
            .map { it to hallwayDestinations(it) }.flatMap { pair -> pair.second.map { Move(true, pair.first, it) } }
        val inmoves = hallway.indices.filter { hw -> hallway[hw] != '.' }.map { it to roomDestinations(it) }
            .flatMap { pair -> pair.second.map { Move(false, it, pair.first) } }
        return inmoves + outmoves
    }

    fun makeMove(move: Move): Rooms {
        val makesMove = if (move.out) {
            rooms[move.room].first { it != '.' }
        } else {
            hallway[move.hallway]
        }
        val newInHw = if (move.out) makesMove else '.'
        val newInRoom = if (move.out) '.' else makesMove
        val newhallway = hallway.indices.map { if (move.hallway == it) newInHw else hallway[it] }
        val outpos = rooms[move.room].indexOfFirst { it != '.' }
        val inpos = rooms[move.room].indexOfLast { it == '.' }
        val pos = if (move.out) outpos else inpos
        val newRoom = rooms[move.room].indices.map {
            if (pos == it) {
                newInRoom
            } else rooms[move.room][it]
        }
        val newRooms = rooms.indices.map { if (move.room == it) newRoom else rooms[it] }
        return Rooms(newhallway, newRooms)
    }

    private fun hallwayDestinations(room: Int): List<Int> {
        val dest = listOf(0, 1, 3, 5, 7, 9, 10)
        val outhallway = room * 2 + 2
        val leftd = LinkedList(dest.filter { it < outhallway })
        val rightd = LinkedList(dest.filter { it > outhallway })

        val dests = mutableListOf<Int>()
        do {
            val current = leftd.pollLast()
            if (current != null && hallway[current] == '.') {
                dests.add(current)
            }
        } while (current != null && hallway[current] == '.')
        do {
            val current = rightd.pollFirst()
            if (current != null && hallway[current] == '.') {
                dests.add(current)
            }
        } while (current != null && hallway[current] == '.')
        return dests
    }

    private fun roomDestinations(hw: Int): List<Int> {
        val hwslots = listOf(0, 1, 3, 5, 7, 9, 10)
        val dests = mapOf(
            'A' to 0,
            'B' to 1,
            'C' to 2,
            'D' to 3
        )
        val room = dests[hallway[hw]]!!
        if (rooms[room].all { it == '.' || it == hallway[hw] }) {
            val inhw = room * 2 + 2
            if (inhw > hw) {
                if (hwslots.filter { it < inhw && it > hw }.all { hallway[it] == '.' }) {
                    return listOf(room)
                } else {
                    return emptyList()
                }
            } else {
                if (hwslots.filter { it > inhw && it < hw }.all { hallway[it] == '.' }) {
                    return listOf(room)
                } else {
                    return emptyList()
                }
            }
        }
        return emptyList()
    }


}

class Move(
    val out: Boolean,
    val room: Int,
    val hallway: Int
) {

    fun cost(rooms: Rooms): Int {
        val multiply = mapOf(
            'A' to 1,
            'B' to 10,
            'C' to 100,
            'D' to 1000
        )
        val makesMove = if (out) {
            rooms.rooms[room].first { it != '.' }
        } else {
            rooms.hallway[hallway]
        }

        val desthw = 2 + room * 2
        val left = (desthw - hallway).absoluteValue

        val destroom = if (out) {
            rooms.rooms[room].indexOfFirst { it != '.' }
        } else rooms.rooms[room].indexOfLast { it == '.' }
        val up = destroom + 1
        return (left + up) * multiply[makesMove]!!
    }
}

fun main() {
    var bestSolution = Long.MAX_VALUE
    val rooms = LinkedList(listOf(Rooms()))
    val bestscore = mutableMapOf(rooms.first() to 0L)

    var r: Rooms?
    do {
        r = rooms.pollFirst()
        if (r != null) {
            val score = bestscore[r]!!
            if (r.finish()) {
                if (score < bestSolution) {
                    bestSolution = score
                }
            } else {
                r.moves().forEach {
                    val newroom = r.makeMove(it)
                    val newscore = score + it.cost(r)
                    if ((bestscore[newroom] ?: Long.MAX_VALUE) > newscore) {
                        rooms.add(newroom)
                        bestscore[newroom] = newscore
                    }
                }
            }
        }
    } while (r != null)

    println(bestSolution)
}