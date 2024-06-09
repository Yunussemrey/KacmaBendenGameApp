buildscript{
    dependencies{
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
    repositories{
        google()
        mavenCentral()
    }
}
plugins {
    id("com.android.application") version "8.1.0" apply false
}
