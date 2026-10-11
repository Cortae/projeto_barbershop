function validarNome(valor) {
    if (typeof valor != "string" || valor.trim() === "") {
        return {
            erro: true,
            mensagem: "Nome é obrigatório"
        }
    }

    return {
        erro: false,
        mensagem: ""
    }
}

function validarPreco(valor) {
    const numero = Number(valor)

    if (valor === "" || valor === null || valor === undefined || Number.isNaN(numero)) {
        return {
            erro: true,
            mensagem: "Preço é obrigatório e deve ser um número"
        }
    }

    if (numero <= 0) {
        return {
            erro: true,
            mensagem: "Preço deve ser maior que zero"
        }
    }

    return {
        erro: false,
        mensagem: ""
    }
}

function validarEstoque(valor) {
    const numero = Number(valor)

    if (valor === "" || valor === null || valor === undefined || Number.isNaN(numero)) {
        return {
            erro: true,
            mensagem: "Estoque é obrigatório e deve ser um número"
        }
    }

    if (!Number.isInteger(numero)) {
        return {
            erro: true,
            mensagem: "Estoque deve ser um número inteiro"
        }
    }

    if (numero < 0) {
        return {
            erro: true,
            mensagem: "Estoque não pode ser negativo"
        }
    }

    return {
        erro: false,
        mensagem: ""
    }
}

function validarCategoria(valor) {
    if (typeof valor != "string" || valor.trim() === "") {
        return {
            erro: true,
            mensagem: "Categoria é obrigatória"
        }
    }

    return {
        erro: false,
        mensagem: ""
    }
}

function validarCampos(produto) {
    return {
        nome: validarNome(produto.nome),
        preco: validarPreco(produto.preco),
        estoque: validarEstoque(produto.estoque),
        categoria: validarCategoria(produto.categoria),
    }
}


export {
    validarCampos
}
