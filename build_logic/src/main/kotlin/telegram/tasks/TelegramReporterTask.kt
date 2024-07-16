package telegram.tasks

import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import telegram.TelegramApi
import javax.inject.Inject

abstract class TelegramReporterTask @Inject constructor(
    private val telegramApi: TelegramApi,
    private val buildVariant: String,
    private val versionCode: String,
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @TaskAction
    fun report() {
        val token = token.get()
        val chatId = chatId.get()
        val resultFileName = "todoList-$buildVariant-$versionCode.apk"

        apkDir.get().asFile
            .listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {
                runBlocking {
                    telegramApi.sendMessage("Build finished", token, chatId)
                }
                runBlocking {
                    telegramApi
                        .upload(it, resultFileName, token, chatId)
                        .apply {
                            println("Upload status = $status")
                        }
                }
            }
    }
}