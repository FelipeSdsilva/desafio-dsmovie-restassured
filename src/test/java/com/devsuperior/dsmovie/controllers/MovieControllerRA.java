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
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerRA {

    private String movieTitle, tokenAdmin, tokenClient, tokenInvalid, clientUsername, clientPassword, adminUsername, adminPassword;
    private Integer[] status;
    private Integer movieExistingId, movieNonExistingId;

    @BeforeEach
    void setUp() throws JSONException {

        clientUsername = "alex@gmail.com";
        clientPassword = "123456";
        adminUsername = "maria@gmail.com";
        adminPassword = "123456";

        movieExistingId = 13;
        movieNonExistingId = 100;
        baseURI = "http://localhost:8080";
        movieTitle = "Vingadores";
        status = new Integer[]{200, 201, 404, 422, 403, 401};
        tokenAdmin = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        tokenClient = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        tokenInvalid = "nunvai";
    }

    @Test
    public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
        given()
                .get("/movies").then()
                .statusCode(status[0])
                .body("content.id", hasItem(movieExistingId))
                .body("numberOfElements", is(20))
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(20))
                .body("totalPages", equalTo(2));
    }

    @Test
    public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() throws Exception {

        given()
                .get("/movies?title={movieTitle}", movieTitle).then()
                .statusCode(status[0])
                .body("content.id", hasItem(movieExistingId))
                .body("content.title", hasItem("Vingadores: Ultimato"))
                .body("content.score", hasItem(0.0F))
                .body("content.image", hasItem("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg"))
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(20))
                .body("totalPages", equalTo(1));

    }

    @Test
    public void findByIdShouldReturnMovieWhenIdExists() {
        given()
                .get("/movies/{movieId}", movieExistingId).then()
                .statusCode(status[0])
                .body("id", is(movieExistingId))
                .body("title", equalTo("Vingadores: Ultimato"))
                .body("score", is(0.0F))
                .body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg"));

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
        given()
                .get("/movies/{movieId}", movieNonExistingId).then()
                .statusCode(status[2]);

    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "");
        requestBody.put("score", 0.0);
        requestBody.put("count", 0);
        requestBody.put("image", "");

        given()
                .auth().preemptive().oauth2(tokenAdmin)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post("/movies")
                .then()
                .statusCode(status[3]);
    }

    @Test
    public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Sin City");
        requestBody.put("score", 0.0);
        requestBody.put("count", 0);
        requestBody.put("image", "");

        given()
                .auth().preemptive().oauth2(tokenClient)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post("/movies")
                .then()
                .statusCode(status[4]);
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Sin City");
        requestBody.put("score", 0.0);
        requestBody.put("count", 0);
        requestBody.put("image", "");

        given()
                .auth().preemptive().oauth2(tokenInvalid)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post("/movies")
                .then()
                .statusCode(status[5]);
    }
}