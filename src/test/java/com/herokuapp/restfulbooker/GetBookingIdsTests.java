package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static io.restassured.RestAssured.*;

public class GetBookingIdsTests extends BaseTest{

    @Test
    public void getBookingIdsWithoutFilterTest(){
        //get response
        Response respone = RestAssured.given(spec).get("/booking");
        respone.print();
        //verify resp status code
        Assert.assertEquals( respone.getStatusCode(),200,"status should have been 200");

        //verify at least 1 booking id in resp
        List<Integer> bookingIds = respone.jsonPath().getList("bookingId");
        Assert.assertFalse(bookingIds.isEmpty(),"list is empty");
    }

    @Test
    public void getBookingIdContent(){
        Response responeCreate = createBooking();

        responeCreate.print();

        //get id of newly created booking
        int bookingid = responeCreate.jsonPath().getInt("bookingid");

        //Set parameters
        spec.pathParam("bookingId",bookingid);

        //get response with booking
        Response respone = RestAssured.given(spec).get("/booking/{bookingId}");
        respone.print();
        //verify resp status code
        Assert.assertEquals( respone.getStatusCode(),200,"status should have been 200");

        //verify content
        //Assert.assertTrue(respone.getBody().asString().contains("Mark"));
        SoftAssert softAssert = new SoftAssert();
        String actualFstName = respone.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFstName,"cool","unexpected fst name");

        int price = respone.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price,150,"unexpected price");

        boolean paid = respone.jsonPath().getBoolean("depositpaid");
        softAssert.assertTrue(paid,"should have been true");

        String actualCheckIn = respone.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckIn,"2019-02-10","unexpected checkin date");

        softAssert.assertAll();
    }

    @Test
    public void getBookingIdsWithFilterTest(){
        //add query param to spec
        spec.queryParam("firstname","John");
        spec.queryParam("lastname","Smith");
        //get response
        Response respone = RestAssured.given(spec).get("/booking");
        respone.print();
        //verify resp status code
        Assert.assertEquals( respone.getStatusCode(),200,"status should have been 200");

        //verify at least 1 booking id in resp
        List<Integer> bookingIds = respone.jsonPath().getList("bookingId");
        Assert.assertFalse(bookingIds.isEmpty(),"list is empty");

    }

    @Test
    public void getBookingIdContentXML(){
        Response responeCreate = createBooking();

        responeCreate.print();

        //get id of newly created booking
        int bookingid = responeCreate.jsonPath().getInt("bookingid");

        //Set parameters
        spec.pathParam("bookingId",bookingid);

        //get response with booking
        Header xml = new Header("Accept","application/xml");
        spec.header(xml);
        Response respone = RestAssured.given(spec)
                .get("/booking/{bookingId}");
        respone.print();
        //verify resp status code
        Assert.assertEquals( respone.getStatusCode(),200,"status should have been 200");

        //verify content
        //Assert.assertTrue(respone.getBody().asString().contains("Mark"));
        SoftAssert softAssert = new SoftAssert();
        String actualFstName = respone.xmlPath().getString("booking.firstname");
        softAssert.assertEquals(actualFstName,"cool","unexpected fst name");

        int price = respone.xmlPath().getInt("booking.totalprice");
        softAssert.assertEquals(price,150,"unexpected price");

        boolean paid = respone.xmlPath().getBoolean("booking.depositpaid");
        softAssert.assertTrue(paid,"should have been true");

        String actualCheckIn = respone.xmlPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckIn,"2019-02-10","unexpected checkin date");

        softAssert.assertAll();
    }

}
