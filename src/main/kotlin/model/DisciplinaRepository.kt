package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.DisciplinaDTO
import services.domain.persistence.IDAO.IDisciplinaDAO

class DisciplinaRepository: Repository<Disciplina, IDisciplinaDAO, DisciplinaDTO>() {

    override val dao: IDisciplinaDAO = DAOFactory.getDAO(DAOFactory.Type.DISCIPLINA)

    override fun createDTO(e: Disciplina) = DisciplinaDTO.fromEntity(e)

    override fun createEntity(dto: DisciplinaDTO) = Disciplina.of(dto.versao, dto.codigo, dto.nome, dto.periodo, dto.creditos, dto.horas, dto.tipo, dto.situacao, dto.aula)
}