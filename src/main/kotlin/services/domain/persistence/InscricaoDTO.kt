package services.domain.persistence

import model.EntityDTO
import model.Inscricao
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.uuid.Uuid

class InscricaoDTO(id: Uuid?, val matricula: String, val codigo: String, val turma: String, val situacao: Int, val descricao: String, val ano: Int, val periodo: Int, val dataSolicitacao: LocalDate, val horaSolicitacao: LocalTime, val dataProcessamento: LocalDate?): EntityDTO(id) {

    companion object {
        fun fromEntity(i: Inscricao) = InscricaoDTO(i.id, i.matricula, i.codigo, i.turma, i.situacao, i.descricao, i.ano, i.periodo, i.dataSolicitacao, i.horaSolicitacao, i.dataProcessamento)
    }
}