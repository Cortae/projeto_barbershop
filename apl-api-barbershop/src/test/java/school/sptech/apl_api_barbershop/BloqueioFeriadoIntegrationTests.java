package school.sptech.apl_api_barbershop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import school.sptech.apl_api_barbershop.client.BrasilApiClient;
import school.sptech.apl_api_barbershop.dto.feriado.FeriadoDto;
import school.sptech.apl_api_barbershop.entity.Bloqueio;
import school.sptech.apl_api_barbershop.exception.BrasilApiIndisponivelException;
import school.sptech.apl_api_barbershop.repository.BloqueioRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:barbershop-feriados")
@Transactional
class BloqueioFeriadoIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BloqueioRepository bloqueioRepository;

    @MockitoBean
    private BrasilApiClient brasilApiClient;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void criaBloqueioDeDiaInteiroParaCadaBarbeiro() throws Exception {
        when(brasilApiClient.listarFeriados(2026))
                .thenReturn(List.of(new FeriadoDto("2026-01-01", "Confraternização mundial", "national")));

        mvc.perform(post("/bloqueios/feriados/2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ano").value(2026))
                .andExpect(jsonPath("$.feriadosEncontrados").value(1))
                .andExpect(jsonPath("$.barbeiros").value(2))
                .andExpect(jsonPath("$.bloqueiosCriados").value(2))
                .andExpect(jsonPath("$.bloqueiosJaExistentes").value(0))
                .andExpect(jsonPath("$.conflitos").isEmpty());

        List<Bloqueio> bloqueios = bloqueiosNoDia("2026-01-01T00:00:00", "2026-01-02T00:00:00");
        assertThat(bloqueios).hasSize(2);
        assertThat(bloqueios)
                .allSatisfy(bloqueio -> {
                    assertThat(bloqueio.getInicio()).isEqualTo(LocalDateTime.parse("2026-01-01T00:00:00"));
                    assertThat(bloqueio.getFim()).isEqualTo(LocalDateTime.parse("2026-01-01T23:59:59"));
                    assertThat(bloqueio.getMotivo()).startsWith("Feriado: ");
                });
    }

    @Test
    void repetirImportacaoNaoCriaDuplicados() throws Exception {
        when(brasilApiClient.listarFeriados(2026))
                .thenReturn(List.of(new FeriadoDto("2026-02-16", "Carnaval", "national")));

        mvc.perform(post("/bloqueios/feriados/2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bloqueiosCriados").value(2));

        mvc.perform(post("/bloqueios/feriados/2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bloqueiosCriados").value(0))
                .andExpect(jsonPath("$.bloqueiosJaExistentes").value(2));

        assertThat(bloqueiosNoDia("2026-02-16T00:00:00", "2026-02-17T00:00:00")).hasSize(2);
    }

    @Test
    void bloqueioManualSobrepostoEhContadoComoExistente() throws Exception {
        when(brasilApiClient.listarFeriados(2026))
                .thenReturn(List.of(new FeriadoDto("2026-10-13", "Feriado de teste", "national")));

        mvc.perform(post("/bloqueios/feriados/2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bloqueiosCriados").value(1))
                .andExpect(jsonPath("$.bloqueiosJaExistentes").value(1))
                .andExpect(jsonPath("$.conflitos").isEmpty());
    }

    @Test
    void agendamentoAtivoImpedeBloqueioEApareceNosConflitos() throws Exception {
        when(brasilApiClient.listarFeriados(2026))
                .thenReturn(List.of(new FeriadoDto("2026-10-14", "Feriado de teste", "national")));

        mvc.perform(post("/bloqueios/feriados/2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bloqueiosCriados").value(1))
                .andExpect(jsonPath("$.bloqueiosJaExistentes").value(0))
                .andExpect(jsonPath("$.conflitos.length()").value(1))
                .andExpect(jsonPath("$.conflitos[0].barbeiroId").value(3))
                .andExpect(jsonPath("$.conflitos[0].data").value("2026-10-14"))
                .andExpect(jsonPath("$.conflitos[0].feriado").value("Feriado de teste"));
    }

    @Test
    void anoInvalidoRetornaBadRequestSemConsultarBrasilApi() throws Exception {
        mvc.perform(post("/bloqueios/feriados/1800"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(brasilApiClient);
    }

    @Test
    void falhaDaBrasilApiRetornaBadGatewaySemGravarBloqueios() throws Exception {
        long quantidadeInicial = bloqueioRepository.count();
        doThrow(new BrasilApiIndisponivelException("BrasilAPI indisponível"))
                .when(brasilApiClient).listarFeriados(2026);

        mvc.perform(post("/bloqueios/feriados/2026"))
                .andExpect(status().isBadGateway());

        assertThat(bloqueioRepository.count()).isEqualTo(quantidadeInicial);
    }

    private List<Bloqueio> bloqueiosNoDia(String inicio, String fim) {
        LocalDateTime inicioPeriodo = LocalDateTime.parse(inicio);
        LocalDateTime fimPeriodo = LocalDateTime.parse(fim);
        return bloqueioRepository.findAll().stream()
                .filter(bloqueio -> !bloqueio.getInicio().isBefore(inicioPeriodo))
                .filter(bloqueio -> bloqueio.getInicio().isBefore(fimPeriodo))
                .toList();
    }
}
