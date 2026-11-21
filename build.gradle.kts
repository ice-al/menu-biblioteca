// Arquivo de build do projeto raiz: apenas declara os plugins e suas versões.
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    // KSP é o processador de anotações usado pelo Room para gerar o código do banco.
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}
