package model

import services.domain.persistence.PreRequisitoDTO

class PreRequisito private constructor(val versao: String, val codigo: String, val codigoPreReq: String): Entity() {

    companion object {
        fun of(versao: String, codigo: String, codigoPreReq: String) = PreRequisito(versao, codigo, codigoPreReq)
    }

    override fun toString(): String = "[id=$id versao=$versao codigo=$codigo codigoPreReq=$codigoPreReq]"
}