package Project;

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

public class GitProject {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    String sshKey;
    int sshKeyID;

    @BeforeClass
    public void setUp(){
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "token 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")
                .setBaseUri("https://api.github.com")
                .build();

    }
    @Test(priority = 1)
    public void postRequestTest(){
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("title" , "TestKey");
        reqBody.put("key", "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCQ+nMRy9F76tDjN4uBhp126DF8ioffGAtYnd36Y4D3qHV7gMFqQymaKTbXvoAJ1IR16nnZWR5gCT8Hpv0DZV0m7evi6Lcbl/Enbu6dOMbqv85tmPcQ2dXoYUr+uxacZKHXnFRW/s1gZ3V0vKv0vz6IW9Ua2qjSzvcuRtXFFKaFPxMSETD8EJyQ7Vn7cPM4sNw3gmTQIh3cyoc3HUljQsHoBzne3MWuVoRadIuPV8O2N0ze8dA6HjNkFr52WRK/Csbch2C5hwlEwYo+GBAOeplvj0bkxs5kY4FSwNkUyv11xj5IgEK3nejzHk/mMjeAYWSyLzBphNjxYorE32obPcd1");

        String resourcePath = "user/keys";
        Response response = given().spec(requestSpec).body(reqBody)
                .when().post(resourcePath);

        sshKeyID = response.then().extract().path("id");
        String resBody = response.getBody().asPrettyString();
        System.out.println(resBody);
        response.then().statusCode(201);
    }
    @Test(priority = 2)
    public void getRequestTest(){
        Response response = given().spec(requestSpec).pathParam("keyId",sshKeyID)
                .when().get("/user/keys/{keyId}");
        String resBody = response.getBody().asPrettyString();
        System.out.println(resBody);
        response.then().statusCode(200);
    }

    @Test(priority = 3)
    public void deleteRequestTest(){
        Response response = given().spec(requestSpec).pathParam("keyId",sshKeyID)
                .when().delete("/user/keys/{keyId}");
        String resBody = response.getBody().asPrettyString();
        System.out.println(resBody);
        response.then().statusCode(204);
    }
}