package school.sptech.apl_api_barbershop.dto.feriado;

import io.swagger.v3.oas.annotations.media.Schema;

public class FeriadoDto {

    @Schema(description = "Data do feriado", example = "2026-01-01")
    private String date;

    @Schema(description = "Nome do feriado", example = "Confraternização mundial")
    private String name;

    @Schema(description = "Tipo do feriado", example = "national")
    private String type;

    public FeriadoDto() {
    }

    public FeriadoDto(String date, String name, String type) {
        this.date = date;
        this.name = name;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
