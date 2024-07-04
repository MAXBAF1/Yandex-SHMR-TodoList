package com.around_team.todolist.data.network.repositories

import android.util.Log
import com.around_team.todolist.data.db.DatabaseRepository
import com.around_team.todolist.data.network.RequestManager
import com.around_team.todolist.data.network.TodoListConfig
import com.around_team.todolist.data.network.model.receive.GetAllItemsReceive
import com.around_team.todolist.data.network.model.receive.GetItemReceive
import com.around_team.todolist.data.network.model.request.PostItemRequest
import com.around_team.todolist.data.network.model.request.SendAllItemsRequest
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.utils.MyErrorIdException
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val databaseRepository: DatabaseRepository) {
    private val todos = MutableStateFlow(listOf<TodoItem>())
    private val errors = MutableSharedFlow<Int>(replay = 2)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is MyErrorIdException -> errors.tryEmit(throwable.errorId)
            else -> Log.e("MyLog", throwable.stackTraceToString())
        }
    }

    private val job = SupervisorJob()
    private val repositoryScope = CoroutineScope(Dispatchers.IO + job + exceptionHandler)

    fun getTodos(): StateFlow<List<TodoItem>> = todos
    fun getErrors(): Flow<Int> = errors

    fun getAllTodosFromBD() {
        repositoryScope.launch {
            todos.update { databaseRepository.getAllTodos() }
        }
    }

    fun refreshAllTodos(onSuccess: () -> Unit = {}) {
        repositoryScope.launch {
            val todosDTO = RequestManager.createRequest<GetAllItemsReceive>(
                methodType = HttpMethod.Get,
                address = TodoListConfig.LIST_ADDRESS.toString(),
            )?.list ?: return@launch
            val todoItems = todosDTO.map { it.toTodoItem() }
            databaseRepository.replaceTodos(todoItems)
            todos.update { todoItems }
            onSuccess()
        }
    }

    fun sendAllTodos(list: List<TodoItem>) {
        repositoryScope.launch {
            val todosDTO = RequestManager.createRequest<GetAllItemsReceive>(
                methodType = HttpMethod.Patch,
                address = TodoListConfig.LIST_ADDRESS.toString(),
                body = SendAllItemsRequest(list.map { it.toDTO() })
            )?.list ?: return@launch
            val todoItems = todosDTO.map { it.toTodoItem() }
            databaseRepository.replaceTodos(todoItems)
            todos.update { todoItems }
        }
    }

    fun saveTodo(todoItem: TodoItem) {
        repositoryScope.launch {
            databaseRepository.insertTodo(todoItem)
            todos.update { list ->
                val newList = list.toMutableList()
                newList.add(todoItem)
                newList
            }
        }
        repositoryScope.launch {
            RequestManager.createRequest<GetItemReceive>(
                methodType = HttpMethod.Post,
                address = TodoListConfig.LIST_ADDRESS.toString(),
                body = PostItemRequest(todo = todoItem.toDTO()),
            )
        }
    }

    fun deleteTodo(id: String) {
        repositoryScope.launch {
            databaseRepository.deleteTodoById(id)
            todos.update { list ->
                val index = list.indexOfFirst { it.id == id }
                val newList = list.toMutableList()
                newList.removeAt(index)
                newList
            }
        }
        repositoryScope.launch {
            RequestManager.createRequest<GetItemReceive>(
                methodType = HttpMethod.Delete,
                address = TodoListConfig.getElementListAddress(id),
            )
        }
    }

    fun updateTodo(todo: TodoItem) {
        repositoryScope.launch {
            databaseRepository.insertTodo(todo)
            todos.update { list ->
                val index = list.indexOfFirst { it.id == todo.id }
                val newList = list.toMutableList()
                newList[index] = todo
                newList
            }
        }
        repositoryScope.launch {
            RequestManager.createRequest<GetItemReceive>(
                methodType = HttpMethod.Put,
                address = TodoListConfig.getElementListAddress(todo.id),
                body = PostItemRequest(todo = todo.toDTO())
            )
        }
    }

    fun getTodoById(id: String): TodoItem? {
        return todos.value.find { it.id == id }
    }
}
