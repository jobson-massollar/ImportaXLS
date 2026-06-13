package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

object Departamentos: Table() {
    val id = uuid("id")
    val depto = varchar("depto", 5).uniqueIndex()
    val nome = varchar("nome", 100)
    override val primaryKey = PrimaryKey(Departamentos.id)
}