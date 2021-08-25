package com.arthurfmg.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.room.Room
import com.arthurfmg.todolist.databinding.ActivityMainBinding
import com.arthurfmg.todolist.datasource.TaskDao
import com.arthurfmg.todolist.datasource.TaskDataSource
import com.arthurfmg.todolist.datasource.TaskDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = TaskDatabase.getDatabase(this)
        val dao = db.taskDao()

        binding.rvTasks.adapter = adapter
        updateList(dao)

        insertListeners(dao)
    }

    private fun insertListeners(database: TaskDao) {
        binding.fabAddTask.setOnClickListener {
            startActivityForResult(Intent(this,AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            database.deleteTask(it)
            updateList(database)
        }
    }

    private fun updateList(database: TaskDao){
        val getAll = database.getAll()
        binding.includeEmpty.emptyState.visibility = if(getAll.isEmpty()) View.VISIBLE
        else View.GONE

        adapter.notifyDataSetChanged()
        adapter.submitList(getAll)
    }

    companion object{
        private const val CREATE_NEW_TASK = 1000
    }
}