package services.domain.persistence

import model.EntityDTO
import model.PreRequisito
import kotlin.uuid.Uuid

class PreRequisitoDTO(id: Uuid?, val versao: String, val codigo: String, val codigoPreReq: String): EntityDTO(id) {
    companion object {
        fun fromEntity(preReq: PreRequisito) = PreRequisitoDTO(preReq.id, preReq.versao, preReq.codigo, preReq.codigoPreReq)
    }
}