package school.sptech.apl_api_barbershop.dto.bloqueio;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ImportacaoFeriadosResponse {

    @Schema(description = "Ano importado", example = "2026")
    private int ano;

    @Schema(description = "Quantidade de feriados retornados pela BrasilAPI", example = "12")
    private int feriadosEncontrados;

    @Schema(description = "Quantidade de barbeiros processados", example = "2")
    private int barbeiros;

    @Schema(description = "Quantidade de bloqueios criados", example = "20")
    private int bloqueiosCriados;

    @Schema(description = "Quantidade de bloqueios sobrepostos já existentes", example = "2")
    private int bloqueiosJaExistentes;

    @Schema(description = "Conflitos com agendamentos ativos")
    private List<ConflitoFeriadoResponse> conflitos;

    public ImportacaoFeriadosResponse() {
    }

    public ImportacaoFeriadosResponse(int ano, int feriadosEncontrados, int barbeiros,
                                     int bloqueiosCriados, int bloqueiosJaExistentes,
                                     List<ConflitoFeriadoResponse> conflitos) {
        this.ano = ano;
        this.feriadosEncontrados = feriadosEncontrados;
        this.barbeiros = barbeiros;
        this.bloqueiosCriados = bloqueiosCriados;
        this.bloqueiosJaExistentes = bloqueiosJaExistentes;
        this.conflitos = conflitos;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getFeriadosEncontrados() {
        return feriadosEncontrados;
    }

    public void setFeriadosEncontrados(int feriadosEncontrados) {
        this.feriadosEncontrados = feriadosEncontrados;
    }

    public int getBarbeiros() {
        return barbeiros;
    }

    public void setBarbeiros(int barbeiros) {
        this.barbeiros = barbeiros;
    }

    public int getBloqueiosCriados() {
        return bloqueiosCriados;
    }

    public void setBloqueiosCriados(int bloqueiosCriados) {
        this.bloqueiosCriados = bloqueiosCriados;
    }

    public int getBloqueiosJaExistentes() {
        return bloqueiosJaExistentes;
    }

    public void setBloqueiosJaExistentes(int bloqueiosJaExistentes) {
        this.bloqueiosJaExistentes = bloqueiosJaExistentes;
    }

    public List<ConflitoFeriadoResponse> getConflitos() {
        return conflitos;
    }

    public void setConflitos(List<ConflitoFeriadoResponse> conflitos) {
        this.conflitos = conflitos;
    }
}
