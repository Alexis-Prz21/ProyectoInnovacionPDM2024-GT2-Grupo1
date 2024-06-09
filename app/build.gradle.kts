plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Agregando el plugin Grafle de servicios de Google
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.proyectoinnovacionpdm2024_gt2_grupo1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyectoinnovacionpdm2024_gt2_grupo1"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")

    // Importar el Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth-ktx")

    // Autenticación con Google
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Dependencia para la autenticación
    implementation("com.google.firebase:firebase-auth:23.0.0")

    // Dependencias para acceder al Firebase Storage
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")

    // Importando para comprimir imágenes antes de subir a Firebase Storage
    implementation("id.zelory:compressor:3.0.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Dependencia para acceder a Piccaso
    implementation("com.squareup.picasso:picasso:2.8")

    // Dependencia para recortar imágenes
    implementation("com.github.yalantis:ucrop:2.2.8")
}