package com.learning.ruslan.task

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Task(
    var word: String,
    var position: Int = 0,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
)