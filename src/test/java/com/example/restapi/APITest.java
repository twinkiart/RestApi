package com.example.restapi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITest {
    private final int unexistingPetId = 999;

    @BeforeEach
    public void setup() {
        //RestAssured.baseURI = "https://petstore.swagger.io/v2/";
        RestAssured.baseURI = "https://demoqa.com/Account/v1/";
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
    @Test
    public void newPetTest(){
    Integer id = 998;
    String name = "J";
    String status = "sold";

    Map<String,String> request = new HashMap<>();
        request.put("id",id.toString());
        request.put("name",name);
        request.put("status",status);
        given().contentType("application/json")
                .body(request)
                .when()
                .post(baseURI + "pet/")
                .then()
                .log().all()
                .time(lessThan(2000L))
                .assertThat()
                .statusCode(200)
                .body("name",equalTo(name))
                .body("status",equalTo(status))
                .body("id",equalTo(id));
    }
    @Test
    @DisplayName("Авторизация пользователя")
    public void postUserACC(){
        String userName = "TestAPI";
        String password = "Twin_111!";

        Map<String,String> requst = new HashMap<>();
        requst.put("userName",userName);
        requst.put("password",password);
        given().contentType("application/json")
                .body(requst)
                .when()
                .post(baseURI + "Authorized")
                .then()
                .log().all()
                .time(lessThan(2000L))
                .assertThat()
                .statusCode(200);

    }

}
