package adapter.infrastructure.exposed

import adapter.infrastructure.exposed.Alunos.bairro
import adapter.infrastructure.exposed.Alunos.cep
import adapter.infrastructure.exposed.Alunos.cidade
import adapter.infrastructure.exposed.Alunos.complemento
import adapter.infrastructure.exposed.Alunos.dataEvasao
import adapter.infrastructure.exposed.Alunos.dataNasc
import adapter.infrastructure.exposed.Alunos.email
import adapter.infrastructure.exposed.Alunos.evasao
import adapter.infrastructure.exposed.Alunos.ingresso
import adapter.infrastructure.exposed.Alunos.logradouro
import adapter.infrastructure.exposed.Alunos.matricula
import adapter.infrastructure.exposed.Alunos.nome
import adapter.infrastructure.exposed.Alunos.numero
import adapter.infrastructure.exposed.Alunos.sexo
import adapter.infrastructure.exposed.Alunos.telefone1
import adapter.infrastructure.exposed.Alunos.telefone2
import adapter.infrastructure.exposed.Alunos.versao
import services.domain.persistence.AlunoDTO
import model.DadosAluno
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import services.domain.persistence.IAlunoDAO

class AlunoExposedDAO: IAlunoDAO {
    override fun insert(dto: AlunoDTO) {
        transaction {
            Alunos.insert {
                it[Alunos.id] = dto.id!!
                it[matricula] = dto.matricula
                it[nome] = dto.nome
                it[sexo] = dto.sexo
                it[dataNasc] = dto.dataNasc
                it[versao] = dto.versao
                it[ingresso] = dto.ingresso
                it[evasao] = dto.evasao
                it[dataEvasao] = dto.dataEvasao
                it[logradouro] = dto.logradouro
                it[numero] = dto.numero
                it[complemento] = dto.complemento
                it[bairro] = dto.bairro
                it[cidade] = dto.cidade
                it[cep] = dto.cep
                it[telefone1] = dto.telefone1
                it[telefone2] = dto.telefone2
                it[email] = dto.email
            }
        }
    }

    override fun update(dto: AlunoDTO) {
        transaction {
            Alunos.update({ Alunos.id eq dto.id!! }) {
                it[matricula] = dto.matricula
                it[nome] = dto.nome
                it[sexo] = dto.sexo
                it[dataNasc] = dto.dataNasc
                it[versao] = dto.versao
                it[ingresso] = dto.ingresso
                it[evasao] = dto.evasao
                it[dataEvasao] = dto.dataEvasao
                it[logradouro] = dto.logradouro
                it[numero] = dto.numero
                it[complemento] = dto.complemento
                it[bairro] = dto.bairro
                it[cidade] = dto.cidade
                it[cep] = dto.cep
                it[telefone1] = dto.telefone1
                it[telefone2] = dto.telefone2
                it[email] = dto.email
            }
        }
    }

    override fun delete(dto: AlunoDTO) {
        transaction {
            Alunos.deleteWhere { Alunos.id eq dto.id!! }
        }
    }

    override fun findAll(): List<AlunoDTO> =
        transaction {
            Alunos.selectAll().map {
                AlunoDTO(
                    it[Alunos.id],
                    it[matricula],
                    it[nome],
                    it[sexo],
                    it[dataNasc],
                    it[versao],
                    it[ingresso],
                    it[evasao],
                    it[dataEvasao],
                    it[logradouro],
                    it[numero],
                    it[complemento],
                    it[bairro],
                    it[cidade],
                    it[cep],
                    it[telefone1],
                    it[telefone2],
                    it[email]
                )
            }.toList()
        }

    override fun deleteAll() {
        transaction {
            Alunos.deleteAll()
        }
    }

    override fun batchInsert(dtos: List<AlunoDTO>) {
        transaction {
            Alunos.batchInsert(
                data = dtos,
                shouldReturnGeneratedValues = false)
                {dto ->
                    this[Alunos.id] = dto.id!!
                    this[matricula] = dto.matricula
                    this[nome] = dto.nome
                    this[sexo] = dto.sexo
                    this[dataNasc] = dto.dataNasc
                    this[versao] = dto.versao
                    this[ingresso] = dto.ingresso
                    this[evasao] = dto.evasao
                    this[dataEvasao] = dto.dataEvasao
                    this[logradouro] = dto.logradouro
                    this[numero] = dto.numero
                    this[complemento] = dto.complemento
                    this[bairro] = dto.bairro
                    this[cidade] = dto.cidade
                    this[cep] = dto.cep
                    this[telefone1] = dto.telefone1
                    this[telefone2] = dto.telefone2
                    this[email] = dto.email
                }
        }
    }

    override fun updateAddress(data: DadosAluno) {
        transaction {
            Alunos.update({ Alunos.matricula eq data.matricula }) {
                it[logradouro] = data.logradouro
                it[numero] = data.numero
                it[complemento] = data.complemento
                it[bairro] = data.bairro
                it[cidade] = data.cidade
                it[cep] = data.cep
                it[telefone1] = data.telefone1
                it[telefone2] = data.telefone2
                it[email] = data.email
            }
        }
    }
}