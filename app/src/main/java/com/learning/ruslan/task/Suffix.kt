package com.learning.ruslan.task

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "SuffixTable")
class Suffix(id: Int, word: String, position: Int, var alternative: String) :
    Task(word, position, id) {

    @Ignore
    constructor(word: String, position: Int, alternative_letter: String) :
            this(
                id = 0,
                word = word,
                position = position,
                alternative = alternative_letter
            )
}