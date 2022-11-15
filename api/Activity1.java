package activities;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class Activity1 {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    int petId;

    @BeforeClass
    public void setUp() {
        //Request specification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .setContentType(ContentType.JSON)
                .build();

        //Response Specification
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody("status", equalTo("alive"))
                .expectResponseTime(lessThan(5000L))
                .build();
    }

    @Test(priority = 1)
    public void postRequesttest() {
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 77242);
        reqBody.put("name", "Oreo");
        reqBody.put("status", "alive");

        //generate response
        Response response = given().spec(requestSpec).body(reqBody).log().all()
                .when().post();
        petId = response.then().extract().body().path("id");
        response.then().spec(responseSpec).log().all();

        response.then().body("id", equalTo(77242));
        response.then().body("name", equalTo("Oreo"));
        response.then().body("status", equalTo("alive"));

    }

    @Test(priority = 2)
    public void getRequestTest() {
        // Generate response
        Response response =
                given().spec(requestSpec).log().all().pathParam("petId", petId)
                        .when().get("/{petId}");

        response.then().body("id", equalTo(77242));
        response.then().body("name", equalTo("Oreo"));
        response.then().body("status", equalTo("alive"));
    }


    @Test(priority = 3)
    public void deleteRequestTest() {
        // Generate response
        given().spec(requestSpec).log().all().pathParam("petId", petId)
                .when().delete("/{petId}")
                .then().log().all().statusCode(200).body("message", equalTo("" + petId));
    }
}