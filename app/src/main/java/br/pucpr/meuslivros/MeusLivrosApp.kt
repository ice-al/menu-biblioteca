package br.pucpr.meuslivros

import android.app.Application
import br.pucpr.meuslivros.data.AppDatabase
import br.pucpr.meuslivros.data.LivroRepository

/**
 * Classe Application: criada uma única vez quando o app abre.
 * Guarda o banco e o repositório para que todas as telas usem a mesma instância.
 */
class MeusLivrosApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    val repository: LivroRepository by lazy { LivroRepository(database.livroDao()) }
}
