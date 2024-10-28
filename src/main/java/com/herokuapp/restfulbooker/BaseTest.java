package com.herokuapp.restfulbooker;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected RequestSpecification spec;

    @BeforeMethod
    public void setUp() {
        spec = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .build();
    }

    protected Response createBooking() {
        JSONObject body = new JSONObject();
        body.put("firstname", "cool");
        body.put("lastname", "guy");
        body.put("totalprice", 150);
        body.put("depositpaid", true);

        JSONObject datebody = new JSONObject();
        datebody.put("checkin", "2019-02-10");
        datebody.put("checkout", "2020-02-10");

        body.put("bookingdates", datebody);
        body.put("additionalneeds", "no needs");

        //get response
        Response respone = RestAssured.given(spec)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .post("/booking");

        return respone;
    }
}
