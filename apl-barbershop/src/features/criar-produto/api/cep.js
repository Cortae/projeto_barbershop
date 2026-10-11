import axios from "axios"

async function buscarEnderecoPorCep(cep) {
    const response = await axios.get(`https://viacep.com.br/ws/${cep}/json/`)

    if (response.data.erro) return null

    return {
        rua: response.data.logradouro,
        bairro: response.data.bairro,
        cidade: response.data.localidade,
        estado: response.data.uf
    }
}

export {
    buscarEnderecoPorCep
}
