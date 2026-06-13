package adapter.infrastructure.exposed

import adapter.infrastructure.exposed.Disciplinas.creditos
import adapter.infrastructure.exposed.Disciplinas.horas
import adapter.infrastructure.exposed.ItensHistorico.ano
import adapter.infrastructure.exposed.ItensHistorico.codigo
import adapter.infrastructure.exposed.ItensHistorico.descPeriodo
import adapter.infrastructure.exposed.ItensHistorico.descricao
import adapter.infrastructure.exposed.ItensHistorico.matricula
import adapter.infrastructure.exposed.ItensHistorico.nome
import adapter.infrastructure.exposed.ItensHistorico.nota
import adapter.infrastructure.exposed.ItensHistorico.periodo
import adapter.infrastructure.exposed.ItensHistorico.situacao
import adapter.infrastructure.exposed.ItensHistorico.versao
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import services.domain.persistence.IItemHistoricoDAO
import services.domain.persistence.ItemHistoricoDTO

class ItemHistoricoExposedDAO: IItemHistoricoDAO{
    override fun insert(dto: ItemHistoricoDTO) {
        transaction {
            ItensHistorico.insert {
                it[ItensHistorico.id] = dto.id!!
                it[matricula] = dto.matricula
                it[ano] = dto.ano
                it[periodo] = dto.periodo
                it[descPeriodo] = dto.descPeriodo
                it[versao] = dto.versao
                it[codigo] = dto.codigo
                it[nome] = dto.nome
                it[situacao] = dto.situacao
                it[descricao] = dto.descricao
                it[nota] = dto.nota
                it[creditos] = dto.creditos
                it[horas] = dto.horas
            }
        }
    }

    override fun update(dto: ItemHistoricoDTO) {
        transaction {
            ItensHistorico.update({ ItensHistorico.id eq dto.id!! }) {
                it[ItensHistorico.matricula] = dto.matricula
                it[ano] = dto.ano
                it[periodo] = dto.periodo
                it[descPeriodo] = dto.descPeriodo
                it[versao] = dto.versao
                it[codigo] = dto.codigo
                it[nome] = dto.nome
                it[situacao] = dto.situacao
                it[descricao] = dto.descricao
                it[nota] = dto.nota
                it[creditos] = dto.creditos
                it[horas] = dto.horas
            }
        }
    }

    override fun delete(dto: ItemHistoricoDTO) {
        transaction {
            ItensHistorico.deleteWhere { ItensHistorico.id eq dto.id!! }
        }
    }

    override fun findAll(): List<ItemHistoricoDTO> =
        transaction {
            ItensHistorico.selectAll().map {
                ItemHistoricoDTO(it[ItensHistorico.id],
                    it[matricula],
                    it[ano],
                    it[periodo],
                    it[descPeriodo],
                    it[versao],
                    it[codigo],
                    it[nome],
                    it[situacao],
                    it[descricao],
                    it[nota],
                    it[creditos],
                    it[horas])
            }.toList()
        }

    override fun deleteAll() {
        transaction {
            ItensHistorico.deleteAll()
        }
    }

    override fun batchInsert(dtos: List<ItemHistoricoDTO>) {
        transaction {
            ItensHistorico.batchInsert(
                data = dtos,
                shouldReturnGeneratedValues = false)
                {dto ->
                    this[ItensHistorico.id] = dto.id!!
                    this[matricula] = dto.matricula
                    this[ano] = dto.ano
                    this[periodo] = dto.periodo
                    this[descPeriodo] = dto.descPeriodo
                    this[versao] = dto.versao
                    this[codigo] = dto.codigo
                    this[nome] = dto.nome
                    this[situacao] = dto.situacao
                    this[descricao] = dto.descricao
                    this[nota] = dto.nota
                    this[creditos] = dto.creditos
                    this[horas] = dto.horas
                }
        }
    }
}