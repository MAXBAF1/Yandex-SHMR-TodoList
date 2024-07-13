package telegram

import AndroidConst
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
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

            val sizeTask = registerValidateApkSize(
                project, buildVariantName, telegramApi, artifacts, extension
            )
            val reportTask = registerReportTelegramApk(
                project,
                buildVariantName,
                telegramApi,
                artifacts,
                extension,
                variant.name,
                AndroidConst.VERSION_NAME
            )
            val detailTask = registerApkDetailInfo(project, buildVariantName, telegramApi, artifacts, extension)

            sizeTask.dependsOn(detailTask)
            reportTask.dependsOn(sizeTask)
        }
    }

    private fun registerValidateApkSize(
        project: Project,
        buildVariantName: String,
        telegramApi: TelegramApi,
        artifacts: Provider<Directory>,
        extension: TelegramExtension,
    ): TaskProvider<ValidateSizeTask> {
        return project.tasks
            .register(
                "validateApkSizeFor$buildVariantName",
                ValidateSizeTask::class.java,
                telegramApi,
            )
            .apply {
                configure {
                    enabled = extension.validateSizeTaskEnabled.get()
                    apkDir.set(artifacts)
                    apkSizeLimitInMB.set(extension.apkSizeLimitInMB)
                    token.set(extension.token)
                    chatId.set(extension.chatId)
                }
            }
    }

    private fun registerReportTelegramApk(
        project: Project,
        buildVariantName: String,
        telegramApi: TelegramApi,
        artifacts: Provider<Directory>,
        extension: TelegramExtension,
        buildVariant: String,
        versionCode: String,
    ): TaskProvider<TelegramReporterTask> {
        return project.tasks
            .register(
                "reportTelegramApkFor$buildVariantName",
                TelegramReporterTask::class.java,
                telegramApi,
                buildVariant,
                versionCode,
            )
            .apply {
                configure {
                    apkDir.set(artifacts)
                    token.set(extension.token)
                    chatId.set(extension.chatId)
                }
            }
    }

    private fun registerApkDetailInfo(
        project: Project,
        buildVariantName: String,
        telegramApi: TelegramApi,
        artifacts: Provider<Directory>,
        extension: TelegramExtension,
    ): TaskProvider<DetailInfoTask> {
        return project.tasks
            .register(
                "apkDetailInfoFor$buildVariantName", DetailInfoTask::class.java, telegramApi
            )
            .apply {
                configure {
                    enabled = extension.detailInfoEnabled.get()
                    apkDir.set(artifacts)
                    token.set(extension.token)
                    chatId.set(extension.chatId)
                }
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
