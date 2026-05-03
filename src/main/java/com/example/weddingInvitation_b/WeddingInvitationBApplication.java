package com.example.weddingInvitation_b;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeddingInvitationBApplication {

	public static void main(String[] args) {
		// Spring 기동 전에 .env 파일을 시스템 프로퍼티로 등록
		// - 시스템 환경변수가 이미 존재하면 .env 값으로 덮어쓰지 않음 (배포 환경 우선)
		// - .env 파일이 없으면 무시 (CI/CD 등 환경에서 안전)
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(entry -> {
			if (System.getenv(entry.getKey()) == null) {
				System.setProperty(entry.getKey(), entry.getValue());
			}
		});

		SpringApplication.run(WeddingInvitationBApplication.class, args);
	}

}
