package school.sptech.apl_api_barbershop.dto.bloqueio;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class ConflitoFeriadoResponse {

    @Schema(description = "Identificador do barbeiro", example = "3")
    private Integer barbeiroId;

    @Schema(description = "Data do feriado", example = "2026-04-03")
    private LocalDate data;

    @Schema(description = "Nome do feriado", example = "Sexta-feira Santa")
    private String feriado;

    public ConflitoFeriadoResponse() {
    }

    public ConflitoFeriadoResponse(Integer barbeiroId, LocalDate data, String feriado) {
        this.barbeiroId = barbeiroId;
        this.data = data;
        this.feriado = feriado;
    }

    public Integer getBarbeiroId() {
        return barbeiroId;
    }

    public void setBarbeiroId(Integer barbeiroId) {
        this.barbeiroId = barbeiroId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getFeriado() {
        return feriado;
    }

    public void setFeriado(String feriado) {
        this.feriado = feriado;
    }
}
