package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HeadersAndCookiesTest extends BaseTest {

    @Test
    public void healthCheckTest() {
        Header someHeader = new Header("some name","some value");
        spec.header(someHeader);

        Cookie someCookie = new Cookie.Builder("some cookie","some flavour").build();
        spec.cookie(someCookie);

        Response response = RestAssured.given(spec)
                .cookies("Test cookie name","Test cookie value")
                .header("Test header name","Test header value")
                .log().all()
                .get("/ping");

        //get headers
        Headers headers = response.getHeaders();
        System.out.println("headers are: " + headers);

        Header serverHeader1 = headers.get("Server");
        System.out.println("server headers are: " + serverHeader1.getName() + " : " + serverHeader1.getValue());

        String headers2 = response.getHeader("Server");
        System.out.println("same server header is: " + headers2);
        //get cookies
        Cookies cookies = response.getDetailedCookies();
        System.out.println("cookies are: " + cookies);
    }
}
