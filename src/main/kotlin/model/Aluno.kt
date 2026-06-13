package model

import kotlinx.datetime.LocalDate

class Aluno private constructor(val matricula: String, val nome: String, val sexo: Char, val dataNasc: LocalDate?, val versao: String, val ingresso: String, val evasao: String, val dataEvasao: LocalDate?, val logradouro: String = "", val numero: String = "", val complemento: String = "", val bairro: String = "", val cidade: String = "", val cep: String = "", val telefone1: String = "", val telefone2: String = "", val email: String = ""): Entity() {

    companion object {
        fun of(matricula: String, nome: String, sexo: Char, dataNasc: LocalDate?, versao: String, ingresso: String, evasao: String, dataEvasao: LocalDate?, logradouro: String = "", numero: String = "", complemento: String = "", bairro: String = "", cidade: String = "", cep: String = "", telefone1: String = "", telefone2: String = "", email: String = "") =
            Aluno(matricula, nome, sexo, dataNasc, versao, ingresso, evasao, dataEvasao, logradouro, numero, complemento, bairro, cidade, cep, telefone1, telefone2, email)
    }

    override fun toString() = "[id=$id matricula=$matricula, nome=$nome, sexo=$sexo, dtNasc=$dataNasc, versao=$versao, logradouro = $logradouro, numero = $numero, complemento = $complemento, bairro = $bairro, cidade = $cidade, cep = $cep, telefone1 = $telefone1, telefone2 = $telefone2, email = $email, ingresso=$ingresso, evasao=$evasao, dtEvasao=$dataEvasao)"
}