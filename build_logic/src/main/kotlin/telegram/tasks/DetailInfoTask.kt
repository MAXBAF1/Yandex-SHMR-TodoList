package telegram.tasks

import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import telegram.TelegramApi
import utils.BytesConverter.bytesToKyloBytes
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject

abstract class DetailInfoTask @Inject constructor(
    private val telegramApi: TelegramApi,
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>


    @TaskAction
    fun execute() {
        apkDir.get().asFile
            .listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {
                val zipFile = ZipFile(it)
                val info = StringBuilder("APK info:\n")

                zipFile
                    .entries()
                    .asIterator()
                    .forEach { entry: ZipEntry ->
                        val size = entry.size.bytesToKyloBytes()
                        info.appendLine("- ${entry.name} %.1f KB".format(size))
                    }
                val tempReportFile = File.createTempFile("apk_detail_info", ".txt")
                tempReportFile.writeText(info.toString())

                runBlocking {
                    try {
                        telegramApi.upload(
                            file = tempReportFile,
                            fileName = "APK INFO",
                            chatId = chatId.get(),
                            token = token.get()
                        )
                    } catch (e: Exception) {
                        println(e)
                        telegramApi.sendMessage(
                            message = "Error with sending APK info",
                            token = token.get(),
                            chatId = chatId.get()
                        )
                    } finally {
                        if (tempReportFile.exists()) {
                            tempReportFile.delete()
                        }
                    }
                }
            }
    }
}