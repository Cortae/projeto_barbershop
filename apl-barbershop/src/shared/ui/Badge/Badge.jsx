import styles from "./Badge.module.css"

export function Badge(props) {
    const variant = props.variant || "navy"

    return (
        <span className={`${styles.badge} ${styles[variant]}`}>
            {props.children}
        </span>
    )
}
