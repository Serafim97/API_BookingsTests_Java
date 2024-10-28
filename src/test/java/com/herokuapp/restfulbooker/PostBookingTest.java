package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PostBookingTest extends BaseTest {

    @Test
    public void createBookingTest() {
        //create json body
        //get response
        Response respone = createBooking();


        respone.print();
        //verifications
        SoftAssert softAssert = new SoftAssert();
        String actualFstName = respone.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFstName, "cool", "unexpected fst name");

        int price = respone.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 150, "unexpected price");

        boolean paid = respone.jsonPath().getBoolean("depositpaid");
        softAssert.assertTrue(paid, "should have been true");

        String actualCheckIn = respone.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckIn, "2019-02-10", "unexpected checkin date");

        softAssert.assertAll();
    }

    @Test
    public void createBookingWithSerializationTest() {

        //pjo = plain old java object
        //create json body using pjos
        Bookingdates bookingdates = new Bookingdates("2016-12-12", "2016-11-11");
        Booking booking = new Booking("cool", "guy", 144, true, bookingdates, "no needs");

        //get response
        Response respone = RestAssured.given(spec)
                .contentType(ContentType.JSON)
                .body(booking)
                .post("/booking");
        respone.print();
        Bookingid bookingid = respone.as(Bookingid.class);
        System.out.println("Request booking: " + booking);
        System.out.println("Response booking: " + bookingid.getBooking());
        //verifications
        Assert.assertEquals(bookingid.getBooking().toString(), booking.toString(), "objects didn't match");
    }
}