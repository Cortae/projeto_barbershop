import axios from "axios";

import { ProdutoEntidade } from "../model/produtoEntidade"

export async function atualizarProduto(produto){
    const produtoEntidade = ProdutoEntidade(produto)
    const response = await axios.patch(`http://localhost:8080/produtos/${produto.id}`, produtoEntidade)
    return ProdutoEntidade(response.data)
}
