import styles from "./Card.module.css"

export function Card(props) {
    const variant = props.variant || "light"
    const className = props.className || ""
    const resto = { ...props }
    delete resto.children
    delete resto.variant
    delete resto.className

    return (
        <div className={`${styles.card} ${styles[variant]} ${className}`} {...resto}>
            {props.children}
        </div>
    )
}
