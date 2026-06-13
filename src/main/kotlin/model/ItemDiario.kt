package model

class ItemDiario(val matricula: String, val nome: String, val curso: Int, val depto: String, val codigo: String, val versao: String, val turma: String): Entity() {

    companion object {
        fun of(matricula: String, nome: String, curso: Int, depto: String, codigo: String, versao: String, turma: String) =
            ItemDiario(matricula, nome, curso, depto, codigo, versao, turma)
    }

    override fun toString(): String {
        return "[matricula=$matricula, nome=$nome, curso=$curso, depto=$depto, codigo=$codigo, versao=$versao, turma=$turma]"
    }
}