package services.domain.persistence

import model.ItemDiario

class ItemDiarioRepository: Repository<ItemDiario, IItemDiarioDAO, ItemDiarioDTO>() {

    override val dao: IItemDiarioDAO = DAOFactory.getDAO(DAOFactory.Type.DIARIO)

    override fun createDTO(e: ItemDiario) = ItemDiarioDTO.fromEntity(e)

    override fun createEntity(dto: ItemDiarioDTO) =
        ItemDiario.of(dto.matricula, dto.nome, dto.curso, dto.depto, dto.codigo, dto.versao, dto.turma)
}