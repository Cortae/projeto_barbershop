package school.sptech.apl_api_barbershop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class AgendamentoIntegrationTests {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void barbeiroSemAgendamentosRetornaListaVazia() throws Exception {
        mvc.perform(get("/agendamentos/barbeiro/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void cadastroMultisservicoCalculaDuracaoEValor() throws Exception {
        mvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "barbeiro": 2,
                                  "cliente": 1,
                                  "servicos": [1, 2],
                                  "inicio": "2026-10-15T10:00:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.servicos.length()").value(2))
                .andExpect(jsonPath("$.horarioFim").value("2026-10-15T10:50:00"))
                .andExpect(jsonPath("$.pagamento.valor").value(75.0));
    }

    @Test
    void cadastroRejeitaSobreposicaoCompleta() throws Exception {
        mvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "barbeiro": 2,
                                  "cliente": 1,
                                  "servicos": [1],
                                  "inicio": "2026-10-12T13:10:00"
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void cadastroRejeitaBloqueio() throws Exception {
        mvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "barbeiro": 2,
                                  "cliente": 1,
                                  "servicos": [1],
                                  "inicio": "2026-10-12T15:15:00"
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void alterarServicosRecalculaCamposDerivados() throws Exception {
        mvc.perform(patch("/agendamentos/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "idAgendamento": 8, "servicos": [3, 5] }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.servicos.length()").value(2))
                .andExpect(jsonPath("$.horarioFim").value("2026-10-14T17:00:00"))
                .andExpect(jsonPath("$.titulo").value("Diego Carvalho - Duração: 60min"))
                .andExpect(jsonPath("$.pagamento.valor").value(80.0));
    }

    @Test
    void concluirSemPagamentoNaoExigeBody() throws Exception {
        mvc.perform(patch("/agendamentos/concluir/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDO"))
                .andExpect(jsonPath("$.pagamento.pago").value(false));
    }

    @Test
    void historicoRetornaSomenteConcluidos() throws Exception {
        mvc.perform(get("/agendamentos/cliente/4"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        mvc.perform(get("/agendamentos/cliente/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CONCLUIDO"));
    }

    @Test
    void edicaoTransacionalRejeitaConflitoSemAlterarAgendamento() throws Exception {
        mvc.perform(put("/agendamentos/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cliente": 1,
                                  "servicos": [1, 2],
                                  "inicio": "2026-10-13T12:10:00"
                                }
                                """))
                .andExpect(status().isConflict());

        mvc.perform(get("/agendamentos/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente.id").value(4))
                .andExpect(jsonPath("$.horarioInicio").value("2026-10-12T14:00:00"))
                .andExpect(jsonPath("$.servicos.length()").value(1));
    }
}
