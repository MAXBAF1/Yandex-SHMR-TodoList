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