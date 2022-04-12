package com.learning.ruslan.task

import androidx.room.Entity

@Entity(tableName = "AssentTable")
class Assent(word: String, position: Int, var isChecked: Boolean = false, id: Int = 0) :
    Task(word, position, id)