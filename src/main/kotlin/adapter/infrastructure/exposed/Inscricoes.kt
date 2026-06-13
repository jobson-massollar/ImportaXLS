package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time

object Inscricoes: Table(name = "inscricoes") {
    val id = uuid("id")
    val matricula = varchar("matricula", 14).index()
    val codigo = varchar("codigo", 10).index()
    val turma = varchar("turma", 10)
    val situacao = integer("situacao").index()
    val descricao = varchar("descricao", 50).index()
    val ano = integer("ano")
    val periodo = integer("periodo")
    val dataSolicitacao = date("dt_solicitacao")
    val horaSolicitacao = time("hora_solicitacao")
    val dataProcessamento = date("dt_processamento").nullable()
    override val primaryKey = PrimaryKey(Inscricoes.id)
}