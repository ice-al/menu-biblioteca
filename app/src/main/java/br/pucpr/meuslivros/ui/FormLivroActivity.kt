package br.pucpr.meuslivros.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.pucpr.meuslivros.MeusLivrosApp
import br.pucpr.meuslivros.R
import br.pucpr.meuslivros.data.Livro
import br.pucpr.meuslivros.databinding.ActivityFormLivroBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Formulário usado tanto para cadastrar (CREATE) quanto para editar (UPDATE).
 * Se receber um id pelo Intent, carrega o livro e entra em modo edição.
 */
class FormLivroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormLivroBinding
    private var livroAtual: Livro? = null

    private val viewModel: LivroViewModel by viewModels {
        LivroViewModel.Factory((application as MeusLivrosApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLivroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getLongExtra(EXTRA_ID, SEM_ID)
        if (id == SEM_ID) {
            supportActionBar?.title = getString(R.string.titulo_novo)
            binding.btnExcluir.visibility = View.GONE
        } else {
            supportActionBar?.title = getString(R.string.titulo_editar)
            carregarLivro(id)
        }

        binding.btnSalvar.setOnClickListener { salvar() }
        binding.btnExcluir.setOnClickListener { confirmarExclusao() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun carregarLivro(id: Long) {
        lifecycleScope.launch {
            val livro = viewModel.buscar(id)
            if (livro == null) {
                Toast.makeText(this@FormLivroActivity, R.string.livro_nao_encontrado, Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }
            livroAtual = livro
            binding.edtTitulo.setText(livro.titulo)
            binding.edtAutor.setText(livro.autor)
            binding.edtGenero.setText(livro.genero)
            binding.edtAno.setText(livro.ano.toString())
            binding.edtPaginas.setText(livro.paginas.toString())
            binding.chkLido.isChecked = livro.lido
        }
    }

    /** Valida os campos e mostra o erro no próprio campo. Retorna null se inválido. */
    private fun lerFormulario(): Livro? {
        val titulo = binding.edtTitulo.text.toString().trim()
        val autor = binding.edtAutor.text.toString().trim()
        val genero = binding.edtGenero.text.toString().trim()
        val ano = binding.edtAno.text.toString().toIntOrNull()
        val paginas = binding.edtPaginas.text.toString().toIntOrNull()
        val anoAtual = Calendar.getInstance().get(Calendar.YEAR)

        binding.tilTitulo.error = if (titulo.isEmpty()) getString(R.string.erro_obrigatorio) else null
        binding.tilAutor.error = if (autor.isEmpty()) getString(R.string.erro_obrigatorio) else null
        binding.tilGenero.error = if (genero.isEmpty()) getString(R.string.erro_obrigatorio) else null
        binding.tilAno.error =
            if (ano == null || ano < 1 || ano > anoAtual) getString(R.string.erro_ano, anoAtual) else null
        binding.tilPaginas.error =
            if (paginas == null || paginas <= 0) getString(R.string.erro_paginas) else null

        val temErro = listOf(
            binding.tilTitulo, binding.tilAutor, binding.tilGenero, binding.tilAno, binding.tilPaginas
        ).any { it.error != null }
        if (temErro) return null

        // copy() mantém o id original na edição; no cadastro o id é 0 e o Room gera um novo
        val base = livroAtual ?: Livro(titulo = "", autor = "", genero = "", ano = 0, paginas = 0)
        return base.copy(
            titulo = titulo,
            autor = autor,
            genero = genero,
            ano = ano!!,
            paginas = paginas!!,
            lido = binding.chkLido.isChecked
        )
    }

    private fun salvar() {
        val livro = lerFormulario() ?: return
        val msg = if (livro.id == 0L) R.string.livro_cadastrado else R.string.livro_atualizado
        viewModel.salvar(livro) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun confirmarExclusao() {
        val livro = livroAtual ?: return
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.excluir_titulo)
            .setMessage(getString(R.string.excluir_mensagem, livro.titulo))
            .setNegativeButton(R.string.cancelar, null)
            .setPositiveButton(R.string.excluir) { _, _ ->
                viewModel.excluir(livro) {
                    Toast.makeText(this, R.string.livro_excluido, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .show()
    }

    companion object {
        private const val EXTRA_ID = "extra_livro_id"
        private const val SEM_ID = -1L

        fun intent(context: Context, id: Long?): Intent =
            Intent(context, FormLivroActivity::class.java).apply {
                if (id != null) putExtra(EXTRA_ID, id)
            }
    }
}
