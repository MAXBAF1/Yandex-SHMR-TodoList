package com.around_team.todolist.data.network.repositories

import android.util.Log
import com.around_team.todolist.R
import com.around_team.todolist.data.db.DatabaseRepository
import com.around_team.todolist.data.network.RequestManager
import com.around_team.todolist.data.network.NetworkConfig
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

/**
 * Singleton repository class responsible for managing Todo items.
 *
 * @property databaseRepository The repository for interacting with the local database.
 * @property todos Mutable state flow holding the current list of Todo items.
 * @property messages Mutable shared flow used for emitting error codes.
 */
@Singleton
class Repository @Inject constructor(
    private val requestManager: RequestManager,
    private val databaseRepository: DatabaseRepository
) {
    private val todos = MutableStateFlow(listOf<TodoItem>())
    private val messages = MutableSharedFlow<Int>(replay = 2)
    private var lastRemovedTodo: TodoItem? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is MyErrorIdException -> messages.tryEmit(throwable.errorId)
            else -> Log.e("MyLog", throwable.stackTraceToString())
        }
    }

    private val job = SupervisorJob()
    private val repositoryScope = CoroutineScope(Dispatchers.IO + job + exceptionHandler)

    fun getTodos(): StateFlow<List<TodoItem>> = todos
    fun getMessages(): Flow<Int> = messages

    fun getAllTodosFromBD() {
        repositoryScope.launch {
            todos.update { databaseRepository.getAllTodos() }
        }
    }

    fun refreshAllTodos() {
        repositoryScope.launch {
            val todosDTO = requestManager.createRequest<GetAllItemsReceive>(
                methodType = HttpMethod.Get,
                address = NetworkConfig.LIST_ADDRESS.toString(),
            )?.list ?: return@launch
            val todoItems = todosDTO.map { it.toTodoItem() }
            databaseRepository.replaceTodos(todoItems)
            todos.update { todoItems }
        }
    }

    fun sendAllTodos(list: List<TodoItem>, onSuccess: () -> Unit = {}) {
        repositoryScope.launch {
            val todosDTO = requestManager.createRequest<GetAllItemsReceive>(
                methodType = HttpMethod.Patch,
                address = NetworkConfig.LIST_ADDRESS.toString(),
                body = SendAllItemsRequest(list.map { it.toDTO() })
            )?.list ?: return@launch

            val todoItems = todosDTO.map { it.toTodoItem() }
            databaseRepository.replaceTodos(todoItems)
            todos.update { todoItems }
            onSuccess()
        }
    }

    fun saveTodo(todoItem: TodoItem? = lastRemovedTodo) {
        if (todoItem == null) return

        repositoryScope.launch {
            databaseRepository.insertTodo(todoItem)
            todos.update { list ->
                val newList = list.toMutableList()
                newList.add(todoItem)
                newList
            }
        }
        repositoryScope.launch {
            requestManager.createRequest<GetItemReceive>(
                methodType = HttpMethod.Post,
                address = NetworkConfig.LIST_ADDRESS.toString(),
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
                lastRemovedTodo = newList[index]
                newList.removeAt(index)
                newList
            }
            messages.tryEmit(R.string.todo_deleted)
        }
        repositoryScope.launch {
            requestManager.createRequest<GetItemReceive>(
                methodType = HttpMethod.Delete,
                address = NetworkConfig.getElementListAddress(id),
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
            requestManager.createRequest<GetItemReceive>(
                methodType = HttpMethod.Put,
                address = NetworkConfig.getElementListAddress(todo.id),
                body = PostItemRequest(todo = todo.toDTO())
            )
        }
    }

    fun getTodoById(id: String): TodoItem? {
        return todos.value.find { it.id == id }
    }
}
