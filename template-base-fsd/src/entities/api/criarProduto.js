import axios from "axios";

import { ProdutoEntidade } from "../model/produtoEntidade"

export async function criarProduto(produto){
    const produtoEntidade = ProdutoEntidade(produto)
    const response = await axios.post("http://localhost:8080/produtos", produtoEntidade)
    return ProdutoEntidade(response.data)
}
