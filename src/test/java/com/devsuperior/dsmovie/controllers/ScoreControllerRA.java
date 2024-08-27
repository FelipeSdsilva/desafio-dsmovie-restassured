package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@SpringBootTest
@AutoConfigureMockMvc
public class ScoreControllerRA {

    private String tokenAdmin, adminUsername, adminPassword;
    private Integer[] status;
    private Integer movieExistingId, movieNonExistingId;

    @BeforeEach
    void setUp() throws JSONException {

        adminUsername = "maria@gmail.com";
        adminPassword = "123456";

        movieExistingId = 13;
        movieNonExistingId = 100;
        baseURI = "http://localhost:8080";
        status = new Integer[]{200, 201, 404, 422, 403, 401};
        tokenAdmin = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
    }

    @Test
    public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("movieId", movieNonExistingId);
        requestBody.put("score", 4);

        given()
                .auth().preemptive().oauth2(tokenAdmin)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put("/scores")
                .then()
                .statusCode(status[2]);
    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("score", 1);

        given()
                .auth().preemptive().oauth2(tokenAdmin)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put("/scores")
                .then()
                .statusCode(status[3]);

    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("movieId", movieExistingId);
        requestBody.put("score", -4);

        given()
                .auth().preemptive().oauth2(tokenAdmin)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put("/scores")
                .then()
                .statusCode(status[3]);
    }
}
