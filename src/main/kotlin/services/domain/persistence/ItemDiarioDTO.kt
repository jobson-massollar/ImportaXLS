package services.domain.persistence

import model.EntityDTO
import model.ItemDiario
import kotlin.uuid.Uuid

class ItemDiarioDTO(id: Uuid?, val matricula: String, val nome: String, val curso: Int, val depto: String, val codigo: String, val versao: String, val turma: String): EntityDTO(id) {

    companion object {
        fun fromEntity(i: ItemDiario) = ItemDiarioDTO(i.id, i.matricula, i.nome, i.curso, i.depto, i.codigo, i.versao, i.turma)
    }
}