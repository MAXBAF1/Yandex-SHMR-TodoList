package com.around_team.todolist.data

import android.util.Log
import com.around_team.todolist.data.model.DTO
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object RequestManager {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    suspend fun createRequest(
        methodType: HttpMethod,
        address: String,
        body: DTO? = null,
    ): HttpResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.request(address) {
                    method = methodType
                    contentType(ContentType.Application.Json)

                    header(HttpHeaders.Authorization, "Bearer ${Token.TOKEN}")
                    header("X-Last-Known-Revision", 1)

                    body?.let { setBody(it) }
                }
                Log.d("MyLog", "R ${response.bodyAsText()}")

                if (response.status == HttpStatusCode.OK) {
                    response
                } else {
                    Log.e("MyLog", response.toString())
                    null
                }
            } catch (e: Exception) {
                Log.e("MyLog", e.stackTraceToString())
                null
            }
        }
    }
}