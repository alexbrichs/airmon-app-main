// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.saveourtool.diktat") version "2.0.0"
}

diktat {
    inputs {
        include("app/src/**/*.kt")  // path matching this pattern (per PatternFilterable) that will be checked by diktat
        //exclude("app/src/main/java/com/airmon/app/ui/**")  // path matching this pattern will not be checked by diktat
    }
    reporters {
        plain {
            output = file("lintResult.txt")
        }
        json()
        html {
            output = file("someFile.html")
        }
        // checkstyle()
        // sarif()
        // gitHubActions()
    }
    debug = true  // turn on debug logging
}