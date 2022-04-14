package com.learning.ruslan.task

class TaskRepository(private val taskDao: TaskDao) {

    fun addTask(task: Task) = taskDao.run {
        when (task) {
            is Assent -> addAssent(task)
            is Suffix -> addSuffix(task)
            is Paronym -> addParonym(task)
            is SteadyExpression -> addExpression(task)
            else -> Unit
        }
    }

    private fun getTaskList(taskType: TaskType) = taskDao.run {
        when (taskType) {
            TaskType.Assent -> getAssentList()
            TaskType.Suffix -> getSuffixList()
            TaskType.Paronym -> getParonymList()
            TaskType.SteadyExpression -> getExpressionList()
        }
    }

    fun isTableEmpty(taskType: TaskType) =
        getTaskList(taskType).isEmpty()



    fun getTask(taskType: TaskType, position: Int) = taskDao.run {
        when (taskType) {
            TaskType.Assent -> getAssent(position)
            TaskType.Suffix -> getSuffix(position)
            TaskType.Paronym -> getParonym(position)
            TaskType.SteadyExpression -> getExpression(position)
        }
    }

    fun getTask(taskType: TaskType, word: String) = taskDao.run {
        when (taskType) {
            TaskType.Assent -> getAssent(word)
            TaskType.Suffix -> getSuffix(word)
            TaskType.Paronym -> getParonym(word)
            TaskType.SteadyExpression -> getExpression(word)
        }
    }

    fun searchTask(taskType: TaskType, query: String?) = taskDao.run {
        if (query == null || query.isBlank())
            return@run getTaskList(taskType)

        when (taskType) {
            TaskType.Assent -> searchAssent(query)
            TaskType.Suffix -> searchSuffix(query)
            TaskType.Paronym -> searchParonym(query)
            TaskType.SteadyExpression -> searchExpression(query)
        }
    }


    fun updateAssent(position: Int, isChecked: Boolean) =
        taskDao.updateAssent(position, isChecked)

    fun getNonCheckedAssents() =
        taskDao.getNonCheckedAssents()
}