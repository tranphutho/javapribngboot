package com.app.appdemo.database;

import com.app.appdemo.models.Product;
import com.app.appdemo.repositories.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Database {
    private static final Logger logger= LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner intDatabase(ProductRepository productRepository){

        //logger

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product productA= new  Product("Macbook Pro",2020,2400.0,"");
                Product productB= new Product("Ipad air",2022,1000.0,"");
                logger.info ("insert data: " + productRepository.save(productA));
                logger.info ("insert data: "+ productRepository.save(productB));
            }
        };

    }

}
