package br.pucpr.meuslivros.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.meuslivros.MeusLivrosApp
import br.pucpr.meuslivros.R
import br.pucpr.meuslivros.data.Livro
import br.pucpr.meuslivros.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/** Tela principal: lista de livros (READ) e atalhos para criar, editar e excluir. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: LivroViewModel by viewModels {
        LivroViewModel.Factory((application as MeusLivrosApp).repository)
    }

    private val adapter = LivroAdapter(
        aoClicar = { livro -> abrirFormulario(livro.id) },
        aoExcluir = { livro -> confirmarExclusao(livro) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.recyclerLivros.layoutManager = LinearLayoutManager(this)
        binding.recyclerLivros.adapter = adapter

        // CREATE: botão flutuante abre o formulário vazio
        binding.fabAdicionar.setOnClickListener { abrirFormulario(null) }

        // Observa o banco: qualquer mudança chega aqui e atualiza a lista
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.livros.collect { lista ->
                    adapter.submitList(lista)
                    binding.txtVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun abrirFormulario(id: Long?) {
        startActivity(FormLivroActivity.intent(this, id))
    }

    // DELETE: pede confirmação e oferece "Desfazer"
    private fun confirmarExclusao(livro: Livro) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.excluir_titulo)
            .setMessage(getString(R.string.excluir_mensagem, livro.titulo))
            .setNegativeButton(R.string.cancelar, null)
            .setPositiveButton(R.string.excluir) { _, _ ->
                viewModel.excluir(livro) {
                    Snackbar.make(binding.root, R.string.livro_excluido, Snackbar.LENGTH_LONG)
                        .setAction(R.string.desfazer) { viewModel.salvar(livro.copy(id = 0)) }
                        .show()
                }
            }
            .show()
    }
}
