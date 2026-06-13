package adapter.infrastructure.exposed

import adapter.infrastructure.exposed.Disciplinas.versao
import adapter.infrastructure.exposed.Disciplinas.codigo
import adapter.infrastructure.exposed.Disciplinas.nome
import adapter.infrastructure.exposed.Disciplinas.periodo
import adapter.infrastructure.exposed.Disciplinas.creditos
import adapter.infrastructure.exposed.Disciplinas.horas
import adapter.infrastructure.exposed.Disciplinas.tipo
import adapter.infrastructure.exposed.Disciplinas.situacao
import adapter.infrastructure.exposed.Disciplinas.aula
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import services.domain.persistence.DisciplinaDTO
import services.domain.persistence.IDisciplinaDAO

class DisciplinaExposedDAO: IDisciplinaDAO {
    override fun insert(dto: DisciplinaDTO) {
        transaction {
            Disciplinas.insert {
                it[Disciplinas.id] = dto.id!!
                it[versao] = dto.versao
                it[codigo] = dto.codigo
                it[nome] = dto.nome
                it[periodo] = dto.periodo
                it[creditos] = dto.creditos
                it[horas] = dto.horas
                it[tipo] = dto.tipo
                it[situacao] = dto.situacao
                it[aula] = dto.aula
            }
        }
    }

    override fun update(dto: DisciplinaDTO) {
        transaction {
            Disciplinas.update({ Disciplinas.id eq dto.id!! }) {
                it[versao] = dto.versao
                it[codigo] = dto.codigo
                it[nome] = dto.nome
                it[periodo] = dto.periodo
                it[creditos] = dto.creditos
                it[horas] = dto.horas
                it[tipo] = dto.tipo
                it[situacao] = dto.situacao
                it[aula] = dto.aula
            }
        }
    }

    override fun delete(dto: DisciplinaDTO) {
        transaction {
            Disciplinas.deleteWhere { Disciplinas.id eq dto.id!! }
        }
    }

    override fun findAll(): List<DisciplinaDTO> =
        transaction {
            Disciplinas.selectAll().map {
                DisciplinaDTO(it[Disciplinas.id],
                    it[versao],
                    it[codigo],
                    it[nome],
                    it[periodo],
                    it[creditos],
                    it[horas],
                    it[tipo],
                    it[situacao],
                    it[aula])
            }.toList()
        }

    override fun deleteAll() {
        transaction {
            Disciplinas.deleteAll()
        }
    }

    override fun batchInsert(dtos: List<DisciplinaDTO>) {
        transaction {
            Disciplinas.batchInsert(
                data = dtos,
                shouldReturnGeneratedValues = false)
                {dto ->
                    this[Disciplinas.id] = dto.id!!
                    this[versao] = dto.versao
                    this[codigo] = dto.codigo
                    this[nome] = dto.nome
                    this[periodo] = dto.periodo
                    this[creditos] = dto.creditos
                    this[horas] = dto.horas
                    this[tipo] = dto.tipo
                    this[situacao] = dto.situacao
                    this[aula] = dto.aula
                }
        }
    }
}