package com.learning.ruslan.task

import androidx.room.Entity
import androidx.room.Ignore
import java.util.*

@Entity(tableName = "ParonymTable")
class Paronym(id: Int, word: String, var variants: String, var alternatives: String) : Task(
    word,
    -1,
    id
) {

    companion object {
        private const val split = "|"
    }

    @Ignore
    constructor(word: String, variants: String, alternativeWords: String) : this(
        id = 0,
        word = word,
        variants = variants,
        alternatives = alternativeWords
    )

    @Ignore
    constructor(params: Array<String>) : this(params[0], params[1], params[2])

    val arrayVariants get() = variants.split(split)

    private val arrayAlternatives get() = alternatives.split(split)


    fun getVariant(position: Int) = arrayVariants[position]

    val randomVariant get() = getVariant(Random().nextInt(arrayVariants.size))

    fun getRandomAlternatives(count: Int) : List<String> {
        val list = arrayAlternatives.shuffled()

        return if (list.size <= count) list
        else list.subList(0, count)
    }
}