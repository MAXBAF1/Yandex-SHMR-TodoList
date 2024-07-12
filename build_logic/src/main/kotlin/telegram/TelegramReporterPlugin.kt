package telegram

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.create
import telegram.tasks.DetailInfoTask
import telegram.tasks.TelegramReporterTask
import telegram.tasks.ValidateSizeTask
import java.util.Locale

class TelegramReporterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw GradleException("Android not found")

        val extension = project.extensions
            .create("tgReporter", TelegramExtension::class)
            .apply {
                apkSizeLimitInMB.convention(APK_SIZE_LIMIT)
                validateSizeTaskEnabled.convention(VALIDATE_SIZE_TASK_ENABLED)
                detailInfoEnabled.convention(DETAIL_INFO_ENABLED)
            }
        val telegramApi = TelegramApi(HttpClient(OkHttp))

        androidComponents.onVariants { variant ->
            val artifacts = variant.artifacts.get(SingleArtifact.APK)
            val buildVariantName = variant.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }

            registerValidateApkSize(project, buildVariantName, telegramApi, artifacts, extension)
            registerReportTelegramApk(project, buildVariantName, telegramApi, artifacts, extension)
            registerApkDetailInfo(project, buildVariantName, telegramApi, artifacts, extension)
        }
    }

    private fun registerValidateApkSize(
        project: Project,
        buildVariantName: String,
        telegramApi: TelegramApi,
        artifacts: Provider<Directory>,
        extension: TelegramExtension,
    ) {
        project.tasks
            .register(
                "validateApkSizeFor$buildVariantName",
                ValidateSizeTask::class.java,
                telegramApi,
            )
            .configure {
                enabled = extension.validateSizeTaskEnabled.get()
                apkDir.set(artifacts)
                apkSizeLimitInMB.set(extension.apkSizeLimitInMB)
                token.set(extension.token)
                chatId.set(extension.chatId)
            }
    }

    private fun registerReportTelegramApk(
        project: Project,
        buildVariantName: String,
        telegramApi: TelegramApi,
        artifacts: Provider<Directory>,
        extension: TelegramExtension,
    ) {
        project.tasks
            .register(
                "reportTelegramApkFor$buildVariantName",
                TelegramReporterTask::class.java,
                telegramApi,
            )
            .configure {
                apkDir.set(artifacts)
                token.set(extension.token)
                chatId.set(extension.chatId)
            }
    }

    private fun registerApkDetailInfo(
        project: Project,
        buildVariantName: String,
        telegramApi: TelegramApi,
        artifacts: Provider<Directory>,
        extension: TelegramExtension,
    ) {
        project.tasks
            .register(
                "apkDetailInfoFor$buildVariantName", DetailInfoTask::class.java, telegramApi
            )
            .configure {
                enabled = extension.detailInfoEnabled.get()
                apkDir.set(artifacts)
                token.set(extension.token)
                chatId.set(extension.chatId)
            }
    }


    companion object {
        private const val APK_SIZE_LIMIT = 1000
        private const val VALIDATE_SIZE_TASK_ENABLED = true
        private const val DETAIL_INFO_ENABLED = true
    }
}

interface TelegramExtension {
    val token: Property<String>
    val chatId: Property<String>
    val detailInfoEnabled: Property<Boolean>
    val validateSizeTaskEnabled: Property<Boolean>
    val apkSizeLimitInMB: Property<Int>
}
