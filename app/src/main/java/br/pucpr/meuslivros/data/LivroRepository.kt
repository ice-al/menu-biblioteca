package br.pucpr.meuslivros.data

import kotlinx.coroutines.flow.Flow

/**
 * Repositório: camada entre a interface e a fonte de dados.
 * As telas não conhecem o Room; se um dia os dados vierem de uma API,
 * só esta classe muda.
 */
class LivroRepository(private val dao: LivroDao) {

    val livros: Flow<List<Livro>> = dao.listarTodos()

    suspend fun buscar(id: Long): Livro? = dao.buscarPorId(id)

    suspend fun salvar(livro: Livro) {
        if (livro.id == 0L) dao.inserir(livro) else dao.atualizar(livro)
    }

    suspend fun excluir(livro: Livro) = dao.excluir(livro)
}
