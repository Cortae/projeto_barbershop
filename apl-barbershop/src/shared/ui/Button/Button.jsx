import styles from "./Button.module.css"

export function Button(props) {
    const variant = props.variant || "primary"

    return (
        <button
            type={props.type || "button"}
            className={`${styles.botao} ${styles[variant]}`}
            onClick={props.onClick}
            disabled={props.disabled}
        >
            {props.children}
        </button>
    )
}
