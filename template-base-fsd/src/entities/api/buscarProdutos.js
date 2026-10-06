import axios from "axios";

import { ProdutoEntidade } from "../model/produtoEntidade"

export async function buscarProdutos(){
    const response = await axios.get("http://localhost:8080/produtos")
    return response.data.map(ProdutoEntidade)
}
