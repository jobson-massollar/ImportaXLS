package services.domain.persistence

import model.DadosAluno
import model.EntityDTO

sealed interface IDAO<T: EntityDTO> {
    fun insert(dto: T)
    fun update(dto: T)
    fun delete(dto: T)
    fun findAll(): List<T>
    fun deleteAll()
    fun batchInsert(dtos: List<T>)

    interface IAlunoDAO: IDAO<AlunoDTO> {
        fun updateAddress(data: DadosAluno)
    }
    interface IDisciplinaDAO: IDAO<DisciplinaDTO>
    interface IPreRequisitoDAO: IDAO<PreRequisitoDTO>
    interface IInscricaoDAO: IDAO<InscricaoDTO>
    interface IItemDiarioDAO: IDAO<ItemDiarioDTO>
    interface IItemHistoricoDAO: IDAO<ItemHistoricoDTO>
}

interface IDataDAO {
    fun runProc(storedProc: String)
}