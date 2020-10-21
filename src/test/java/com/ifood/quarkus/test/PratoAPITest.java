package com.ifood.quarkus.test;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class PratoAPITest {

	  @Test
	    public void testHelloEndpoint() {

	        //Aqui dará erro se não utilizar testContainers

	        String body = given()
	                .when().get("/pratos")
	                .then()
	                .statusCode(200).extract().asString();
	        System.out.println(body);
	    }
}
