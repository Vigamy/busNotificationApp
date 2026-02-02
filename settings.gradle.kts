pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev"))
        maven(uri("https://maven.pkg.jetbrains.space/public/p/compose/dev"))
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BusNotification"
include(":app")
