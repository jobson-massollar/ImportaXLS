package services.domain.persistence

import model.EntityDTO
import kotlin.reflect.KClass

interface IDAO<T: EntityDTO> {
    fun insert(dto: T)
    fun update(dto: T)
    fun delete(dto: T)
    fun findAll(): List<T>
    fun deleteAll()
    fun batchInsert(dtos: List<T>)
}

interface IDAOFactory {
    fun <T:IDAO<*>> get(c: KClass<in T>): T
    fun <T : IDAO<*>> getDAO(t: DAOFactory.Type): T
}

object DAOFactory: IDAOFactory {

    enum class Type { DISCIPLINA, ALUNO, HISTORICO, INSCRICAO, DIARIO }
    private var factory: IDAOFactory? = null

    fun register(factory: IDAOFactory) {
        this.factory = factory
    }

    override fun <T : IDAO<*>> get(c: KClass<in T>): T =
        factory?.get(c) ?: throw NullPointerException("DAO Factory not set")

    override fun <T : IDAO<*>> getDAO(t: Type): T  =
        factory?.getDAO(t) ?: throw NullPointerException("DAO Factory not set")
}