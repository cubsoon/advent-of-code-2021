package io.github.cubsoon.aoc.aoc2021.untreated

import io.github.cubsoon.aoc.input
import java.util.LinkedList
import java.util.Queue

fun nextBits(l: Int, bits: Queue<Char>): String {
    return (1..l).map { bits.remove() }.joinToString("")
}

sealed class Packet(
    val version: Int,
    val type: Int
) {
    abstract fun getValue(): Long
    abstract fun allPackets(): List<Packet>

    companion object {
        fun parse(bits: Queue<Char>): Packet {
            val version = nextBits(3, bits).toInt(2)
            val type = nextBits(3, bits).toInt(2)
            return when (type) {
                4 -> LiteralPacket.parse(version, type, bits)
                else -> OperatorPacket.parse(version, type, bits)
            }
        }
    }
}

class LiteralPacket(version: Int, type: Int, val v: Long) : Packet(version, type) {
    override fun getValue(): Long = v
    override fun allPackets(): List<Packet> = listOf(this)

    companion object {
        fun parse(version: Int, type: Int, bits: Queue<Char>): LiteralPacket {
            var valueBits = ""
            do {
                val group = nextBits(5, bits)
                valueBits += group.drop(1)
            } while (group.first() == '1')
            return LiteralPacket(version, type, valueBits.toLong(2))
        }
    }
}

class OperatorPacket(version: Int, type: Int, val lengthType: Int, val length: Int, val packets: List<Packet>) :
    Packet(version, type) {
    override fun getValue(): Long {
        return when (type) {
            0 -> packets.sumOf { it.getValue() }
            1 -> packets.map { it.getValue() }.fold(1L) { acc, v -> acc * v }
            2 -> packets.minOf { it.getValue() }
            3 -> packets.maxOf { it.getValue() }
            5 -> if (packets[0].getValue() > packets[1].getValue()) {
                1L
            } else {
                0L
            }
            6 -> if (packets[0].getValue() < packets[1].getValue()) {
                1L
            } else {
                0L
            }
            7 -> if (packets[0].getValue() == packets[1].getValue()) {
                1L
            } else {
                0L
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun allPackets(): List<Packet> = listOf(this) + packets.flatMap { it.allPackets() }

    companion object {
        fun parse(version: Int, type: Int, bits: Queue<Char>): OperatorPacket {
            val lengthType = nextBits(1, bits).toInt(2)
            val length = (if (lengthType == 0) nextBits(15, bits) else nextBits(11, bits)).toInt(2)

            return if (lengthType == 0) {
                val subbits: Queue<Char> = LinkedList(nextBits(length, bits).toCharArray().toList())
                val packets = mutableListOf<Packet>()
                while (subbits.isNotEmpty()) {
                    packets.add(parse(subbits))
                }
                OperatorPacket(version, type, lengthType, length, packets)
            } else {
                val packets = (1..length).map { parse(bits) }
                OperatorPacket(version, type, lengthType, length, packets)
            }
        }
    }
}

fun main() {
    val input = input("d16_2021.txt")
    val bits = input.toCharArray().joinToString("") {
        it.digitToInt(16).toString(2).padStart(4, '0')
    }

    println(bits)

    val bitsQueue = LinkedList(bits.toCharArray().toList())
    val packet = Packet.parse(bitsQueue)

    println(packet.allPackets().sumOf { it.version })
    println(packet.getValue())
}
