package services.application

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char
import model.*
import services.domain.spreadsheet.Spreadsheet

enum class Operation {
    ALUNO, DADOS_ALUNO, HISTORICO, INSCRICAO, DISCIPLINA, DIARIO, PRE_REQUISITO, LIST
}

private fun <T> String.transform(block: String.() -> T): T? = if (this == "NULL" || this.isEmpty() || this.isBlank() ) null else this.block()

private fun <T> String.transform(default: T, block: String.() -> T): T = if (this == "NULL" || this.isEmpty() || this.isBlank() ) default else this.block()

private fun String.toEmpty() = if (this == "NULL") "" else this

private fun String.toPeriodo() = if (this[0].isDigit()) this[0].digitToInt() else 3

class SpreadsheetService(val spreadSheet: Spreadsheet) {

    fun import(operation: Operation, repo: IRepository<out Entity>?, delete: Boolean, sep: String, depto: String) {
        if (delete && repo != null && operation != Operation.DADOS_ALUNO) {
            repo.deleteAll()
        }

        repo?.startBatchInsert(1000)

        when (operation) {
            Operation.ALUNO -> readAlunos().forEach { (repo as AlunoRepository).batchInsert(it) }
            Operation.DADOS_ALUNO -> readDadosAlunos().forEach { (repo as AlunoRepository).updateAluno(it) }
            Operation.HISTORICO -> readItensHistorico().forEach { (repo as ItemHistoricoRepository).batchInsert(it) }
            Operation.INSCRICAO -> readInscricoes().forEach { (repo as InscricaoRepository).batchInsert(it) }
            Operation.DISCIPLINA -> readDisciplinas().forEach { (repo as DisciplinaRepository).batchInsert(it) }
            Operation.PRE_REQUISITO -> readPreRequisitos().forEach { (repo as PreRequisitoRepository).batchInsert(it) }
            Operation.DIARIO -> {
                val disciplinas = RepositoryFactory.get(DisciplinaRepository::class).findAll()
                readDiario(depto).forEach { itemDiario ->
                    if (disciplinas.any { disciplina -> disciplina.codigo == itemDiario.codigo })
                        (repo as ItemDiarioRepository).batchInsert(itemDiario)
                }
            }
            Operation.LIST -> spreadSheet.readAsSequence()
                .forEach { line ->
                    println(line.joinToString("$sep "))
                }
        }

        repo?.endBatchInsert()
    }

    private fun readDisciplinas(): Sequence<Disciplina> = sequence {
        val disciplinaXLS = listOf(1, 3, 6, 7, 8, 9, 12, 19, 11)

        readSpreadsheet(20,disciplinaXLS) { line, map ->
            Disciplina.of(
                line[map[0]],
                line[map[1]],
                line[map[2]],
                line[map[3]].toInt(),
                line[map[4]].transform(0) { this.toInt() },
                line[map[5]].toInt(),
                line[map[6]],
                line[map[7]],
                line[map[8]]
            )
        }
    }

    private fun readAlunos(): Sequence<Aluno> = sequence {
        val alunoXLS = listOf(8, 1, 2, 3, 9, 4, 5, 11)
        val df = LocalDate.Format {
            day()
            char('/')
            monthNumber()
            char('/')
            year()
        }

        readSpreadsheet(14,alunoXLS) { line, map ->
            Aluno.of(
                line[map[0]],
                line[map[1]],
                line[map[2]][0],
                line[map[3]].transform { LocalDate.parse(this, df) },
                line[map[4]],
                line[map[5]],
                line[map[6]],
                line[map[7]].transform { LocalDate.parse(this, df) }
            )
        }
    }

    private fun readDadosAlunos(): Sequence<DadosAluno> = sequence {
        val dadosAlunoXLS = listOf(1, 2, 3, 6, 4, 9, 10, 7, 13, 5)

        readSpreadsheet(14,dadosAlunoXLS) { line, map ->
            DadosAluno(
                line[map[0]],
                line[map[1]].toEmpty(),
                line[map[2]].toEmpty(),
                line[map[3]].toEmpty(),
                line[map[4]].toEmpty(),
                line[map[5]].toEmpty(),
                line[map[6]].toEmpty(),
                line[map[7]].toEmpty(),
                line[map[8]].toEmpty(),
                line[map[9]].toEmpty()
            )
        }
    }

    private fun readItensHistorico(): Sequence<ItemHistorico> = sequence {
        val itemHistoricoXLS = listOf(3, 8, 13, 4, 9, 10, 15, 12, 22, 11, 16)

        readSpreadsheet(23,itemHistoricoXLS) { line, map ->
            ItemHistorico.of(
                line[map[0]],
                line[map[1]].toInt(),
                line[map[2]].toPeriodo(),
                line[map[2]],
                line[map[3]],
                line[map[4]],
                line[map[5]],
                line[map[6]].toInt(),
                line[map[7]],
                line[map[8]].transform { this.replace(',','.').toFloat() },
                line[map[9]].transform(0) { this.toInt() },
                line[map[10]].transform(0) { this.toInt() })
        }
    }

    private fun readPreRequisitos(): Sequence<PreRequisito> = sequence {
        val preRequisitoXLS = listOf(2, 4, 6)

        readSpreadsheet(12,preRequisitoXLS) { line, map ->
            PreRequisito.of(
                line[map[0]],
                line[map[1]],
                line[map[2]]
            )
        }
    }

    private fun readInscricoes(): Sequence<Inscricao> = sequence {
        val inscricaoXLS = listOf(13, 4, 11, 14, 10, 15, 16, 2, 19, 3)
        val df = LocalDate.Format {
            day()
            char('/')
            monthNumber()
            char('/')
            year()
        }
        val tf = LocalTime.Format {
            hour()
            char(':')
            minute()
            char(':')
            second()
            char(' ')
            amPmMarker(am = "AM", pm = "PM")
        }

        readSpreadsheet(21,inscricaoXLS) { line, map ->
            Inscricao.of(
                line[map[0]],
                line[map[1]],
                line[map[2]],
                line[map[3]].toInt(),
                line[map[4]],
                line[map[5]].toInt(),
                line[map[6]][0].digitToInt(),
                LocalDate.parse(line[map[7]], df),
                LocalTime.parse(line[map[8]], tf),
                line[map[9]].transform { LocalDate.parse(this, df) }
            )
        }
    }

    private fun readDiario(depto: String): Sequence<ItemDiario> = sequence {
        val itemDiarioXLS = listOf(2, 3, 4, 7, 1, 6)

        readSpreadsheet(13,itemDiarioXLS) { line, map ->
            ItemDiario.of(
                line[map[0]],
                line[map[1]],
                line[map[2]].toInt(),
                depto,
                line[map[3]],
                line[map[4]],
                line[map[5]]
            )
        }
    }

    private suspend fun <T> SequenceScope<T>.readSpreadsheet(expectedSize: Int, map: List<Int>,
                                                             block: (List<String>, List<Int>) -> T) {
        spreadSheet.readAsSequence()
            .forEach { line ->

                checkColumnSize(line.size, expectedSize)

                try {
                    yield(block(line, map))
                }
                catch (e: Exception) {
                    println(line.joinToString(", "))
                    throw e
                }
            }
    }

    private fun checkColumnSize(current: Int, expected: Int) {
        if (current != expected) {
            throw IllegalArgumentException("Worksheet should have $expected columns, but it has $current")
        }
    }
}