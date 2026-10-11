import { useEffect, useId } from "react"
import styles from "./Modal.module.css"

export function Modal(props) {
    const idTitulo = useId()
    const tamanho = props.tamanho === "grande" ? styles.grande : ""

    useEffect(() => {
        function aoTeclar(evento) {
            if (evento.key === "Escape") props.onFechar()
        }
        document.addEventListener("keydown", aoTeclar)
        return () => document.removeEventListener("keydown", aoTeclar)
    }, [props.onFechar])

    return (
        <div className={styles.sobreposicao} onClick={props.onFechar}>
            <div
                className={`${styles.modal} ${tamanho}`}
                role="dialog"
                aria-modal="true"
                aria-labelledby={idTitulo}
                onClick={(evento) => evento.stopPropagation()}
            >
                <div className={styles.cabecalho}>
                    <h2 id={idTitulo} className={styles.titulo}>{props.titulo}</h2>
                    <button type="button" className={styles.fechar} onClick={props.onFechar} aria-label="Fechar">
                        <svg viewBox="0 0 16 16" width="16" height="16" aria-hidden="true">
                            <path d="M3.5 3.5l9 9m0-9l-9 9" stroke="currentColor" strokeWidth="1.75" strokeLinecap="round" />
                        </svg>
                    </button>
                </div>
                <div className={styles.conteudo}>
                    {props.children}
                </div>
            </div>
        </div>
    )
}
