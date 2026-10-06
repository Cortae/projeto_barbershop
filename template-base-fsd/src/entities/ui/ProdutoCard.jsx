import { Card } from "../../shared/ui/Card/Card"
import { Badge } from "../../shared/ui/Badge/Badge"
import { Button } from "../../shared/ui/Button/Button"
import styles from "./ProdutoCard.module.css"

export function ProdutoCard(props) {
    return (
        <Card className={styles.card}>
            <div className={styles.cabecalho}>
                <h3 className={styles.nome}>{props.produto.nome}</h3>
                <Badge variant="accent">{props.produto.categoria}</Badge>
            </div>

            <div className={styles.valores}>
                <p className={styles.preco}>
                    {props.produto.preco.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                </p>
                <p className={styles.estoque}>{props.produto.estoque} em estoque</p>
            </div>

            {(props.onEditar || props.onRemover) && (
                <div className={styles.acoes}>
                    {props.onEditar && (
                        <Button variant="secondary" onClick={() => props.onEditar(props.produto)}>Editar</Button>
                    )}
                    {props.onRemover && (
                        <Button variant="dangerGhost" onClick={() => props.onRemover(props.produto)}>Remover</Button>
                    )}
                </div>
            )}
        </Card>
    )
}
