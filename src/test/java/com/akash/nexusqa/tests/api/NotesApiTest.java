package com.akash.nexusqa.tests.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class NotesApiTest {

    @BeforeClass
    public void setupApi() {
        RestAssured.baseURI = "https://practice.expandtesting.com/notes/api";
    }

    @Test(groups = {"api", "regression"})
    public void healthCheckShouldReturnSuccess() {
        Response response = given()
                .when()
                .get("/health-check")
                .then()
                .statusCode(200)
                .extract().response();

        Assert.assertTrue(response.getBody().asString().toLowerCase().contains("success"),
                "Expected health-check response to indicate success");
    }

    @Test(groups = {"api", "regression"})
    public void registerUserShouldReturnUserObject() {
        String uniqueEmail = "nexusqa_" + System.currentTimeMillis() + "@example.com";

        String requestBody = "{" +
                "\"name\": \"NexusQA Tester\"," +
                "\"email\": \"" + uniqueEmail + "\"," +
                "\"password\": \"SecurePass123\"" +
                "}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/users/register")
                .then()
                .statusCode(201)
                .body("data.email", org.hamcrest.Matchers.equalTo(uniqueEmail))
                .body("data.name", org.hamcrest.Matchers.equalTo("NexusQA Tester"));
    }

    @Test(groups = {"api", "regression"})
    public void invalidLoginShouldReturnUnauthorized() {
        String requestBody = "{" +
                "\"email\": \"nonexistent_user_12345@example.com\"," +
                "\"password\": \"WrongPassword123\"" +
                "}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/users/login")
                .then()
                .statusCode(401);
    }
}