package com.herokuapp.restfulbooker;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UpdateBookingTests extends BaseTest {

    @Test
    public void updateBookingTest() {
        //create booking
        Response responeCreate = createBooking();

        responeCreate.print();

        //get id of newly created booking
        int bookingid = responeCreate.jsonPath().getInt("bookingid");

        //update booking
        JSONObject body = new JSONObject();
        body.put("firstname", "hot");
        body.put("lastname", "guy");
        body.put("totalprice", 160);
        body.put("depositpaid", true);

        JSONObject datebody = new JSONObject();
        datebody.put("checkin", "2019-02-10");
        datebody.put("checkout", "2020-02-10");

        body.put("bookingdates", datebody);
        body.put("additionalneeds", "no needs");

        //get response
        Response respone = RestAssured.given(spec)
                .auth()
                .preemptive()
                .basic("admin","password123")
                .contentType(ContentType.JSON)
                .body(body.toString())
                .put("/booking/" + bookingid);

        //verifications
        Assert.assertEquals( respone.getStatusCode(),200,"status should have been 200");
        SoftAssert softAssert = new SoftAssert();
        String actualFstName = respone.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFstName, "hot", "unexpected fst name");

        int price = respone.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 160, "unexpected price");

        boolean paid = respone.jsonPath().getBoolean("depositpaid");
        softAssert.assertTrue(paid, "should have been true");

        String actualCheckIn = respone.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckIn, "2019-02-10", "unexpected checkin date");

        softAssert.assertAll();
    }
}
