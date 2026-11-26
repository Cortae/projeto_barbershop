package school.sptech.apl_api_barbershop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UsuarioIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void deveListarSomenteBarbeiros() throws Exception {
        mvc.perform(get("/usuarios/barbeiros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].nome").value("Lucas Almeida"))
                .andExpect(jsonPath("$[0].tipoUsuario").value("BARBEIRO"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].nome").value("Rafael Santos"))
                .andExpect(jsonPath("$[1].tipoUsuario").value("BARBEIRO"));
    }
}
