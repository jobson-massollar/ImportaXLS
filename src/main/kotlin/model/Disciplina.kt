package model

class Disciplina private constructor(val versao: String, val codigo: String, val nome: String, val periodo: Int, val creditos: Int, val horas: Int, val tipo: String, val situacao: String, val aula: String): Entity() {
    companion object {
        fun of(versao: String, codigo: String, nome: String, periodo: Int, creditos: Int, horas: Int, tipo: String, situacao: String, aula: String) =
            Disciplina(versao, codigo, nome, periodo, creditos, horas, tipo, situacao, aula)
    }

    override fun toString(): String = "[id=$id versao=$versao codigo=$codigo nome=$nome periodo=$periodo creditos=$creditos horas=$horas tipo=$tipo situacao=$situacao]"
}