package services.domain.persistence

import model.ItemHistorico

class ItemHistoricoRepository: Repository<ItemHistorico, IItemHistoricoDAO, ItemHistoricoDTO>() {

    override val dao: IItemHistoricoDAO = DAOFactory.getDAO(DAOFactory.Type.HISTORICO)

    override fun createDTO(e: ItemHistorico) = ItemHistoricoDTO.fromEntity(e)

    override fun createEntity(dto: ItemHistoricoDTO) = ItemHistorico.of(dto.matricula, dto. ano, dto.periodo, dto.descPeriodo, dto.versao, dto.codigo, dto.nome, dto.situacao, dto.descricao, dto.nota, dto.creditos, dto.horas)
}