package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import services.domain.persistence.IDataDAO

class ExposedDataDAO : IDataDAO {

    override fun runProc(storedProc: String) {
        transaction {
            exec("call ${storedProc}()")
        }
    }
}