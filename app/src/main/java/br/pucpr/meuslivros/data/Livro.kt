package br.pucpr.meuslivros.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de dados: cada instância é uma linha da tabela "livros".
 * data class gera automaticamente equals, hashCode, toString e copy.
 */
@Entity(tableName = "livros")
data class Livro(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titulo: String,
    val autor: String,
    val genero: String,
    val ano: Int,
    val paginas: Int,
    val lido: Boolean = false
)
