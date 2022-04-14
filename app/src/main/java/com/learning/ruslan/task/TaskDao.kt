package com.learning.ruslan.task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {


    /**
     *
     * Добавления
     *
     */
    @Insert(entity = Assent::class, onConflict = OnConflictStrategy.REPLACE)
    fun addAssent(assent: Assent)

    @Insert(entity = Suffix::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSuffix(suffix: Suffix)

    @Insert(entity = Paronym::class, onConflict = OnConflictStrategy.REPLACE)
    fun addParonym(paronym: Paronym)

    @Insert(entity = SteadyExpression::class, onConflict = OnConflictStrategy.REPLACE)
    fun addExpression(steadyExpression: SteadyExpression)


    /**
     *
     * Получение всех элементов базы
     *
     */
    @Query("SELECT * FROM AssentTable ORDER BY word ASC")
    fun getAssentList(): List<Assent>

    @Query("SELECT * FROM SuffixTable ORDER BY word ASC")
    fun getSuffixList(): List<Suffix>

    @Query("SELECT * FROM ParonymTable ORDER BY word ASC")
    fun getParonymList(): List<Paronym>

    @Query("SELECT * FROM ExpressionTable ORDER BY word ASC")
    fun getExpressionList(): List<SteadyExpression>


    /**
     *
     * Получение одного элемента базы
     *
     */
    @Query("SELECT * FROM AssentTable WHERE id = :position + 1")
    fun getAssent(position: Int): Assent

    @Query("SELECT * FROM SuffixTable WHERE id = :position + 1")
    fun getSuffix(position: Int): Suffix

    @Query("SELECT * FROM ParonymTable WHERE id = :position + 1")
    fun getParonym(position: Int): Paronym

    @Query("SELECT * FROM ExpressionTable WHERE id = :position + 1")
    fun getExpression(position: Int): SteadyExpression

    @Query("SELECT * FROM AssentTable WHERE word = :word")
    fun getAssent(word: String): Assent

    @Query("SELECT * FROM SuffixTable WHERE word = :word")
    fun getSuffix(word: String): Suffix

    @Query("SELECT * FROM ParonymTable WHERE word = :word")
    fun getParonym(word: String): Paronym

    @Query("SELECT * FROM ExpressionTable WHERE word = :word")
    fun getExpression(word: String): SteadyExpression


    /**
     *
     * количество элэментов в базе
     *
     */
    @Query("SELECT COUNT(word) FROM AssentTable")
    fun getAssentCount(): Int

    @Query("SELECT COUNT(word) FROM SuffixTable")
    fun getSuffixCount(): Int

    @Query("SELECT COUNT(word) FROM ParonymTable")
    fun getParonymCount(): Int

    @Query("SELECT COUNT(word) FROM ExpressionTable")
    fun getExpressionCount(): Int


    @Query("SELECT COUNT(word) FROM AssentTable WHERE isChecked = :isChecked")
    fun getAssentCount(isChecked: Boolean): Int


    /**
     *
     * обновление assents
     *
     */
    @Query("SELECT * FROM AssentTable WHERE isChecked = 0")
    fun getNonCheckedAssents(): List<Assent>

    @Query("UPDATE AssentTable SET isChecked = :isChecked WHERE id = :position")
    fun updateAssent(position: Int, isChecked: Boolean)


    /**
     *
     * Поиск
     *
     */
    @Query("SELECT * FROM AssentTable WHERE word LIKE '%' || :query || '%'")
    fun searchAssent(query: String): MutableList<Assent>

    @Query("SELECT * FROM SuffixTable WHERE word LIKE '%' || :query || '%'")
    fun searchSuffix(query: String): MutableList<Suffix>

    @Query("SELECT * FROM ParonymTable WHERE word LIKE '%' || :query || '%'")
    fun searchParonym(query: String): MutableList<Paronym>

    @Query("SELECT * FROM ExpressionTable WHERE word LIKE '%' || :query || '%'")
    fun searchExpression(query: String): MutableList<SteadyExpression>

//    open fun search()
}