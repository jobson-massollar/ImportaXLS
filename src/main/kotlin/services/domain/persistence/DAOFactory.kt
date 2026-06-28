package services.domain.persistence

import kotlin.reflect.KClass

interface IDAOFactory {
    fun <T:IDAO<*>> get(c: KClass<in T>): T
    fun <T : IDAO<*>> getDAO(t: DAOFactory.Type): T
}

object DAOFactory: IDAOFactory {

    enum class Type { DISCIPLINA, ALUNO, HISTORICO, INSCRICAO, DIARIO, PRE_REQUISITO }
    private var factory: IDAOFactory? = null

    fun register(factory: IDAOFactory) {
        this.factory = factory
    }

    override fun <T : IDAO<*>> get(c: KClass<in T>): T =
        factory?.get(c) ?: throw NullPointerException("DAO Factory not set")

    override fun <T : IDAO<*>> getDAO(t: Type): T  =
        factory?.getDAO(t) ?: throw NullPointerException("DAO Factory not set")
}