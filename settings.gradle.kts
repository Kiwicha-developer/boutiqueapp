pluginManagement {
    repositories {
        gradlePluginPortal()
        google ()
        mavenCentral()
        maven { url = uri("https://artifacts.mercadolibre.com/repository/android-releases") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://artifacts.mercadolibre.com/repository/android-releases")
        }
    }
}

rootProject.name = "BoutiqueSmart"
include(":app")
 