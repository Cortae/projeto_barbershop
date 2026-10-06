import { useState } from "react"

import { Input } from "../../../shared/ui/Input/Input"
import { Button } from "../../../shared/ui/Button/Button"
import { buscarEnderecoPorCep } from "../api/cep"
import { validarCampos } from "../model/validadores"
import styles from "./CriarProdutoFormulario.module.css"
import { criarProduto } from "../../../entities/api/criarProduto"

const enderecoInicial = {
    rua: "",
    bairro: "",
    cidade: "",
    estado: ""
}

export function CriarProdutoFeature(props) {
    const [erros, setErros] = useState({})
    const [enviando, setEnviando] = useState(false)
    const [buscandoCep, setEstaBuscandoCep] = useState(false)
    const [endereco, setEndereco] = useState(enderecoInicial)

    function atualizarCampoEndereco(campo, valor) {
        const copia = {...endereco}
        copia[campo] = valor
        setEndereco(copia)
    }

    function definirErroCampo(campo, erro) {
        const copia = {...erros}
        copia[campo] = erro;
        setErros(copia)
    }

    async function preencherEndereco(evento) {
        const cep = evento.target.value.replace(/\D/g, "")

        if (cep.length !== 8) return

        setEstaBuscandoCep(true)

        const resultado = await buscarEnderecoPorCep(cep).finally(() => setEstaBuscandoCep(false))

        if (!resultado) {
            definirErroCampo("cep", { erro: true, mensagem: "CEP não encontrado" })
            return
        }

        definirErroCampo("cep", undefined)
        setEndereco({
            rua: resultado.rua,
            cidade:resultado.cidade,
            estado:resultado.estado,
        })
    }


    function converterDadosFormularioParaObjeto(target){
        const dadosFormulario = new FormData(target);
        return Object.fromEntries(dadosFormulario)
    }

    function existeAlgumErroNoFormulario(dados){
        return Object.values(dados).some(elemento=>elemento.erro)
    }

    async function salvarProduto(evento) {
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
            ...formulario,
            preco: Number(formulario.preco),
            estoque: Number(formulario.estoque)
        }

        // Requisição para criar produto
        criarProduto(formularioPayload).then((produtoCriado) => {
            evento.target.reset() 
            setEndereco(enderecoInicial)
            if(props.onCriar){
                props.onCriar(produtoCriado)
            }
        })
        .finally(()=>setEnviando(false))
    }

    return (
        <form onSubmit={salvarProduto} className={styles.form}>
            <div className={styles.colunas}>
                <fieldset className={styles.grupo}>
                    <legend className={styles.legenda}>Dados do produto</legend>

                    <Input
                        id="nome"
                        name="nome"
                        label="Nome"
                        error={erros.nome?.mensagem}
                    />
                    <div className={styles.linha}>
                        <Input
                            id="preco"
                            name="preco"
                            label="Preço"
                            type="number"
                            step="0.01"
                            error={erros.preco?.mensagem}
                        />
                        <Input
                            id="estoque"
                            name="estoque"
                            label="Estoque"
                            type="number"
                            error={erros.estoque?.mensagem}
                        />
                    </div>
                    <Input
                        id="categoria"
                        name="categoria"
                        label="Categoria"
                        error={erros.categoria?.mensagem}
                    />
                </fieldset>

                <fieldset className={`${styles.grupo} ${styles.grupoEndereco}`}>
                    <legend className={styles.legenda}>Localização do armazém</legend>

                    <Input
                        id="cep"
                        name="cep"
                        label="CEP"
                        placeholder="00000-000"
                        inputMode="numeric"
                        autoComplete="postal-code"
                        onBlur={preencherEndereco}
                        error={erros.cep?.mensagem}
                    />
                    {!erros.cep && (
                        <p className={styles.dica} aria-live="polite">
                            {buscandoCep ? "Buscando endereço..." : "O endereço é preenchido automaticamente pelo CEP."}
                        </p>
                    )}
                    <Input
                        id="rua"
                        label="Rua"
                        name="rua"
                        value={endereco.rua}
                        onChange={(evento) => atualizarCampoEndereco("rua", evento.target.value)}
                        disabled={buscandoCep}
                    />
                    <Input
                        id="bairro"
                        label="Bairro"
                        name="bairro"
                        value={endereco.bairro}
                        onChange={(evento) => atualizarCampoEndereco("bairro", evento.target.value)}
                        disabled={buscandoCep}
                    />
                    <div className={`${styles.linha} ${styles.linhaEstado}`}>
                        <Input
                            id="cidade"
                            label="Cidade"
                            name="cidade"
                            value={endereco.cidade}
                            onChange={(evento) => atualizarCampoEndereco("cidade", evento.target.value)}
                            disabled={buscandoCep}
                        />
                        <Input
                            id="estado"
                            label="Estado"
                            name="estado"
                            value={endereco.estado}
                            onChange={(evento) => atualizarCampoEndereco("estado", evento.target.value)}
                            disabled={buscandoCep}
                        />
                    </div>
                </fieldset>
            </div>

            <div className={styles.rodape}>
                <Button type="submit" disabled={enviando}>
                    {enviando ? "Salvando..." : "Criar produto"}
                </Button>
            </div>
        </form>
    )
}
