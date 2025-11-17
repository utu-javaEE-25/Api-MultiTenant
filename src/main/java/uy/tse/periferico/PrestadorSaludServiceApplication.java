package uy.tse.periferico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uy.tse.periferico.config.SecurityConfig;

@SpringBootApplication
@EnableTransactionManagement
public class PrestadorSaludServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrestadorSaludServiceApplication.class, args);
	}

}
