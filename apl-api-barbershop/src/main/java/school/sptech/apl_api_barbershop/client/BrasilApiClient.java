package school.sptech.apl_api_barbershop.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import school.sptech.apl_api_barbershop.dto.feriado.FeriadoDto;
import school.sptech.apl_api_barbershop.exception.BrasilApiIndisponivelException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class BrasilApiClient {

    private static final String ERRO_API = "Não foi possível consultar os feriados na BrasilAPI.";

    private final RestClient restClient;

    public BrasilApiClient(RestClient brasilApiRestClient) {
        this.restClient = brasilApiRestClient;
    }

    public List<FeriadoDto> listarFeriados(int ano) {
        try {
            List<FeriadoDto> feriados = restClient.get()
                    .uri("/api/feriados/v1/{ano}", ano)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
            validarResposta(feriados);
            return feriados;
        } catch (RestClientException | HttpMessageConversionException exception) {
            throw new BrasilApiIndisponivelException(ERRO_API, exception);
        }
    }

    private void validarResposta(List<FeriadoDto> feriados) {
        if (feriados == null) {
            throw new BrasilApiIndisponivelException(ERRO_API);
        }

        for (FeriadoDto feriado : feriados) {
            if (feriado == null || feriado.getDate() == null || feriado.getName() == null
                    || feriado.getName().isBlank()) {
                throw new BrasilApiIndisponivelException(ERRO_API);
            }
            try {
                LocalDate.parse(feriado.getDate());
            } catch (DateTimeParseException exception) {
                throw new BrasilApiIndisponivelException(ERRO_API, exception);
            }
        }
    }
}
