plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("todolist-compose") {
            id = "todolist-compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("todolist-hilt") {
            id = "todolist-hilt"
            implementationClass = "HiltConventionPlugin"
        }
        plugins.register("telegram-reporter") {
            id = "telegram-reporter"
            implementationClass = "telegram.TelegramReporterPlugin"
        }
    }
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.ksp.gradle)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}