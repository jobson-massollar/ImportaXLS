package adapter.infrastructure.exposed

import adapter.infrastructure.exposed.ItensDiario.codigo
import adapter.infrastructure.exposed.ItensDiario.curso
import adapter.infrastructure.exposed.ItensDiario.depto
import adapter.infrastructure.exposed.ItensDiario.matricula
import adapter.infrastructure.exposed.ItensDiario.nome
import adapter.infrastructure.exposed.ItensDiario.turma
import adapter.infrastructure.exposed.ItensDiario.versao
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import services.domain.persistence.IDAO.IItemDiarioDAO
import services.domain.persistence.ItemDiarioDTO

class ItemDiarioExposedDAO: IItemDiarioDAO {
    override fun insert(dto: ItemDiarioDTO) {
        transaction {
            ItensDiario.insert {
                it[ItensDiario.id] = dto.id!!
                it[matricula] = dto.matricula
                it[nome] = dto.nome
                it[curso] = dto.curso
                it[depto] = dto.depto
                it[codigo] = dto.codigo
                it[versao] = dto.versao
                it[turma] = dto.turma
            }
        }
    }

    override fun update(dto: ItemDiarioDTO) {
        ItensDiario.update({ ItensDiario.id eq dto.id!! }) {
            it[matricula] = dto.matricula
            it[nome] = dto.nome
            it[curso] = dto.curso
            it[depto] = dto.depto
            it[codigo] = dto.codigo
            it[versao] = dto.versao
            it[turma] = dto.turma
        }
    }

    override fun delete(dto: ItemDiarioDTO) {
        transaction {
            ItensDiario.deleteWhere { ItensDiario.id eq dto.id!! }
        }
    }

    override fun findAll(): List<ItemDiarioDTO> =
        transaction {
            ItensDiario.selectAll().map {
                ItemDiarioDTO(
                    it[ItensDiario.id],
                    it[matricula],
                    it[nome],
                    it[curso],
                    it[depto],
                    it[codigo],
                    it[versao],
                    it[turma]
                )
            }.toList()
        }

    override fun deleteAll() {
        transaction {
            ItensDiario.deleteAll()
        }
    }

    override fun batchInsert(dtos: List<ItemDiarioDTO>) {
        transaction {
            ItensDiario.batchInsert(
                data = dtos,
                shouldReturnGeneratedValues = false)
                {dto ->
                    this[ItensDiario.id] = dto.id!!
                    this[matricula] = dto.matricula
                    this[nome] = dto.nome
                    this[curso] = dto.curso
                    this[depto] = dto.depto
                    this[codigo] = dto.codigo
                    this[versao] = dto.versao
                    this[turma] = dto.turma
                }
        }
    }
}