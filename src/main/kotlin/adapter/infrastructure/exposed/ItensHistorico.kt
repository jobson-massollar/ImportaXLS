package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

object ItensHistorico: Table(name = "itens_historico")  {
    val id = uuid("id")
    val matricula = varchar("matricula", 14).index()
    val ano = integer("ano")
    val periodo = integer("periodo")
    val descPeriodo = varchar("desc_periodo", 20)
    val versao = varchar("versao", 6)
    val codigo = varchar("codigo", 10).index()
    val nome = varchar("nome", 200)
    val situacao = integer("situacao")
    val descricao = varchar("descricao", 50)
    val nota = float("nota").nullable()
    val creditos = integer("creditos")
    val horas = integer("horas")
    override val primaryKey = PrimaryKey(ItensHistorico.id)
}