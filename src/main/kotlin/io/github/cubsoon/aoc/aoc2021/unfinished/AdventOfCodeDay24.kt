package io.github.cubsoon.aoc.aoc2021.unfinished

import io.github.cubsoon.aoc.input

class ALU(
    val instructions: List<Instruction>,
    private var input: List<Int> = emptyList(),
    var variables: Variables = Variables()
) {
    var inputPos = 0

    fun nextInput(): Int {
        val next = input[inputPos]
        inputPos++
        return next
    }

    fun input(input: List<Int>) {
        this.input = input
        this.inputPos = 0
    }

    fun execute() {
        variables = Variables()
        instructions.forEach {
            it.apply(this)
        }
    }
}

data class Variables(
    var w: Int = 0,
    var x: Int = 0,
    var y: Int = 0,
    var z: Int = 0
)

class Instruction(
    val instr: String,
    val args: List<Argument>
) {
    companion object {
        fun fromString(string: String): Instruction {
            val parts = string.trim().split(" ")
            val instr = parts[0]
            val args = parts.drop(1).map { Argument.fromString(it) }
            return Instruction(instr, args)
        }
    }

    fun apply(alu: ALU) {
        //println("$instr $args")
        when(instr) {
            "inp" -> setTarget(alu.nextInput(), alu)
            "add" -> setTarget(args[0].getValue(alu.variables) + args[1].getValue(alu.variables), alu)
            "mul" -> setTarget(args[0].getValue(alu.variables) * args[1].getValue(alu.variables), alu)
            "div" -> setTarget(args[0].getValue(alu.variables) / args[1].getValue(alu.variables), alu)
            "mod" -> setTarget(args[0].getValue(alu.variables) % args[1].getValue(alu.variables), alu)
            "eql" -> setTarget(if (args[0].getValue(alu.variables) == args[1].getValue(alu.variables)) 1 else 0, alu)
            else -> throw IllegalArgumentException()
        }
    }

    private fun setTarget(value: Int, alu: ALU) {
        val target: VariableArgument = args[0] as VariableArgument
        //println("    set $target $value")
        when (target.variable) {
            "w" -> alu.variables.w = value
            "x" -> alu.variables.x = value
            "y" -> alu.variables.y = value
            "z" -> alu.variables.z = value
            else -> throw IllegalArgumentException()
        }
        //println("    ${alu.variables}")
    }
}

sealed interface Argument {
    companion object {
        fun fromString(string: String): Argument {
            return string.toIntOrNull()?.let { LiteralArgument(it) } ?: VariableArgument(string)
        }
    }
    fun getValue(variables: Variables): Int
}

class VariableArgument(val variable: String) : Argument {
    override fun getValue(variables: Variables): Int {
        return when (variable) {
            "w" -> variables.w
            "x" -> variables.x
            "y" -> variables.y
            "z" -> variables.z
            else -> throw IllegalArgumentException()
        }
    }

    override fun toString(): String {
        return variable
    }
}

class LiteralArgument(val value: Int) : Argument {
    override fun getValue(variables: Variables): Int {
        return value
    }

    override fun toString(): String {
        return value.toString()
    }
}

fun main() {
    val alu = ALU(
        input("d24_2021.txt").lines().map { Instruction.fromString(it) }
    )

    fun half(a1: Long, a2: Long): Long {
        var middle = (a1 + a2)/2
        return middle.toString().toCharArray().map { if (it == '0') '1' else it }.joinToString("").toLong()

    }

    fun toInput(long: Long): List<Int> {
        return long.toString().toCharArray().map { it.digitToInt() }
    }

//    var low = 99463851316171L
//    var high = 99999999999999L
//
//    alu.input(toInput(half(low, high)))
//    alu.execute()
//    while (alu.variables.z != 0) {
//        val half = half(low, high)
//        if (alu.variables.z < 0) {
//            low = half
//        } else {
//            high = half
//        }
//        alu.input(toInput(half(low, high)))
//        alu.execute()
//        println("${half(low, high)} z: ${alu.variables.z}")
//    }

    for (i in 99991528661654L downTo 8888888888888L) {
        alu.input(toInput(i))
        alu.execute()
        if (alu.variables.z == 0) {
            println("$i - ${alu.variables.z}")
            break
        }
    }


}

