package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

object PreRequisitos: Table(name = "pre_requisitos")  {
    val id = uuid("id")
    val versao = varchar("versao", 6)
    val codigo = varchar("codigo", 10)
    val codigoPreReq = varchar("codigo_pre_req", 10)
    override val primaryKey = PrimaryKey(Disciplinas.id)

    init {
        index("IDX_PRE_REQUISITOS_VERSAO_CODIGO_CODIGO_PRE_REQ", isUnique = true, versao, codigo, codigoPreReq)
        index("IDX_PRE_REQUISITOS_VERSAO_CODIGO", isUnique = false, versao, codigo)
    }

}