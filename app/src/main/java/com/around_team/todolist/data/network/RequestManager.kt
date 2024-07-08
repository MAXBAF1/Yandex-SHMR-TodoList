package com.around_team.todolist.data.network

import android.util.Log
import com.around_team.todolist.R
import com.around_team.todolist.data.network.model.receive.ReceiveDTO
import com.around_team.todolist.data.network.model.request.RequestDTO
import com.around_team.todolist.utils.MyErrorIdException
import com.around_team.todolist.utils.PreferencesHelper
import com.around_team.todolist.utils.castOrNull
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import java.text.DateFormat
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A manager for handling HTTP requests using the Ktor HTTP client with OkHttp and JSON content negotiation.
 */
class RequestManager @Inject constructor(private val preferencesHelper: PreferencesHelper) {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        install(DefaultRequest) {
            header(HttpHeaders.Authorization, "OAuth ${preferencesHelper.getToken()}")
            header(ERRORS_HEADER, 20)
            header(REVISION_HEADER, lastKnownRevision.toString())
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay()
        }
    }
    var lastKnownRevision = 0

    suspend inline fun <reified T : ReceiveDTO> createRequest(
        methodType: HttpMethod,
        address: String,
        body: RequestDTO? = null,
    ): T? {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.request {
                    method = methodType
                    url(address)
                    body?.let { setBody(it) }

                    Log.d("MyLog", "type: $method; body: $body")
                }
                Log.d("MyLog", "Receive body: ${response.bodyAsText()}")

                if (response.status == HttpStatusCode.OK) {
                    val dto = response.castOrNull<T>()
                    lastKnownRevision = dto?.revision ?: 0
                    dto
                } else {
                    Log.e("MyLog", response.toString())
                    throw MyErrorIdException(R.string.sync_error)
                }
            } catch (e: UnknownHostException) {
                null
            }
        }
    }

    companion object {
        private const val ERRORS_HEADER = "X-Generate-Fails"
        private const val REVISION_HEADER = "X-Last-Known-Revision"
    }
}