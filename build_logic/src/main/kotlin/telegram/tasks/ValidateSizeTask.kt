package telegram.tasks

import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import telegram.TelegramApi
import utils.BytesConverter.bytesToMegaBytes
import java.util.Locale
import javax.inject.Inject

abstract class ValidateSizeTask @Inject constructor(
    private val tgApi: TelegramApi,
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val apkSizeLimitInMB: Property<Int>

    @get:Input
    abstract val chatId: Property<String>


    @TaskAction
    fun execute() {
        apkDir.get().asFile
            .listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach { apkFile ->
                runBlocking {
                    val size = apkFile
                        .length()
                        .bytesToMegaBytes()
                    if (size > apkSizeLimitInMB.get()) {
                        tgApi.sendMessage(
                            message = "Build apk > ${apkSizeLimitInMB.get()}MB",
                            token = token.get(),
                            chatId = chatId.get()
                        )
                        throw GradleException("ValidateSizeTask failed: larger size")
                    }

                    project.extensions.extraProperties.set(
                        SIZE_KEY,
                        String
                            .format(Locale.ROOT, "%.1f", size)
                            .toFloat()
                    )
                }
            }
    }

    companion object {
        private const val SIZE_KEY = "SIZE"
    }
}

