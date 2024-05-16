import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildKonfig)
}

group = "com.lrudenick.blogmultiplatform"
version = "1.0-SNAPSHOT"

kotlin {

    js(IR) { browser() }

    jvm()

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization)
        }

        jsMain.dependencies {
            implementation(libs.kotlinx.serialization)

        }
        jvmMain.dependencies {
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
