import { useEffect, useState } from "react"
import { Card } from "../../../shared/ui/Card/Card"
import { Modal } from "../../../shared/ui/Modal/Modal"
import { Badge } from "../../../shared/ui/Badge/Badge"
import { ProdutoCard, buscarProdutos } from "../../../entities"
import { CriarProdutoFeature, EditarProdutoFeature, RemoverProdutoFeature } from "../../../features"
import styles from "./CatalogoProdutosWidget.module.css"

export function CatalogoProdutosWidget() {
    const [produtos, setProdutos] = useState([])
    const [carregando, setCarregando] = useState(true)
    const [produtoEmEdicao, setProdutoEmEdicao] = useState(null)
    const [produtoParaRemover, setProdutoParaRemover] = useState(null)

    function carregarProdutos(){
        buscarProdutos()
            .then((produtos)=>{
                setProdutos(produtos)
            })
            .finally(() => setCarregando(false))
    }

    useEffect(() => {
        carregarProdutos()
    }, [])

    function aoCriar(produtoCriado) {
        const copia = [...produtos]
        copia.push(produtoCriado)
        setProdutos(copia)
    }

    function aoSalvarEdicao(produtoAtualizado) {
        const copia = [...produtos]
        const indice = copia.findIndex((produto) => produto.id === produtoAtualizado.id)
        if (indice !== -1) copia[indice] = produtoAtualizado
        setProdutos(copia)
        setProdutoEmEdicao(null)
    }

    function aoRemover(id) {
        const copia = [...produtos]
        const indice = copia.findIndex((produto) => produto.id === id)
        if (indice !== -1) copia.splice(indice, 1)
        setProdutos(copia)
        setProdutoParaRemover(null)
    }

    return (
        <>
            <section className={styles.secao} aria-labelledby="titulo-novo-produto">
                <h2 id="titulo-novo-produto" className={styles.tituloSecao}>Novo produto</h2>
                <Card>
                    <CriarProdutoFeature onCriar={aoCriar} />
                </Card>
            </section>

            <section className={styles.secao} aria-labelledby="titulo-produtos" aria-busy={carregando}>
                <div className={styles.cabecalhoSecao}>
                    <h2 id="titulo-produtos" className={styles.tituloSecao}>Produtos cadastrados</h2>
                    {!carregando && <Badge variant="secondary">{produtos.length}</Badge>}
                </div>

                {carregando && <p className={styles.estado} role="status">Carregando produtos...</p>}

                {!carregando && produtos.length === 0 && (
                    <Card variant="soft" className={styles.estadoVazio}>
                        <p className={styles.estadoVazioTitulo}>Nenhum produto cadastrado ainda.</p>
                        <p className={styles.estado}>Use o formulário acima para adicionar o primeiro.</p>
                    </Card>
                )}

                {!carregando && produtos.length > 0 && (
                    <div className={styles.grade}>
                        {produtos.map((produto) => (
                            <ProdutoCard
                                key={produto.id}
                                produto={produto}
                                onEditar={setProdutoEmEdicao}
                                onRemover={setProdutoParaRemover}
                            />
                        ))}
                    </div>
                )}
            </section>

            {produtoEmEdicao && (
                <Modal titulo="Editar produto" tamanho="grande" onFechar={() => setProdutoEmEdicao(null)}>
                    <EditarProdutoFeature produto={produtoEmEdicao} onSalvar={aoSalvarEdicao} />
                </Modal>
            )}

            {produtoParaRemover && (
                <RemoverProdutoFeature
                    produto={produtoParaRemover}
                    onCancelar={() => setProdutoParaRemover(null)}
                    onRemovido={aoRemover}
                />
            )}
        </>
    )
}
