package com.around_team.todolist

import android.content.Context
import androidx.room.Room
import com.around_team.todolist.data.db.Dao
import com.around_team.todolist.data.db.TodoListDatabase
import com.around_team.todolist.di.DataBaseModule
import com.around_team.todolist.di.HttpClientModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.gson.gson
import io.ktor.utils.io.ByteReadChannel
import java.text.DateFormat
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HttpClientModule::class, DataBaseModule::class]
)
object TestModule {

    @Provides
    @Singleton
    fun provideMockEngine(): MockEngine {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel("""{"ip":"127.0.0.1"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        return mockEngine
    }

    @Provides
    @Singleton
    fun provideHttpClient(engine: MockEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                gson {
                    setDateFormat(DateFormat.LONG)
                }
            }
            install(DefaultRequest) {
                header(HttpHeaders.Authorization, "Bearer Nienor")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 2)
                exponentialDelay()
            }
        }
    }

    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoListDatabase {
        return Room
            .inMemoryDatabaseBuilder(
                context,
                TodoListDatabase::class.java,
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(db: TodoListDatabase): Dao {
        return db.dao()
    }
}