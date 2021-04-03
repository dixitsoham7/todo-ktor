package com.soham.todo


import com.soham.todo.entities.ToDoDraft
import com.soham.todo.repository.InMemoryToDoRepository
import com.soham.todo.repository.MySQLTodoRepository
import com.soham.todo.repository.ToDoRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf

fun Application.module() {
    install(CallLogging)
    install(ContentNegotiation){
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/health") {
            call.respondText("todoList application is up and running!!")
        }

        route("/todos") {
            val repository: ToDoRepository = MySQLTodoRepository()

            get {
                call.respond(repository.getAllToDos())
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if(id==null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "id param has to be a number"
                    )
                    return@get //execution stops
                }
                val todo = repository.getSingleToDo(id)
                if(todo == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "todo with id $id not found"
                    )
                } else {
                    call.respond(todo)
                }
            }

            post {
                val toDoDraft = call.receive<ToDoDraft>()
                val todo = repository.addToDo(toDoDraft)
                call.respond(todo)
            }

            put("/{id}") {
                val toDoIdUpdate = call.parameters["id"]?.toIntOrNull()
                val toDoDraftUpdate = call.receive<ToDoDraft>()
                if(toDoIdUpdate == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "id param has to be a number"
                    )
                    return@put
                }
                val updateToDo = repository.updateToDo(toDoIdUpdate,toDoDraftUpdate)
                if(updateToDo){
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "found no todo with todoId: $toDoIdUpdate"
                    )
                }
            }

            delete("/{id}") {
                val toDoIdDelete = call.parameters["id"]?.toIntOrNull()
                if(toDoIdDelete == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "id param has to be a number"
                    )
                    return@delete
                }
                val removeToDo = repository.removeToDo(toDoIdDelete)
                if(removeToDo){
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "found no todo to remove with todoId: $toDoIdDelete"
                    )
                }
            }
        }

    }
}

