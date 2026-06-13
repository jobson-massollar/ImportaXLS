package services.domain.persistence

import model.Disciplina
import model.EntityDTO
import kotlin.uuid.Uuid

class DisciplinaDTO(id: Uuid?, val versao: String, val codigo: String, val nome: String, val periodo: Int, val creditos: Int, val horas: Int, val tipo: String, val situacao: String, val aula: String): EntityDTO(id) {
    companion object {
        fun fromEntity(d: Disciplina) = DisciplinaDTO(d.id, d.versao, d.codigo, d.nome, d.periodo, d.creditos, d.horas, d.tipo, d.situacao, d.aula)
    }
}