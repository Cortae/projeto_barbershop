import { useState } from "react"
import { Modal } from "../../../shared/ui/Modal/Modal"
import { Button } from "../../../shared/ui/Button/Button"
import { removerProduto } from "../../../entities"
import styles from "./RemoverProdutoFeature.module.css"

export function RemoverProdutoFeature(props) {
    const [removendo, setRemovendo] = useState(false)

    async function confirmarRemocao() {
        setRemovendo(true)
        try {
            await removerProduto(props.produto.id)
            props.onRemovido?.(props.produto.id)
        } finally {
            setRemovendo(false)
        }
    }

    return (
        <Modal titulo="Remover produto" onFechar={props.onCancelar}>
            <p className={styles.mensagem}>
                Tem certeza que deseja remover <strong>{props.produto.nome}</strong>? Essa ação não pode ser desfeita.
            </p>
            <div className={styles.acoes}>
                <Button variant="secondary" onClick={props.onCancelar} disabled={removendo}>Cancelar</Button>
                <Button variant="danger" onClick={confirmarRemocao} disabled={removendo}>
                    {removendo ? "Removendo..." : "Remover"}
                </Button>
            </div>
        </Modal>
    )
}
