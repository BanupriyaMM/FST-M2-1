package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers
    Map<String, String> headers = new HashMap<>();
    //resource path
    String resourcePath = "/api/users";

    //pact creation
    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        //headers
        headers.put("Content-Type","application/json");

        //Request and response body
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id",123)
                .stringType("firstName", "Madhu")
                .stringType("lastName","kisan")
                .stringType("email","madhu@example.com");
        //pact
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                    .method("POST")
                    .path(resourcePath)
                    .headers(headers)
                    .body(requestResponseBody)
                .willRespondWith()
                    .status(201)
                    .body(requestResponseBody)
                .toPact();

    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void postRequestTest(){
        String mockServer = "http://localhost:8282";
        //Request body
        Map<String,Object> reqBody = new HashMap<>();
        reqBody.put("id",77245);
        reqBody.put("firstName","fst");
        reqBody.put("lastName","lastnm");
        reqBody.put("email","fst1@example.com");

        //Generate response
        given().body(reqBody).headers(headers)
                .when().post(mockServer + resourcePath)
                .then().statusCode(201).log().all();
    }
}