package main

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) = App()
    .subcommands(
        Aluno(),
        DadosAluno(),
        Disciplina(),
        Historico(),
        Inscricao(),
        Diario(),
        Lista())
    .main(args)