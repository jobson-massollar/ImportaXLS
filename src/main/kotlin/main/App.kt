package main

import adapter.infrastructure.excel.Excel
import adapter.infrastructure.exposed.ExposedDAOFactory
import adapter.infrastructure.exposed.connectToDatabase
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import model.Entity
import model.EntityDTO
import services.application.Operation
import services.application.SpreadsheetService
import model.AlunoRepository
import services.domain.persistence.DAOFactory
import model.DisciplinaRepository
import services.domain.persistence.IDAO
import model.InscricaoRepository
import model.ItemDiarioRepository
import model.ItemHistoricoRepository
import model.PreRequisitoRepository
import model.Repository
import model.RepositoryFactory
import java.util.Locale.getDefault
import kotlin.io.path.pathString

abstract class Command(commandName: String): CliktCommand(name = commandName) {
    val path by option("-path", "-p", help = "Path to Excel file").path(mustExist = true, canBeDir = false, mustBeReadable = true).required()

    fun run(operation: Operation, delete: Boolean = false, sep: String = ",", depto: String = "") {

        if (! AppConfig.isValid()) {
            println("Arquivo ${AppConfig.APP_PROP_FILE} não encontrado ou inválido!");
            return;
        }

        if (! connectToDatabase(AppConfig.url, AppConfig.user, AppConfig.password)) {
            println("Erro na conexão com o BD com os parâmetros definidos em ${AppConfig.APP_PROP_FILE}!");
            return;
        }

//        transaction {
//            SchemaUtils.create(
//                Alunos,
//                Departamentos,
//                Disciplinas,
//                Inscricoes,
//                ItensHistorico,
//                ItensDiario,
//            )
//        }

        DAOFactory.register(ExposedDAOFactory)

        val repo: Repository<out Entity, out IDAO<out EntityDTO>, out EntityDTO>? = when (operation) {
            Operation.ALUNO, Operation.DADOS_ALUNO -> RepositoryFactory.get(AlunoRepository::class)
            Operation.HISTORICO -> RepositoryFactory.get(ItemHistoricoRepository::class)
            Operation.INSCRICAO -> RepositoryFactory.get(InscricaoRepository::class)
            Operation.DISCIPLINA -> RepositoryFactory.get(DisciplinaRepository::class)
            Operation.DIARIO -> RepositoryFactory.get(ItemDiarioRepository::class)
            Operation.PRE_REQUISITO -> RepositoryFactory.get(PreRequisitoRepository::class)
            Operation.LIST -> null
        }

        SpreadsheetService(Excel(path.pathString)).import(operation, repo, delete, sep, depto.uppercase(getDefault()))
    }
}

abstract class  DBCommand(commandName: String): Command(commandName) {
    val delete by option("--delete", "-del", help = "Delete table before insertions").flag()
}

class Aluno: DBCommand("alunos") {

    override fun help(context: Context) = "Import data to alunos table"

    override fun run() {
        run(Operation.ALUNO, delete)
    }

}

class DadosAluno: DBCommand("dados-alunos") {

    override fun help(context: Context) = "Import address data to alunos table"

    override fun run() {
        run(Operation.DADOS_ALUNO, delete)
    }

}

class Historico: DBCommand("historicos") {

    override fun help(context: Context) = "Import data to itens_historico table"

    override fun run() {
        run(Operation.HISTORICO, delete)
    }

}

class Inscricao: DBCommand("inscricoes") {

    override fun help(context: Context) = "Import data to inscricoes table"

    override fun run() {
        run(Operation.INSCRICAO, delete)
    }

}

class Disciplina: DBCommand("disciplinas") {

    override fun help(context: Context) = "Import data to disciplinas table"

    override fun run() {
        run(Operation.DISCIPLINA, delete)
    }

}

class PreRequisitos: DBCommand("pre-requisitos") {

    override fun help(context: Context) = "Import data to pre-requisitos table"

    override fun run() {
        run(Operation.PRE_REQUISITO, delete)
    }

}

class Diario: DBCommand("diarios") {
    val depto by option("--depto", "-d", help = "Department").choice("dia", "dmq", "dmat").required()

    override fun help(context: Context) = "Import data to itens_diario table"

    override fun run() {
        run(Operation.DIARIO, delete = delete, depto = depto)
    }

}

class Lista: Command("lista") {
    val sep by option("--sep", "-s", help = "Column separator for listing operation (default=comma)").default(",")

    override fun help(context: Context) = "List data without importing"

    override fun run() {
        run(Operation.LIST, sep = sep)
    }

}

class App: NoOpCliktCommand()