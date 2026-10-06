import styles from "./Input.module.css"

export function Input(props) {
    const resto = { ...props }
    delete resto.label
    delete resto.id
    delete resto.error

    const idErro = props.error ? `${props.id}-erro` : undefined

    return (
        <div className={styles.campo}>
            {props.label && <label className={styles.label} htmlFor={props.id}>{props.label}</label>}
            <input
                id={props.id}
                className={`${styles.input} ${props.error ? styles.comErro : ""}`}
                aria-invalid={props.error ? true : undefined}
                aria-describedby={idErro}
                {...resto}
            />
            {props.error && <span id={idErro} className={styles.erro}>{props.error}</span>}
        </div>
    )
}
