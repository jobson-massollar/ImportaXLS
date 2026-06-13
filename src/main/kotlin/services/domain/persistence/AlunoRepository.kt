package services.domain.persistence

import model.Aluno
import model.DadosAluno

class AlunoRepository: Repository<Aluno, IAlunoDAO, AlunoDTO>() {

    override val dao: IAlunoDAO = DAOFactory.getDAO(DAOFactory.Type.ALUNO)

    override fun createDTO(e: Aluno) = AlunoDTO.fromEntity(e)

    override fun createEntity(dto: AlunoDTO) = Aluno.of(dto.matricula, dto.nome, dto.sexo, dto.dataNasc, dto.versao, dto.ingresso, dto.evasao, dto.dataEvasao, dto.logradouro, dto.numero, dto.complemento, dto.bairro, dto.cidade, dto.cep, dto.telefone1, dto.telefone2, dto.email)

    fun updateAluno(data: DadosAluno) {
        dao.updateAddress(data)
    }
}