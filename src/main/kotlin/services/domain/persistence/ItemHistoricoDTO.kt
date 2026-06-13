package services.domain.persistence

import model.EntityDTO
import model.ItemHistorico
import kotlin.uuid.Uuid

class ItemHistoricoDTO(id: Uuid?, val matricula: String, val ano: Int, val periodo: Int, val descPeriodo: String, val versao: String, val codigo: String, val nome: String, val situacao: Int, val descricao: String, val nota: Float?, val creditos: Int, val horas: Int): EntityDTO(id) {

    companion object {
        fun fromEntity(h: ItemHistorico) = ItemHistoricoDTO(h.id, h.matricula, h.ano, h.periodo, h.descPeriodo, h.versao, h.codigo, h.nome, h.situacao, h.descricao, h.nota, h.creditos, h.horas)
    }
}