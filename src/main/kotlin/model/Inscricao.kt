package model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class Inscricao(val matricula: String, val codigo: String, val turma: String, val situacao: Int, val descricao: String, val ano: Int, val periodo: Int, val dataSolicitacao: LocalDate, val horaSolicitacao: LocalTime, val dataProcessamento: LocalDate?):
    Entity() {

    companion object {
        fun of(matricula: String, codigo: String, turma: String, situacao: Int, descricao: String, ano: Int, periodo: Int, dataSolicitacao: LocalDate, horaSolicitacao: LocalTime, dataProcessamento: LocalDate?) =
            Inscricao(matricula, codigo, turma, situacao, descricao, ano, periodo, dataSolicitacao, horaSolicitacao, dataProcessamento)
    }
}