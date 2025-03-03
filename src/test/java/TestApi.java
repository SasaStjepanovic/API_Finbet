import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.json.JSONObject;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;


public class TestApi {

    private static final String BASE_URL = "http://44.204.239.34:5000";

    /*** GET Request Utility - Positive Scenario ***/
    @Test(priority = 1)
    public void testUtility_Positive() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/health");

        System.out.println("Response Code: " + response.statusCode() + ", GET Request Utility - Positive Scenario");
        System.out.println("Response Body Get positive: " + response.getBody().asString());
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200"); // Verify status code
        Assert.assertEquals(response.getContentType(), "application/json", "Expected JSON response"); // Verify JSON format
    }

    /*** GET Request Utility - Negative Scenario ***/
    @Test(priority = 2)
    public void testUtility_Negative() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/nonexistent");

        System.out.println("Response Code: " + response.statusCode() + ", GET Request Utility - Negative Scenario");
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 404, "Expected HTTP 404");

    }

    /*** Post Request Login - Negative Scenario ***/
    @Test(priority = 3)
    public void testLogin_Negative() throws IOException, InterruptedException {

        String endpoint = "/login";

        HttpClient client = HttpClient.newHttpClient();

        String requestBody = "{ \"username\": \"kikonexe\", \"password\": \"password123E\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response Code: " + response.statusCode() + ", Post Request Login - Negative Scenario");
        System.out.println("Response Body Post Login negative : " + response.body());
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.statusCode(), 401, "Expected HTTP 401 OK");
    }

    /*** Post Request Login - Positive Scenario ***/
    @Test(priority = 4)
    public void testLogin_Positive() {

        HttpClient client = HttpClient.newHttpClient();

        String endpoint = "/login";
        String requestBody = "{ \"username\": \"Kikonen3\", \"password\": \"passw123KiK!4\" }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint)) //Set the URL request
                .header("Content-Type", "application/json") // Add HTTP header to specify JSON format
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Set HTTP method to POST and attache JSON body
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); // Send HTTP request and receive body as a String response

            JSONObject jsonResponse = new JSONObject(response.body()); // Parse JSON response
            Assert.assertEquals(jsonResponse.getString("message"), "Login successful", "Expected login success message"); // Verify message

            String token = jsonResponse.getString("access-token"); // Extract token
            System.out.println("Token is: " + token);

            Assert.assertEquals(jsonResponse.getString("access-token"), "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjEsImlhdCI6MTY3Mjc2NjAyOCwiZXhwIjoxNjc0NDk0MDI4fQ.kCak9sLJr74frSRVQp0_27BY4iBCgQSmoT3vQVWKzJg", "Expected access-token valid token"); // Verify message

            System.out.println("Response Code: " + response.statusCode() + ", Post Request Login - Positive Scenario");
            System.out.println("----------------------------------------------");

            Assert.assertNotNull(token, "Access token should not be null"); // Verify token is not null
            Assert.assertEquals(response.statusCode(), 200, "Expected HTTP 200 OK"); // Compare status codes

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during API request: " + e.getMessage());
        }
    }

    /*** Post Request Registration - Negative Scenario ***/
    @Test(priority = 5)
    public void testRegistration_Negative() {

        String endPoint = "/register";

        String requestBody = "{ \"username\": \"testuser\", \"password\": \"testpass\", \"email\": \"testuser\", " +
                "\"firstName\": \"testpass\", \"lastName\": \"testuser\", \"middleName\": \"testpass\" }";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endPoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode() + ", Post Request Registration - Negative Scenario");
            System.out.println("Response Body Post Registration negative:: " + response.body());
            System.out.println("----------------------------------------------");

            Assert.assertEquals(response.statusCode(), 400, "Expected HTTP 400 Created");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during API request: " + e.getMessage());
        }

    }

    /*** Post Request Registration - Positive Scenario ***/
    @Test(priority = 6)
    public void testRegistration_Positive() {

        String endPoint = "/register";

        String requestBody = "{ \"password\": \"passw123KiK!u\", \"username\": \"Kinen3su\", \"email\": \"strinsz3wsu@yahoo.com\", " +
                "\"firstName\": \"string\", \"middleName\": \"string\", \"lastName\": \"stjepanovic\" }";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endPoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode() + ", Post Request Registration - Positive Scenario");
            System.out.println("Response Body Post Registration positive:: " + response.body());
            System.out.println("----------------------------------------------");

            Assert.assertEquals(response.statusCode(), 200, "Expected HTTP 200 Created");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during API request: " + e.getMessage());
        }

    }

    /*** GET Request - User Negative Scenario 1 ***/
    @Test(priority = 7)
    public void testUser_Negative1() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/user/11");

        System.out.println("Response Code: " + response.statusCode() + ", GET Request User - Negative Scenario");
        System.out.println("Response Body Get negative, token is required: " + response.getBody().asString());
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 400, "Expected HTTP 400"); // Verify status code
        Assert.assertEquals(response.getContentType(), "application/json", "Expected JSON response"); // Verify JSON format

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Token is required"), "Response body should contain 'Token is required'");
    }

    /*** GET Request - User Negative Scenario 2 ***/
    @Test(priority = 8)
    public void testUser_Negative2() {

        String invalidToken = "invalid_token_12345"; // An invalid token

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + invalidToken)
                .when()
                .get("/user/23");

        System.out.println("Response Code: " + response.statusCode() + ", GET Request - Negative 2 User Scenario");
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 401, "Expected status code to be 401");
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Invalid token"), "Response body should contain 'Invalid token'");

    }


    }
