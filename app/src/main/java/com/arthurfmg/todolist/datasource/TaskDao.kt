package com.arthurfmg.todolist.datasource

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arthurfmg.todolist.model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(vararg task: Task)

    @Query("SELECT * FROM task ORDER BY title ASC")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id IN (:taskId)")
    fun findById(taskId: Int): Task

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}