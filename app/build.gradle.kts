// Este bloque define los plugins de Gradle que se aplicarán al módulo de la aplicación.
plugins {
    // Plugin de aplicación de Android: necesario para construir una aplicación de Android.
    alias(libs.plugins.android.application)
    // Plugin de Kotlin para Android: habilita el soporte para el lenguaje de programación Kotlin.
    alias(libs.plugins.kotlin.android)
    // Plugin de Kotlin para Jetpack Compose: habilita el soporte para el compilador de Compose.
    alias(libs.plugins.kotlin.compose)

    // Habilita el Procesador de Anotaciones de Kotlin (KAPT). Es necesario para bibliotecas como Room, que generan código en tiempo de compilación.
    kotlin("kapt")

}

// Este bloque configura las opciones específicas del proyecto de Android.
android {
    // El `namespace` es un identificador único para el código fuente de tu aplicación (generalmente, tu nombre de paquete).
    namespace = "com.ct.junimostoreapp"
    // `compileSdk` es la versión del SDK de Android con la que se compila tu aplicación.
    compileSdk = 36

    // `defaultConfig` contiene la configuración que se aplica a todas las variantes de compilación.
    defaultConfig {
        // `applicationId` es el identificador único de tu aplicación en el dispositivo y en la Play Store.
        applicationId = "com.ct.junimostoreapp"
        // `minSdk` es la versión mínima de Android en la que tu aplicación puede ejecutarse.
        minSdk = 24
        // `targetSdk` es la versión de Android para la que tu aplicación está diseñada.
        targetSdk = 36
        // `versionCode` es un número entero que representa la versión de tu aplicación. Debe incrementarse con cada lanzamiento.
        versionCode = 1
        // `versionName` es la cadena de texto que se muestra a los usuarios como la versión de la aplicación.
        versionName = "1.0"

        // `testInstrumentationRunner` es la clase que se utiliza para ejecutar las pruebas de instrumentación (UI tests).
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // `buildTypes` define cómo se construyen las diferentes variantes de tu aplicación (ej. debug, release).
    buildTypes {
        // `release` es la configuración para la versión de producción de tu aplicación.
        release {
            // `isMinifyEnabled` habilita o deshabilita la ofuscación y minimización del código para reducir el tamaño del APK.
            isMinifyEnabled = false
            // `proguardFiles` especifica los archivos de configuración de ProGuard para la ofuscación.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // `compileOptions` configura las opciones del compilador de Java.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // Versión del lenguaje Java para el código fuente.
        targetCompatibility = JavaVersion.VERSION_11 // Versión de la JVM para la que se compila el código.
    }
    // `kotlinOptions` configura las opciones del compilador de Kotlin.
    kotlinOptions {
        jvmTarget = "11" // Versión de la JVM para la que se compila el código de Kotlin.
    }
    // `buildFeatures` habilita o deshabilita características específicas del proceso de compilación.
    buildFeatures {
        compose = true // Habilita Jetpack Compose para la UI.
    }
}

// Este bloque define las dependencias de tu módulo `app`.
dependencies {

    // Dependencias básicas de AndroidX y Jetpack Compose.
    implementation(libs.androidx.core.ktx) // Extensiones de Kotlin para las APIs de Android.
    implementation(libs.androidx.lifecycle.runtime.ktx) // Soporte para el ciclo de vida de los componentes con corutinas.
    implementation(libs.androidx.activity.compose) // Integración de Jetpack Compose con Activities.
    implementation(platform(libs.androidx.compose.bom)) // BOM (Bill of Materials) para gestionar las versiones de las dependencias de Compose.
    implementation(libs.androidx.compose.ui) // Componentes de la UI de Compose.
    implementation(libs.androidx.compose.ui.graphics) // Gráficos y primitivas de dibujo de Compose.
    implementation(libs.androidx.compose.ui.tooling.preview) // Soporte para previsualizaciones en Android Studio.
    implementation(libs.androidx.compose.material3) // Componentes de Material Design 3 para Compose.

    // Dependencia para la navegación con Jetpack Compose.
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Dependencias para los íconos de Material Design.
    implementation("androidx.compose.material:material-icons-core") // Íconos básicos.
    implementation("androidx.compose.material:material-icons-extended") // Íconos extendidos.

    // Dependencias para Coil, una biblioteca de carga de imágenes.
    implementation("io.coil-kt:coil-compose:2.5.0") // Integración de Coil con Jetpack Compose.
    implementation("io.coil-kt:coil-gif:2.5.0") // Soporte para mostrar GIFs con Coil.

    // Dependencias para Room, una biblioteca de persistencia de datos (base de datos local).
    implementation("androidx.room:room-runtime:2.6.1")  // API principal de Room.
    kapt("androidx.room:room-compiler:2.6.1")          // Procesador de anotaciones que genera el código necesario para Room.
    implementation("androidx.room:room-ktx:2.6.1")     // Extensiones de Kotlin para Room (soporte para corutinas y Flow).

    // Dependencias para el escáner de códigos QR.
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0") // Biblioteca para escanear códigos con la UI de Google.
    implementation("com.google.mlkit:barcode-scanning:17.2.0") // Kit de aprendizaje automático de Google para el reconocimiento de códigos de barras.

    // Dependencias para pruebas unitarias.
    testImplementation(libs.junit)
    // Dependencias para pruebas de instrumentación (UI tests).
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM de Compose para pruebas.
    androidTestImplementation(libs.androidx.compose.ui.test.junit4) // Herramientas de prueba de Compose con JUnit 4.
    // Dependencias para depuración.
    debugImplementation(libs.androidx.compose.ui.tooling) // Herramientas de Compose para depuración.
    debugImplementation(libs.androidx.compose.ui.test.manifest) // Manifiesto de prueba para depuración.
}
