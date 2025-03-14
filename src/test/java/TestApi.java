import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
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
import com.github.javafaker.Faker;


public class TestApi {

    private static final String BASE_URL = "http://44.204.239.34:5000";

    Faker faker = new Faker();

    String randomName = faker.name().firstName();
    int number = faker.number().numberBetween(1, 9999);
    String password = "KiK!" + number;
    String username = "Kiko" + number;
    String email = faker.internet().emailAddress();
    String firstName = faker.name().firstName();
    String middleName = faker.name().nameWithMiddle();
    String lastName = faker.name().lastName();

    /*** Post Request Registration - Positive Scenario ***/
    @Test(priority = 1)
    public void testRegistration_Positive() {
        String endPoint = "/register";

        System.out.println("user " + username);
        String requestBody = "{"
                + "\"username\": \"" + username + "\","
                + "\"password\": \"" + password + "\","
                + "\"email\": \"" + email + "\","
                + "\"firstName\": \"" + firstName + "\","
                + "\"middleName\": \"" + middleName + "\","
                + "\"lastName\": \"" + lastName + "\""
                + "}";

        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL)
                .body(requestBody)
                .when()
                .post("/register");


            System.out.println("Response StatusCode is: " + response.statusCode() + ", Post Request Registration - Positive Scenario");
            System.out.println("Response Body Registration positive:: " + response.getBody().asString());
            System.out.println();
            System.out.println("----------------------------------------------");

            String userId = response.jsonPath().getString("id");
            Assert.assertNotNull(userId, "User ID should not be null.");
            Assert.assertEquals(response.statusCode(), 200, "Expected status code to be 200");

    }

    /*** Post Request Registration - Negative Scenario ***/
    @Test(priority = 2)
    public void testRegistration_Negative() {

        String endPoint = "/register";

        String requestBody = "{ \"username\": \"testuser\", \"password\": \"TestPass1!\", \"email\": \"testuser\", " +
                "\"firstName\": \"testpass\", \"lastName\": \"testuser\", \"middleName\": \"testpass\" }";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endPoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response StatusCode is: " + response.statusCode() + ", Post Request Registration - Negative Scenario");
            System.out.println("Response Body Registration negative:: " + response.body());
            System.out.println("----------------------------------------------");

            Assert.assertEquals(response.statusCode(), 400, "Expected status code to be 400");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during API request: " + e.getMessage());
        }
    }


    /*** GET Request Utility - Positive Scenario ***/
    @Test(priority = 3)
    public void testUtility_Positive() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/health");

        System.out.println("Response StatusCode is: " + response.statusCode() + ", GET Request Utility - Positive Scenario");
        System.out.println("Response Body Utility positive: " + response.getBody().asString());
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code to be 200"); // Verify status code
        Assert.assertEquals(response.contentType(), "application/json", "Expected contentType to be application/json"); // Verify status code
    }

    /*** GET Request Utility - Negative Scenario ***/
    @Test(priority = 4)
    public void testUtility_Negative() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/nonexistent");

        System.out.println("Response StatusCode is: " + response.statusCode() + ", GET Request Utility - Negative Scenario");
        System.out.println();
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code to be 404");
    }

    /*** GET Request Utility - Positive Scenario ***/
    @Test(priority = 5)
    public void testLogin_PositiveRandomData() {

        String loginPayload = "{"
                + "\"username\": \"" + username + "\","
                + "\"password\": \"" + password + "\""
                + "}";

        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL)
                .body(loginPayload)
                .when()
                .post("/login");

        System.out.println("Response StatusCode2 is: " + response.statusCode() + ", GET Request Utility - Positive Scenario");
        System.out.println("Response Body Login negative : " + response.getBody().asString());
        System.out.println();
        System.out.println("----------------------------------------------");

        String authToken = response.jsonPath().getString("access-token"); // Adjust the key based on your API's response
        System.out.println("Auth2 Token: " + authToken);

        Assert.assertNotNull(response, "Response should not be null.");

        String message = response.jsonPath().getString("message"); // Extract message
        Assert.assertEquals(message, "Login successful", "Expected login success message"); // Verify message
        System.out.println("Message is: " + message);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code to be 200");
    }

    /*** Post Request Login - Positive Scenario ***/
    @Test(priority = 6)
    public void testLogin_PositiveStuckData() {

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
            String message = jsonResponse.getString("message"); // Extract message
            Assert.assertEquals(message, "Login successful", "Expected login success message"); // Verify message
            System.out.println("Message is: " + message);

            Assert.assertNotNull(response, "Response should not be null.");

            String token = jsonResponse.getString("access-token"); // Extract token
            System.out.println("Token is: " + token);

            Assert.assertEquals(jsonResponse.getString("access-token"), token, "Expected access-token valid token"); // Verify message

            System.out.println("Response StatusCode is: " + response.statusCode() + ", Post Request Login - Positive Scenario");
            System.out.println();
            System.out.println("----------------------------------------------");

            Assert.assertNotNull(token, "Access token should not be null"); // Verify token is not null
            Assert.assertEquals(response.statusCode(), 200, "Expected status code to be 200"); // Compare status codes

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during API request: " + e.getMessage());
        }
    }

    /*** Post Request Login - Negative Scenario 1 ***/
    @Test(priority = 7)
    public void testLogin_NegativeInvalidPassword() throws IOException, InterruptedException {

        String loginPayload = "{ \"username\": \"Kikonen3\", \"password\": \"passw123Ki\" }";
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL)
                .body(loginPayload)
                .when()
                .post("/login");

        System.out.println("Response StatusCode is: " + response.statusCode() + ", Post Request Login - Negative Scenario");
        System.out.println("Response Body Utility positive: " + response.getBody().asString());
        System.out.println("----------------------------------------------");
        String errorMessage = response.jsonPath().getString("error");;
        Assert.assertEquals(errorMessage, "Invalid password", "Error message should match.");
        Assert.assertEquals(response.statusCode(), 401, "Expected status code to be 401");
    }

    /*** Post Request Login - Negative Scenario 1 ***/
    @Test(priority = 8)
    public void testLogin_NegativeInvalidUserName() throws IOException, InterruptedException {

        String loginPayload = "{ \"username\": \"rtr\", \"password\": \"passw123Ki\" }";
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL)
                .body(loginPayload)
                .when()
                .post("/login");

        System.out.println("Response StatusCode is: " + response.statusCode() + ", Post Request Login - Negative Scenario");
        System.out.println("Response Body Utility positive: " + response.getBody().asString());
        System.out.println("----------------------------------------------");
        String errorMessage = response.jsonPath().getString("error");;
        Assert.assertEquals(errorMessage, "User does not exist", "Error message should match.");
        Assert.assertEquals(response.statusCode(), 401, "Expected status code to be 401");
    }


    /*** GET Request - User Positive Scenario***/
    @Test(priority = 9)
    public void testUser_Positive() {
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjEsImlhdCI6MTY3Mjc2NjAyOCwiZXhwIjoxNjc0NDk0MDI4fQ.kCak9sLJr74frSRVQp0_27BY4iBCgQSmoT3vQVWKzJg";
        int validUser = 1259;

        Response response = RestAssured
                .given()
                .header("Authorization", validToken)
                .baseUri(BASE_URL)
                .get("/user/" + validUser);

        System.out.println("Response StatusCode is: " + response.statusCode() + ", GET Request User - Positive Scenario");
        System.out.println("Response Body User negative 1: " + response.getBody().asString());
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code to be 200"); // Verify status code
        Assert.assertEquals(response.getContentType(), "application/json", "Expected JSON response"); // Verify JSON format

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Token is required"), "Response body should contain 'Token is required'");
    }

    /*** GET Request - User Negative Scenario 1 - Token is required ***/
    @Test(priority = 10)
    public void testUser_Negative_1() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/user/11");

        System.out.println("Response StatusCode is: " + response.statusCode() + ", GET Request User - Negative Scenario");
        System.out.println("Response Body User negative 1: " + response.getBody().asString());
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code to be 400"); // Verify status code
        Assert.assertEquals(response.getContentType(), "application/json", "Expected JSON response"); // Verify JSON format

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Token is required"), "Response body should contain 'Token is required'");
    }

    /*** GET Request - User Negative Scenario 2 - Invalid token ***/
    @Test(priority = 11)
    public void testUser_Negative_2() {

        String invalidToken = "fgdd56578";

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", invalidToken)
                .when()
                .get("/user/23");

        System.out.println("Response StatusCode is: " + response.statusCode() + ", GET Request - Negative 2 User Scenario");
        String responseBody = response.getBody().asString();
        System.out.println("Response Body User negative 2: " + responseBody);
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 401, "Expected status code to be 401");
        Assert.assertTrue(responseBody.contains("Invalid token"), "Response body should contain 'Invalid token'");
    }

    /*** GET Request - User Negative Scenario 3 - User not found ***/
    @Test(priority = 12)
    public void testUser_Negative_3() {

        String validToken = "111";
        int invalidUserID = 9908;

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", validToken)
                .when()
                .get("/user/" + invalidUserID);

        String responseBody = response.getBody().asString();
        System.out.println("Response StatusCode is: " + response.statusCode() + ", GET Request - Negative 3 User Scenario");
        System.out.println();
        System.out.println("----------------------------------------------");

        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code to be 404");
        Assert.assertTrue(responseBody.contains("User not found"), "Response body should contain 'User not found'");
    }
}
