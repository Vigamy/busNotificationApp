// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Removed the top-level kotlin.compose plugin alias because plugin resolution failed in some environments.
    // Apply the compose-related configuration per-module instead (module will set the compiler extension version).
    // alias(libs.plugins.kotlin.compose) apply false
    // Hilt plugin disponível para módulos
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.services) apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" apply false
}