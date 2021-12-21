package io.github.cubsoon.aoc.aoc2021.untreated

data class State(
    val scores: List<Int>,
    val positions: List<Int>,
    val nextPlayer: Int
) {
    fun isFinal(): Boolean {
        return scores.any { it >= 21 }
    }

    fun won(): Int {
        return scores.indexOfFirst { it >= 21 }
    }
}

fun main() {
    val states = mutableMapOf(State(listOf(0, 0), listOf(9, 3), 0) to 1L)
    val wons = mutableListOf(0L, 0L)

    fun throws(): List<List<Int>> {
        return (1..3).flatMap { a ->
            (1..3).flatMap { b ->
                (1..3).map { c ->
                    listOf(a, b, c)
                }
            }
        }
    }

    fun round(state: State) {
        val count = states[state]!!
        states.remove(state)

        val results: MutableList<State> = mutableListOf()
        throws().map { throws ->
            val roll = throws.sum()
            val pix = state.nextPlayer
            val newPositions = state.positions.toMutableList()
            newPositions[pix] = (state.positions[pix] + roll - 1) % 10 + 1
            val newScores = state.scores.toMutableList()
            newScores[pix] = state.scores[pix] + newPositions[pix]
            results.add(State(newScores, newPositions, (pix + 1) % 2))
        }
        results.groupBy { it }.mapValues { it.value.size }.forEach { (s, c) ->
            if (s.isFinal()) {
                wons[s.won()] += count * c
            } else {
                val currentCount = states.getOrDefault(s, 0L)
                states[s] = currentCount + count * c
            }
        }
    }

    var currentState = states.keys.firstOrNull()
    while (currentState != null) {
        round(currentState)
        currentState = states.keys.firstOrNull()
    }
    println(wons)
}