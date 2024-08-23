package com.devsuperior.dsmovie.controllers;

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

    private String movieTitle, tokenAdmin, tokenClient, tokenInvalid;
    private Integer[] status;
    private Integer movieExistingId, movieNonExistingId;

    @BeforeEach
    void setUp() {
        movieExistingId = 13;
        movieNonExistingId = 100;
        baseURI = "http://localhost:8080";
        movieTitle = "Vingadores";
        status = new Integer[]{200, 201, 404, 422, 403, 401};
        tokenAdmin = "eyJraWQiOiJhNDlkMmFmZi01Y2Q5LTRmNDAtOTg0ZC0zNTA2ZTk0NTg0NjYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJteWNsaWVudGlkIiwiYXVkIjoibXljbGllbnRpZCIsIm5iZiI6MTcyNDQxNTg0NiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZXhwIjoxNzI0NTAyMjQ2LCJpYXQiOjE3MjQ0MTU4NDYsImp0aSI6ImJmY2JlZDc4LWJiYTUtNDk5YS1hZjVkLTMzMmJjYTg5Y2EwZCIsImF1dGhvcml0aWVzIjpbIlJPTEVfQ0xJRU5UIiwiUk9MRV9BRE1JTiJdLCJ1c2VybmFtZSI6Im1hcmlhQGdtYWlsLmNvbSJ9.2QV2OO2EhMCrFJVS3eBiYAzrxpIXmzleQZS64V8WQf4P9X3Opyke5Tcbzi3HK9zdJha_UHCTsLvJVyxgOpXRC7QtnsH4ypuN3GXd7AdBaArpOgMTU3aqQ849LyuRCtB0uNGMDtq3qvmK_tQC4afyrb9QSyjA2ZBE7X1z63zpGyMnGsRjeKkvwlZom4cg-jvIxW7wqDh2yIRnuSlYMitKH1-_G3LzYqupRuMa_eIs1aGpMVxjrvySdrKTsX6h9o-Y2PFANUpL8BX7U44YARfkMb7X_OLt3HBuLxD1-FzL28vOriBhqdUIKI9nohr5KoQs7s8vNRqn1MjtltmBtofLRQ";
        tokenClient = "eyJraWQiOiJhNDlkMmFmZi01Y2Q5LTRmNDAtOTg0ZC0zNTA2ZTk0NTg0NjYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJteWNsaWVudGlkIiwiYXVkIjoibXljbGllbnRpZCIsIm5iZiI6MTcyNDQxNjAzMywiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZXhwIjoxNzI0NTAyNDMzLCJpYXQiOjE3MjQ0MTYwMzMsImp0aSI6IjhhYjA3OThjLWVmY2QtNGMwMS1hZGE1LWQwODcyNTJiMDhkZiIsImF1dGhvcml0aWVzIjpbIlJPTEVfQ0xJRU5UIl0sInVzZXJuYW1lIjoiYWxleEBnbWFpbC5jb20ifQ.gUzy-nRBsK8--v5mJBNK9hcqKhg5837q8eyIcBo7oTUvYUbihb9FysqK425uZLhvRLgSoj-J43uDc_kZ6z5nvQEA8DTjc0vww93Ml3G9O0SdDL88im1Kqy9IqiBoTpRB0__6-qoi5qVU86XvUlSgPCaj9tJ5cmynZ0zltQtzcDkwj1FbMXprDXQVMYy2kqm3IljOKJHvZbhmdSY7mgGZv8_wz4v7fQYNPsFW09_-014M71VFd5dxQg_MisqlP65s6xSdccyDfBh_Smd0w4K83aPBhrJjxW3-T9D0PQ7jpyt6QNdm-nAySJkmAY9yyp1CHvqfCwjst3eq0zS8LwS8-Q";
        tokenInvalid = "nunvai";
    }

    @Test
    public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
        given()
                .get("/movies").then()
                .statusCode(status[0])
                .body("content.id", hasItem(movieExistingId))
                .body("totalElements", is(29))
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