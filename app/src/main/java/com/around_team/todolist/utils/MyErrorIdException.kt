package com.around_team.todolist.utils

/**
 * Exception representing an error identified by an error ID.
 *
 * @property errorId The ID associated with the specific error.
 */
data class MyErrorIdException(val errorId: Int) : Exception()