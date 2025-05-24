package ge.evmittesttask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class EvMitTestTaskApplication {
	public static void main(String[] args) {
		SpringApplication.run(EvMitTestTaskApplication.class, args);
	}

}

//ToDo Добавить БД и JPA
//ToDo Добавить сохранение пользователей
//ToDo Добавить сохранение сессий с отметками времени
//ToDo Добавить выброс ошибок и обработку с возвращением модели на страницу error