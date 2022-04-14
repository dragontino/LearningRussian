package com.learning.ruslan

class Pair<X, Y>(var first: X, var second: Y) {

    operator fun set(first: X, second: Y) {
        this.first = first
        this.second = second
    }
}

operator fun <T>Pair<T, T>.get(index: Int) =
    if (index == 0) first
    else second


fun <A, B: Iterable<A>> Pair<A, B>.toList(): List<A> {
    val result: ArrayList<A> = arrayListOf(first)
    result.addAll(second)
    return result
}