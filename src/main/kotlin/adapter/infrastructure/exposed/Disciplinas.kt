package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

object Disciplinas: Table(name = "disciplinas")  {
    val id = uuid("id")
    val versao = varchar("versao", 6)
    val codigo = varchar("codigo", 10).index()
    val nome = varchar("nome", 100)
    val periodo = integer("periodo")
    val creditos = integer("creditos")
    val horas = integer("horas")
    val tipo = varchar("tipo", 60)
    val situacao = varchar("situacao", 20)
    val aula = varchar("aula", 50)
    override val primaryKey = PrimaryKey(Disciplinas.id)

    init {
        index("IDX_DISCIPLINA_CODIGO_VERSAO_AULA", isUnique = true, codigo, versao, aula)
    }
}