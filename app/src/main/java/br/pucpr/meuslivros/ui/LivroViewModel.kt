package br.pucpr.meuslivros.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.pucpr.meuslivros.data.Livro
import br.pucpr.meuslivros.data.LivroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel: guarda o estado da tela e sobrevive a rotações.
 * As operações de banco rodam em corrotinas (viewModelScope),
 * fora da thread principal, para não travar a interface.
 */
class LivroViewModel(private val repository: LivroRepository) : ViewModel() {

    val livros: Flow<List<Livro>> = repository.livros

    suspend fun buscar(id: Long): Livro? = repository.buscar(id)

    fun salvar(livro: Livro, aoConcluir: () -> Unit = {}) {
        viewModelScope.launch {
            repository.salvar(livro)
            aoConcluir()
        }
    }

    fun excluir(livro: Livro, aoConcluir: () -> Unit = {}) {
        viewModelScope.launch {
            repository.excluir(livro)
            aoConcluir()
        }
    }

    /** Fábrica necessária porque o ViewModel recebe o repositório no construtor. */
    class Factory(private val repository: LivroRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LivroViewModel::class.java)) {
                return LivroViewModel(repository) as T
            }
            throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
        }
    }
}
