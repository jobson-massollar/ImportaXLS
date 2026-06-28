package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import services.domain.persistence.IDAO.IPreRequisitoDAO
import services.domain.persistence.PreRequisitoDTO

class PreRequisitoExposedDAO: IPreRequisitoDAO {
    override fun insert(dto: PreRequisitoDTO) {
        transaction {
            PreRequisitos.insert {
                it[PreRequisitos.id] = dto.id!!
                it[PreRequisitos.versao] = dto.versao
                it[PreRequisitos.codigo] = dto.codigo
                it[PreRequisitos.codigoPreReq] = dto.codigoPreReq
            }
        }
    }

    override fun update(dto: PreRequisitoDTO) {
        transaction {
            PreRequisitos.update({ PreRequisitos.id eq dto.id!! }) {
                it[PreRequisitos.versao] = dto.versao
                it[PreRequisitos.codigo] = dto.codigo
                it[PreRequisitos.codigoPreReq] = dto.codigoPreReq
            }
        }
    }

    override fun delete(dto: PreRequisitoDTO) {
        transaction {
            PreRequisitos.deleteWhere { PreRequisitos.id eq dto.id!! }
        }
    }

    override fun findAll(): List<PreRequisitoDTO> =
        transaction {
            PreRequisitos.selectAll().map {
                PreRequisitoDTO(it[PreRequisitos.id],
                    it[PreRequisitos.versao],
                    it[PreRequisitos.codigo],
                    it[PreRequisitos.codigoPreReq])
            }
        }

    override fun deleteAll() {
        transaction {
            PreRequisitos.deleteAll()
        }
    }

    override fun batchInsert(dtos: List<PreRequisitoDTO>) {
        transaction {
            PreRequisitos.batchInsert(
                data = dtos,
                shouldReturnGeneratedValues = false)
                { dto ->
                    this[PreRequisitos.id] = dto.id!!
                    this[PreRequisitos.versao] = dto.versao
                    this[PreRequisitos.codigo] = dto.codigo
                    this[PreRequisitos.codigoPreReq] = dto.codigoPreReq
                }
        }
    }
}