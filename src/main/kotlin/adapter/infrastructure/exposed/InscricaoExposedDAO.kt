package adapter.infrastructure.exposed

import adapter.infrastructure.exposed.Inscricoes.ano
import adapter.infrastructure.exposed.Inscricoes.codigo
import adapter.infrastructure.exposed.Inscricoes.dataProcessamento
import adapter.infrastructure.exposed.Inscricoes.dataSolicitacao
import adapter.infrastructure.exposed.Inscricoes.descricao
import adapter.infrastructure.exposed.Inscricoes.horaSolicitacao
import adapter.infrastructure.exposed.Inscricoes.matricula
import adapter.infrastructure.exposed.Inscricoes.periodo
import adapter.infrastructure.exposed.Inscricoes.situacao
import adapter.infrastructure.exposed.Inscricoes.turma
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import services.domain.persistence.IDAO.IInscricaoDAO
import services.domain.persistence.InscricaoDTO

class InscricaoExposedDAO: IInscricaoDAO {
    override fun insert(dto: InscricaoDTO) {
        transaction {
            Inscricoes.insert {
                it[Inscricoes.id] = dto.id!!
                it[matricula] = dto.matricula
                it[codigo] = dto.codigo
                it[turma] = dto.turma
                it[situacao] = dto.situacao
                it[descricao] = dto.descricao
                it[ano] = dto.ano
                it[periodo] = dto.periodo
                it[dataSolicitacao] = dto.dataSolicitacao
                it[horaSolicitacao] = dto.horaSolicitacao
                it[dataProcessamento] = dto.dataProcessamento
            }
        }
    }

    override fun update(dto: InscricaoDTO) {
        transaction {
            Inscricoes.update({ Inscricoes.id eq dto.id!! }) {
                it[matricula] = dto.matricula
                it[codigo] = dto.codigo
                it[turma] = dto.turma
                it[situacao] = dto.situacao
                it[descricao] = dto.descricao
                it[ano] = dto.ano
                it[periodo] = dto.periodo
                it[dataSolicitacao] = dto.dataSolicitacao
                it[horaSolicitacao] = dto.horaSolicitacao
                it[dataProcessamento] = dto.dataProcessamento
            }
        }
    }

    override fun delete(dto: InscricaoDTO) {
        transaction {
            Inscricoes.deleteWhere { Inscricoes.id eq dto.id!! }
        }
    }

    override fun findAll(): List<InscricaoDTO> =
        transaction {
            Inscricoes.selectAll().map {
                InscricaoDTO(it[Inscricoes.id],
                    it[matricula],
                    it[codigo],
                    it[turma],
                    it[situacao],
                    it[descricao],
                    it[ano],
                    it[periodo],
                    it[dataSolicitacao],
                    it[horaSolicitacao],
                    it[dataProcessamento])
            }.toList()
        }

    override fun deleteAll() {
        transaction {
            Inscricoes.deleteAll()
        }
    }

    override fun batchInsert(dtos: List<InscricaoDTO>) {
        transaction {
            Inscricoes.batchInsert(
                data = dtos,
                shouldReturnGeneratedValues = false)
                {dto ->
                    this[Inscricoes.id] = dto.id!!
                    this[matricula] = dto.matricula
                    this[codigo] = dto.codigo
                    this[turma] = dto.turma
                    this[situacao] = dto.situacao
                    this[descricao] = dto.descricao
                    this[ano] = dto.ano
                    this[periodo] = dto.periodo
                    this[dataSolicitacao] = dto.dataSolicitacao
                    this[horaSolicitacao] = dto.horaSolicitacao
                    this[dataProcessamento] = dto.dataProcessamento
                }
        }
    }
}