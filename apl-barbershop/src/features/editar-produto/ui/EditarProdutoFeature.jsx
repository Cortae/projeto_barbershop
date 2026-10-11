import { useState } from "react"

import { Input } from "../../../shared/ui/Input/Input"
import { Button } from "../../../shared/ui/Button/Button"
import { atualizarProduto } from "../../../entities"
import { validarCampos } from "../model/validadores"
import styles from "./EditarProdutoFormulario.module.css"

export function EditarProdutoFeature(props) {
    const [erros, setErros] = useState({})
    const [enviando, setEnviando] = useState(false)

    function converterDadosFormularioParaObjeto(target) {
        const dadosFormulario = new FormData(target)
        return Object.fromEntries(dadosFormulario)
    }

    function existeAlgumErroNoFormulario(dados) {
        return Object.values(dados).some(elemento => elemento.erro)
    }

    async function salvarEdicao(evento) {
        evento.preventDefault()

        const formulario = converterDadosFormularioParaObjeto(evento.target)

        const resultado = validarCampos(formulario)

        const temErro = existeAlgumErroNoFormulario(resultado)

        if (temErro) {
            setErros(resultado)
            return
        }

        setErros({})

        setEnviando(true)

        const formularioPayload = {
            ...props.produto,
            ...formulario,
            preco: Number(formulario.preco),
            estoque: Number(formulario.estoque)
        }

        atualizarProduto(formularioPayload)
            .then((produtoAtualizado) => props.onSalvar?.(produtoAtualizado))
            .finally(() => setEnviando(false))
    }

    return (
        <form onSubmit={salvarEdicao} className={styles.form}>
            <div className={styles.colunas}>
                <fieldset className={styles.grupo}>
                    <legend className={styles.legenda}>Dados do produto</legend>

                    <Input
                        id="editar-nome"
                        name="nome"
                        label="Nome"
                        defaultValue={props.produto.nome}
                        error={erros.nome?.mensagem}
                    />
                    <div className={styles.linha}>
                        <Input
                            id="editar-preco"
                            name="preco"
                            label="Preço"
                            type="number"
                            step="0.01"
                            defaultValue={props.produto.preco}
                            error={erros.preco?.mensagem}
                        />
                        <Input
                            id="editar-estoque"
                            name="estoque"
                            label="Estoque"
                            type="number"
                            defaultValue={props.produto.estoque}
                            error={erros.estoque?.mensagem}
                        />
                    </div>
                    <Input
                        id="editar-categoria"
                        name="categoria"
                        label="Categoria"
                        defaultValue={props.produto.categoria}
                        error={erros.categoria?.mensagem}
                    />
                </fieldset>

                <fieldset className={`${styles.grupo} ${styles.grupoEndereco}`}>
                    <legend className={styles.legenda}>Localização do armazém</legend>

                    <Input
                        id="editar-cep"
                        name="cep"
                        label="CEP"
                        placeholder="00000-000"
                        inputMode="numeric"
                        autoComplete="postal-code"
                        defaultValue={props.produto.cep}
                    />
                    <Input
                        id="editar-rua"
                        label="Rua"
                        name="rua"
                        defaultValue={props.produto.rua}
                    />
                    <Input
                        id="editar-bairro"
                        label="Bairro"
                        name="bairro"
                        defaultValue={props.produto.bairro}
                    />
                    <div className={`${styles.linha} ${styles.linhaEstado}`}>
                        <Input
                            id="editar-cidade"
                            label="Cidade"
                            name="cidade"
                            defaultValue={props.produto.cidade}
                        />
                        <Input
                            id="editar-estado"
                            label="Estado"
                            name="estado"
                            defaultValue={props.produto.estado}
                        />
                    </div>
                </fieldset>
            </div>

            <div className={styles.rodape}>
                <Button type="submit" disabled={enviando}>
                    {enviando ? "Salvando..." : "Salvar alterações"}
                </Button>
            </div>
        </form>
    )
}
