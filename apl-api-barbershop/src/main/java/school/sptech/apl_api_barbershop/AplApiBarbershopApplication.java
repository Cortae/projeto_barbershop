package school.sptech.apl_api_barbershop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "API - JrBarbershop",
				version = "1.0",
				description = "API responsável por gerenciar os agendamentos e o financeiro da barbearia JrBarbershop",
				license = @License(name = "Apache 2.0")
		)
)

@SpringBootApplication
public class AplApiBarbershopApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplApiBarbershopApplication.class, args);
	}

}
