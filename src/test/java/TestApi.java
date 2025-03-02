import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;


public class TestApi {

    private static final String BASE_URL = "http://44.204.239.34:5000";

    /*** GET Request - Positive Scenario ***/
    @Test(priority = 1)
    public void testGet_Positive() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/health");

        System.out.println("Response Code: " + response.statusCode() + ", GET Request - Positive Scenario");
        System.out.println("Response Body Get positive: " + response.getBody().asString());
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200");
        Assert.assertEquals(response.getContentType(), "application/json", "Expected JSON response");
    }

    /*** GET Request - Negative Scenario ***/
    @Test(priority = 2)
    public void testGet_Negative() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/nonexistent");
        System.out.println("Response Code: " + response.statusCode() + ", GET Request - Negative Scenario");
        System.out.println("----------------------------------------------");
        Assert.assertEquals(response.getStatusCode(), 404, "Expected HTTP 404");
    }


}
