package br.pucpr.meuslivros.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Livro::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun livroDao(): LivroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton: garante uma única conexão com o banco no app inteiro.
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meus_livros.db"
                ).build().also { INSTANCE = it }
            }
    }
}
