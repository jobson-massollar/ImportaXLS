package adapter.infrastructure.exposed

import services.domain.persistence.*
import kotlin.reflect.KClass

object ExposedDAOFactory : IDAOFactory {
    override fun <T : IDAO<*>> get(c: KClass<in T>): T =
        when (c) {
            IDAO.IDisciplinaDAO::class -> DisciplinaExposedDAO() as T
            IDAO.IAlunoDAO::class -> AlunoExposedDAO() as T
            IDAO.IItemHistoricoDAO::class -> ItemHistoricoExposedDAO() as T
            IDAO.IInscricaoDAO::class -> InscricaoExposedDAO() as T
            IDAO.IItemDiarioDAO::class -> ItemDiarioExposedDAO() as T
            IDAO.IPreRequisitoDAO::class -> PreRequisitoExposedDAO() as T
            else -> throw IllegalArgumentException("Invalid DAO class " + c.java.name)
        }

    override fun <T : IDAO<*>> getDAO(t: DAOFactory.Type): T =
        when (t) {
            DAOFactory.Type.DISCIPLINA -> DisciplinaExposedDAO() as T
            DAOFactory.Type.ALUNO -> AlunoExposedDAO() as T
            DAOFactory.Type.HISTORICO -> ItemHistoricoExposedDAO() as T
            DAOFactory.Type.INSCRICAO -> InscricaoExposedDAO() as T
            DAOFactory.Type.DIARIO -> ItemDiarioExposedDAO() as T
            DAOFactory.Type.PRE_REQUISITO -> PreRequisitoExposedDAO() as T
        }
}