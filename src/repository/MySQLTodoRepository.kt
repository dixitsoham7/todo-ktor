package com.soham.todo.repository

import com.soham.todo.database.DatabaseManager
import com.soham.todo.entities.ToDo
import com.soham.todo.entities.ToDoDraft

class MySQLTodoRepository : ToDoRepository {
    private val database = DatabaseManager()

    override fun getAllToDos(): List<ToDo> {
        return database.getAllToDos()
            .map { ToDo(it.id, it.title, it.done) }
    }

    override fun getSingleToDo(id: Int): ToDo? {
        return database.getSingleToDo(id)
            ?.let { ToDo(it.id, it.title, it.done) }
    }

    override fun addToDo(draft: ToDoDraft): ToDo {
        return database.addToDo(draft)
    }

    override fun removeToDo(id: Int): Boolean {
        return database.removeToDo(id)
    }

    override fun updateToDo(id: Int, draft: ToDoDraft): Boolean {
        return database.updateToDo(id, draft)
    }
}