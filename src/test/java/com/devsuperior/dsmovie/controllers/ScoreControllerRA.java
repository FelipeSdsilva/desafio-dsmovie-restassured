package com.devsuperior.dsmovie.controllers;

import io.restassured.http.ContentType;
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

    private String tokenAdmin;
    private Integer[] status;
    private Integer movieExistingId, movieNonExistingId;

    @BeforeEach
    void setUp() {
        movieExistingId = 13;
        movieNonExistingId = 100;
        baseURI = "http://localhost:8080";
        status = new Integer[]{200, 201, 404, 422, 403, 401};
        tokenAdmin = "eyJraWQiOiJhNDlkMmFmZi01Y2Q5LTRmNDAtOTg0ZC0zNTA2ZTk0NTg0NjYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJteWNsaWVudGlkIiwiYXVkIjoibXljbGllbnRpZCIsIm5iZiI6MTcyNDQxNTg0NiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZXhwIjoxNzI0NTAyMjQ2LCJpYXQiOjE3MjQ0MTU4NDYsImp0aSI6ImJmY2JlZDc4LWJiYTUtNDk5YS1hZjVkLTMzMmJjYTg5Y2EwZCIsImF1dGhvcml0aWVzIjpbIlJPTEVfQ0xJRU5UIiwiUk9MRV9BRE1JTiJdLCJ1c2VybmFtZSI6Im1hcmlhQGdtYWlsLmNvbSJ9.2QV2OO2EhMCrFJVS3eBiYAzrxpIXmzleQZS64V8WQf4P9X3Opyke5Tcbzi3HK9zdJha_UHCTsLvJVyxgOpXRC7QtnsH4ypuN3GXd7AdBaArpOgMTU3aqQ849LyuRCtB0uNGMDtq3qvmK_tQC4afyrb9QSyjA2ZBE7X1z63zpGyMnGsRjeKkvwlZom4cg-jvIxW7wqDh2yIRnuSlYMitKH1-_G3LzYqupRuMa_eIs1aGpMVxjrvySdrKTsX6h9o-Y2PFANUpL8BX7U44YARfkMb7X_OLt3HBuLxD1-FzL28vOriBhqdUIKI9nohr5KoQs7s8vNRqn1MjtltmBtofLRQ";
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
