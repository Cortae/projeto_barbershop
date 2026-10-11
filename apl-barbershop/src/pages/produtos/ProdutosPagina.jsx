import { CatalogoProdutosWidget } from "../../widgets"
import styles from "./ProdutosPagina.module.css"

export function ProdutosPagina() {
    return (
        <>
            <header className={styles.barraTopo}>
                <div className={styles.barraTopoConteudo}>
                    <img
                        className={styles.logo}
                        src="/imagens/sptech-logo-azul-escuro.png"
                        alt="São Paulo Tech School"
                        width="567"
                        height="271"
                    />
                </div>
            </header>

            <main className={styles.pagina}>
                <div className={styles.cabecalho}>
                    <span className={styles.rotulo}>Catálogo</span>
                    <h1 className={styles.titulo}>Produtos</h1>
                    <p className={styles.descricao}>Cadastre, edite e remova os produtos do estoque.</p>
                </div>

                <CatalogoProdutosWidget />
            </main>
        </>
    )
}
