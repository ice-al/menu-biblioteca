package br.pucpr.meuslivros.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.meuslivros.R
import br.pucpr.meuslivros.data.Livro
import br.pucpr.meuslivros.databinding.ItemLivroBinding

/**
 * Adapter do RecyclerView.
 * ListAdapter + DiffUtil calculam só o que mudou na lista,
 * animando inclusões, edições e exclusões sem recarregar tudo.
 */
class LivroAdapter(
    private val aoClicar: (Livro) -> Unit,
    private val aoExcluir: (Livro) -> Unit
) : ListAdapter<Livro, LivroAdapter.LivroViewHolder>(DIFF) {

    inner class LivroViewHolder(private val binding: ItemLivroBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(livro: Livro) {
            val ctx = binding.root.context
            binding.txtTitulo.text = livro.titulo
            binding.txtAutor.text = livro.autor
            binding.txtDetalhes.text = ctx.getString(
                R.string.item_detalhes, livro.genero, livro.ano, livro.paginas
            )
            binding.txtStatus.text = ctx.getString(
                if (livro.lido) R.string.status_lido else R.string.status_nao_lido
            )
            binding.root.setOnClickListener { aoClicar(livro) }
            binding.btnExcluir.setOnClickListener { aoExcluir(livro) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val binding = ItemLivroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LivroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Livro>() {
            override fun areItemsTheSame(antigo: Livro, novo: Livro) = antigo.id == novo.id
            override fun areContentsTheSame(antigo: Livro, novo: Livro) = antigo == novo
        }
    }
}
