package services.domain.persistence

import model.Inscricao

class InscricaoRepository: Repository<Inscricao, IInscricaoDAO, InscricaoDTO>() {

    override val dao: IInscricaoDAO = DAOFactory.getDAO(DAOFactory.Type.INSCRICAO)

    override fun createDTO(e: Inscricao) = InscricaoDTO.fromEntity(e)

    override fun createEntity(dto: InscricaoDTO) = Inscricao.of(dto.matricula, dto.codigo, dto.turma, dto.situacao, dto.descricao, dto.ano, dto.periodo, dto.dataSolicitacao, dto.horaSolicitacao, dto.dataProcessamento)
}