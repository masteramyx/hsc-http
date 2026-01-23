rootProject.name = "hsc-http"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}


include("androidApp")
include("app")
include("db")
include("shared")
include("web")
include("react-web")

include("androidApp:android")