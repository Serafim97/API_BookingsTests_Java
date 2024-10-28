package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookingTests extends BaseTest{

    @Test
    public void deleteBookingTestMethod(){
        //create booking
        Response responeCreate = createBooking();

        responeCreate.print();

        //get id of newly created booking
        int bookingid = responeCreate.jsonPath().getInt("bookingid");

        Response respone = RestAssured.given(spec)
                .auth()
                .preemptive()
                .basic("admin","password123")
                .delete("/booking/" + bookingid);

        //verify resp status code
        Assert.assertEquals( respone.getStatusCode(),201,"status should have been 200");

        Response respone2 = RestAssured.given(spec).get("/booking/"+bookingid);
        Assert.assertEquals(respone2.getBody().asString(),"Not Found","wrong message");
    }
}
