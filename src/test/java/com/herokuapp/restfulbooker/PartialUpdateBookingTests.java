package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PartialUpdateBookingTests extends BaseTest{

    @Test
    public void partialUpdateBookingTestMethod(){
        Response responeCreate = createBooking();

        responeCreate.print();

        //get id of newly created booking
        int bookingid = responeCreate.jsonPath().getInt("bookingid");

        //update booking
        JSONObject body = new JSONObject();
        body.put("firstname", "hot2");
        body.put("depositpaid", false);

        JSONObject datebody = new JSONObject();
        datebody.put("checkin", "2019-02-11");
        datebody.put("checkout", "2020-02-11");
        body.put("bookingdates", datebody);

        //get response
        Response respone = RestAssured.given(spec)
                .auth()
                .preemptive()
                .basic("admin","password123")
                .contentType(ContentType.JSON)
                .body(body.toString())
                .patch("/booking/" + bookingid);
        respone.print();
        //verifications
        Assert.assertEquals( respone.getStatusCode(),200,"status should have been 200");
        SoftAssert softAssert = new SoftAssert();
        String actualFstName = respone.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFstName, "hot2", "unexpected fst name");

        int price = respone.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 150, "unexpected price");

        boolean paid = respone.jsonPath().getBoolean("depositpaid");
        softAssert.assertTrue(!paid, "should have been false");

        String actualCheckIn = respone.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckIn, "2019-02-11", "unexpected checkin date");

        softAssert.assertAll();
    }
}
