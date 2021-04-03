package com.soham.todo.repository

import com.soham.todo.entities.ToDo
import com.soham.todo.entities.ToDoDraft

interface ToDoRepository {
    fun getAllToDos(): List<ToDo>

    fun getSingleToDo(id: Int): ToDo?

    fun addToDo(draft: ToDoDraft): ToDo

    fun removeToDo(id: Int): Boolean

    fun updateToDo(id: Int, draft: ToDoDraft): Boolean
}