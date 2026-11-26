package school.sptech.apl_api_barbershop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import school.sptech.apl_api_barbershop.client.BrasilApiClient;
import school.sptech.apl_api_barbershop.dto.feriado.FeriadoDto;
import school.sptech.apl_api_barbershop.exception.BrasilApiIndisponivelException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadGateway;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class BrasilApiClientTests {

    private MockRestServiceServer server;
    private BrasilApiClient brasilApiClient;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://brasilapi.test");
        server = MockRestServiceServer.bindTo(builder).build();
        brasilApiClient = new BrasilApiClient(builder.build());
    }

    @Test
    void desserializaRespostaDaBrasilApi() {
        server.expect(requestTo("https://brasilapi.test/api/feriados/v1/2026"))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess("""
                        [
                          {
                            "date": "2026-01-01",
                            "name": "Confraternização mundial",
                            "type": "national",
                            "weekday": "quinta-feira"
                          }
                        ]
                        """, MediaType.APPLICATION_JSON));

        List<FeriadoDto> feriados = brasilApiClient.listarFeriados(2026);

        assertThat(feriados).hasSize(1);
        assertThat(feriados.getFirst().getDate()).isEqualTo("2026-01-01");
        assertThat(feriados.getFirst().getName()).isEqualTo("Confraternização mundial");
        assertThat(feriados.getFirst().getType()).isEqualTo("national");
        server.verify();
    }

    @Test
    void converteErroHttpEmIndisponibilidade() {
        server.expect(requestTo("https://brasilapi.test/api/feriados/v1/2026"))
                .andRespond(withBadGateway());

        assertThatThrownBy(() -> brasilApiClient.listarFeriados(2026))
                .isInstanceOf(BrasilApiIndisponivelException.class);
        server.verify();
    }

    @Test
    void rejeitaRespostaComDataInvalida() {
        server.expect(requestTo("https://brasilapi.test/api/feriados/v1/2026"))
                .andRespond(withSuccess("""
                        [{ "date": "data-invalida", "name": "Feriado", "type": "national" }]
                        """, MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> brasilApiClient.listarFeriados(2026))
                .isInstanceOf(BrasilApiIndisponivelException.class);
        server.verify();
    }
}
