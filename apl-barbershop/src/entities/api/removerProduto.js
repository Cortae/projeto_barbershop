import axios from "axios";

export async function removerProduto(id){
    await axios.delete(`http://localhost:8080/produtos/${id}`)
}
