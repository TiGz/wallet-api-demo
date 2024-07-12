package org.github.tigz.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Wallet API application.
 * This class is responsible for bootstrapping and launching the Spring Boot application.
 */
@SpringBootApplication
public class WalletApiApplication {

    /**
     * The main method which serves as the entry point for the application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(WalletApiApplication.class, args);
    }
}