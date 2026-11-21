# Meus Livros: CRUD Android em Kotlin

Atividade Somativa 1, Desenvolvimento para Dispositivos Móveis (PUCPR).

Aplicativo de cadastro de livros com as quatro operações do CRUD, lista em
RecyclerView e persistência local com Room (os dados continuam salvos depois
de fechar o app).

## Como abrir

1. Android Studio: File > Open > selecione a pasta `MeusLivros`.
2. Aguarde o Gradle Sync (baixa as dependências na primeira vez).
3. Rode em um emulador ou celular com Android 7.0 (API 24) ou superior.

## Funcionalidades

| Operação | Onde acontece |
|---|---|
| Create | Botão "Novo livro" abre o formulário vazio; campos validados antes de salvar |
| Read | Tela inicial lista os livros em RecyclerView, ordenados por título |
| Update | Toque em um card abre o formulário preenchido; "Salvar" atualiza |
| Delete | Ícone de lixeira no card ou botão "Excluir" na edição, com confirmação e opção "Desfazer" |

## Arquitetura

```
data/
  Livro.kt            entidade (tabela "livros")
  LivroDao.kt         consultas SQL do CRUD
  AppDatabase.kt      banco Room (singleton)
  LivroRepository.kt  isola a fonte de dados das telas
ui/
  LivroViewModel.kt   estado e operações, em corrotinas
  LivroAdapter.kt     ListAdapter + DiffUtil para o RecyclerView
  MainActivity.kt     lista (Read) e exclusão
  FormLivroActivity.kt cadastro e edição (Create/Update) e exclusão
MeusLivrosApp.kt      cria banco e repositório uma única vez
```

A lista se atualiza sozinha: o DAO devolve um `Flow`, que emite novamente a
cada alteração na tabela, e o `ListAdapter` aplica só a diferença.

Boas práticas aplicadas: separação em camadas (MVVM + Repository), ViewBinding
em vez de findViewById, operações de banco fora da thread principal,
`data class` imutável com `copy()`, textos em `strings.xml`, validação com
mensagem de erro no próprio campo.
