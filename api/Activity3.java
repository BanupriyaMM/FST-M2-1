package activities;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.HashMap;
import java.util.Map;

public class Activity3 {
    RequestSpecification requestSpec;
    // Declare response specification
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
                // Set content type
                .setContentType(ContentType.JSON)
                // Set base URL
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                // Build request specification
                .build();

        responseSpec = new ResponseSpecBuilder()
                // Check status code in response
                .expectStatusCode(200)
                // Check response content type
                .expectContentType("application/json")
                // Check if response contains name property
                .expectBody("status", equalTo("alive"))
                // Build response specification
                .build();
    }

    @DataProvider
    public Object[][] petInfoProvider() {
        // Setting parameters to pass to test case
        Object[][] testData = new Object[][] {
                { 77242, "Oreo", "alive" },
                { 77243, "Hansel", "alive" }
        };
        return testData;
    }

    @Test(priority=1)
    // Test case using a DataProvider
    public void addPets() {
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 77242);
        reqBody.put("name", "Oreo");
        reqBody.put("status", "alive");

        Response response = given().spec(requestSpec).body(reqBody).log().all()
                .when().post();

        //Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 77243);
        reqBody.put("name", "Hansel");
        reqBody.put("status", "alive");
        response = given().spec(requestSpec).body(reqBody).log().all()
                .when().post();

        // Assertions
        response.then().spec(responseSpec); // Use responseSpec
    }

    // Test case using a DataProvider
    @Test(dataProvider = "petInfoProvider", priority=2)
    public void getPets(int id, String name, String status) {
        Response response = given().spec(requestSpec) // Use requestSpec
                .pathParam("petId", id) // Add path parameter
                .when().get("/{petId}"); // Send GET request

        // Print response
        System.out.println(response.asPrettyString());
        // Assertions
        response.then()
                .spec(responseSpec) // Use responseSpec
                .body("name", equalTo(name)); // Additional Assertion
    }

    // Test case using a DataProvider
    @Test(dataProvider = "petInfoProvider", priority=3)
    public void deletePets(int id, String name, String status) {
        Response response = given().spec(requestSpec) // Use requestSpec
                .pathParam("petId", id) // Add path parameter
                .when().delete("/{petId}"); // Send GET request

        // Assertions
        response.then().statusCode(200);;
    }

}