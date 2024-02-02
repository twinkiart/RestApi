package com.example.restapi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITest {
    private final int unexistingPetId = 999;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2/";
    }

    @Test
    public void petNotFound(){
        Response response = given()
                .when()
                .get("pet/" + unexistingPetId);
        System.out.println("Response: " + response.prettyPrint());
        assertEquals(404, response.statusCode());
        assertEquals("HTTP/1.1 404 Not Found", response.getStatusLine());
        assertEquals("Pet not found", response.jsonPath().getString("message"));
    }

    @Test
    public void petNotFoundNotBDD(){
        Response response = given()
                .when()
                .get("pet/" + unexistingPetId);
        System.out.println("Response: " + response.prettyPrint());
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(404);
        validatableResponse.statusLine("HTTP/1.1 404 Not Found");
        validatableResponse.body("message", equalTo("Pet not found"));
    }

    @Test
    public void petNotFound_BDD(){
        given()
                .when()
                .get("pet/{id}", unexistingPetId)
                .then()
                .log().all()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found")
                .body("type",equalTo("error"))
                .body("message", equalTo("Pet not found"));
    }
}
