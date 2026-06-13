package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.date

object Alunos: Table(name = "alunos") {
    val id = uuid("id")
    val matricula = varchar("matricula", 14).uniqueIndex()
    val nome = varchar("nome", 100)
    val sexo = char("sexo")
    val dataNasc = date("dt_nasc").nullable()
    val versao = varchar("versao", 6)
    val logradouro = varchar("logradouro", 100)
    val numero = varchar("numero", 10)
    val complemento = varchar("complemento", 60)
    val bairro = varchar("bairro", 60)
    val cidade = varchar("cidade", 60)
    val cep = varchar("cep", 10)
    val telefone1 = varchar("telefone1", 20)
    val telefone2 = varchar("telefone2", 20)
    val email = varchar("email", 40).default("")
    val ingresso = varchar("ingresso", 100)
    val evasao = varchar("evasao", 100)
    val dataEvasao = date("dt_evasao").nullable()
    override val primaryKey = PrimaryKey(Alunos.id)
}