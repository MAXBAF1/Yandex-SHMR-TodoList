plugins {
    id("base-android-plugin")
    id("todolist-compose")
    id("todolist-hilt")
    id("telegram-reporter")
}

android {
    defaultConfig {
        applicationId = "com.around_team.todolist"
        versionCode = AndroidConst.VERSION_CODE
        versionName = AndroidConst.VERSION_NAME
    }
}

tgReporter {
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
    detailInfoEnabled.set(true)
    validateSizeTaskEnabled.set(true)
    apkSizeLimitInMB.set(1000)
}
