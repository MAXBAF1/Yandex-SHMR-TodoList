plugins {
    id("base-android-plugin")
    id("todolist-compose")
    id("todolist-hilt")
    id("telegram-reporter")
}

android {
    defaultConfig {
        applicationId = "com.around_team.todolist"
        versionCode = 1
        versionName = "1.0"
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs(
                    "src\\main\\assets",
                    "src\\main\\java\\com\\around_team\\todolist\\ui\\screens\\about\\assets"
                )
            }
        }
    }
}

tgReporter {
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
    detailInfoEnabled.set(true)
    validateSizeTaskEnabled.set(true)
    apkSizeLimitInMB.set(100)
}
