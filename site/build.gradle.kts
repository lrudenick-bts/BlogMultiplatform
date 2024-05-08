import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.varabyte.kobweb.gradle.application.extensions.AppBlock.LegacyRouteRedirectStrategy
import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.script
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildKonfig)
}

group = "com.lrudenick.blogmultiplatform"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")

            head.add {
                script {
                    src = "/highlight.min.js"
                }
                link {
                    rel = "stylesheet"
                    href = "/github-dark.css"
                }
                script {
                    src =
                        "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                }

                link {
                    href = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                    rel = "stylesheet"
                }
            }
        }

        server {
            remoteDebugging {
                enabled.set(true)
                port.set(5005)
            }
        }

        // Only legacy sites need this set. Sites built after 0.16.0 should default to DISALLOW.
        // See https://github.com/varabyte/kobweb#legacy-routes for more information.
        legacyRouteRedirectStrategy.set(LegacyRouteRedirectStrategy.DISALLOW)
    }
}

kotlin {
    configAsKobwebApplication("blogmultiplatform", includeServer = true)

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(libs.kotlinx.serialization)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
            implementation(libs.kotlinx.serialization)

        }
        jvmMain.dependencies {
            compileOnly(libs.kobweb.api) // Provided by Kobweb backend at runtime
            implementation(libs.kmongo.database)
            implementation(libs.kotlinx.serialization)
        }
    }
}

buildkonfig {
    packageName = "com.lrudenick.blogmultiplatform"

    defaultConfigs {
        val prop = Properties().apply {
            load(FileInputStream(File(rootProject.rootDir, "local.properties")))
        }

        // MongoDB
        val mongoDbConnectionUri: String = prop.getProperty("mongoDbConnectionUri")
        require(mongoDbConnectionUri.isNotEmpty()) {
            "MondoDB connection URI required. See https://www.mongodb.com/docs/drivers/kotlin/coroutine/current/fundamentals/connection/connect/#connection-uri"
        }
        buildConfigField(STRING, "MDB_CONNECTION_URI", mongoDbConnectionUri)

        // Humor API
        val humorApiKey: String = prop.getProperty("humorApiKey")
        require(humorApiKey.isNotEmpty()) {
            "A Humor API Key is needed. Register here: https://humorapi.com/, get your own and put it inside the local.properties file."
        }
        buildConfigField(STRING, "HUMOR_API_KEY", humorApiKey)
    }
}
