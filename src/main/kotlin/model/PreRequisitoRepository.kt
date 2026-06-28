package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO.IPreRequisitoDAO
import services.domain.persistence.PreRequisitoDTO

class PreRequisitoRepository: Repository<PreRequisito, IPreRequisitoDAO, PreRequisitoDTO>() {

    override val dao: IPreRequisitoDAO = DAOFactory.getDAO(DAOFactory.Type.PRE_REQUISITO)

    override fun createDTO(e: PreRequisito) = PreRequisitoDTO.fromEntity(e)

    override fun createEntity(dto: PreRequisitoDTO) = PreRequisito.of(dto.versao, dto.codigo, dto.codigoPreReq)
}