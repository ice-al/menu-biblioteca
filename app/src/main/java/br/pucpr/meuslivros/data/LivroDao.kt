package br.pucpr.meuslivros.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object): define as operações do CRUD.
 * O Room gera a implementação em tempo de compilação.
 */
@Dao
interface LivroDao {

    // READ: Flow emite a lista de novo sempre que a tabela muda,
    // por isso a tela atualiza sozinha após inserir, editar ou excluir.
    @Query("SELECT * FROM livros ORDER BY titulo COLLATE NOCASE ASC")
    fun listarTodos(): Flow<List<Livro>>

    @Query("SELECT * FROM livros WHERE id = :id")
    suspend fun buscarPorId(id: Long): Livro?

    // CREATE
    @Insert
    suspend fun inserir(livro: Livro): Long

    // UPDATE
    @Update
    suspend fun atualizar(livro: Livro)

    // DELETE
    @Delete
    suspend fun excluir(livro: Livro)
}
