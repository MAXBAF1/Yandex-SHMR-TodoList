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
}

tgReporter {
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
    detailInfoEnabled.set(true)
    apkSizeLimitInMB.set(1000)
}
