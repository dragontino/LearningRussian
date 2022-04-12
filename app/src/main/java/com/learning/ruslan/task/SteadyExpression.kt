package com.learning.ruslan.task

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "ExpressionTable")
class SteadyExpression(word: String, position: Int, id: Int): Task(word, position, id) {

    @Ignore
    constructor(phrase: String): this(phrase, -1, 0)
}