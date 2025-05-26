package com.torneos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Aplicación principal del Sistema de Torneos Deportivos
 * 
 * @author Sistema Torneos Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class TorneosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TorneosApplication.class, args);
    }
}