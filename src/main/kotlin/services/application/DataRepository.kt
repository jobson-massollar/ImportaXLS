package services.application

import services.domain.persistence.DAOFactory

class DataRepository {

    val dao = DAOFactory.getDataDAO();

    fun runProc(storedProc: String) = dao.runProc(storedProc)
}