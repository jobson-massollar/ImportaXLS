package services.domain.persistence

import model.DadosAluno

interface IAlunoDAO: IDAO<AlunoDTO> {
    fun updateAddress(data: DadosAluno)
}