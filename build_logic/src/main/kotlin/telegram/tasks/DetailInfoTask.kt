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
                        info.append("- ${entry.name} %.1f KB\n".format(size))
                    }
                val tempReportFile = File.createTempFile("apk_detail_info", ".txt")
                tempReportFile.writeText(info.toString())

                runBlocking {
                    telegramApi.sendMessage(info.toString(), token.get(), chatId.get())
                }
            }
    }
}