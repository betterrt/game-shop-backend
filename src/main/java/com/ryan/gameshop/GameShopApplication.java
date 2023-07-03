package com.ryan.gameshop;

import com.ryan.gameshop.entity.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class GameShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameShopApplication.class, args);
	}

}
