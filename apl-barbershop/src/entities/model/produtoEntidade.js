export function ProdutoEntidade(produto){

    if(typeof produto.nome != "string" || produto.nome.trim() === ""){
        throw Error("Camada entidade: Nome deve ser um texto não vazio")
    }

    if(typeof produto.preco != "number"){
        throw Error("Camada entidade: Preço deve ser um numero")
    }

    if(typeof produto.estoque != "number"){
        throw Error("Camada entidade: Estoque deve ser um numero")
    }

    if(typeof produto.categoria != "string"){
        throw Error("Camada entidade: Categoria deve ser um texto")
    }


    return{
        id: produto.id,
        nome: produto.nome,
        preco: produto.preco,
        categoria: "",
        estoque: produto.estoque,
        cep: produto?.cep,
        rua: produto?.rua,
        bairro: produto?.bairro,
        cidade: produto?.cidade,
        estado: produto?.estado
    }
}
