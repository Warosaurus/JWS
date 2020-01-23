package JWS;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HTTPResponseTest {

    @Test
    void testGetIndex() {
        HTTPRequest request = HTTPRequest.create("GET /");
        HTTPResponse response = HTTPResponse.from(request);

        String expectedBody = "<!DOCTYPE html><html><head>    <title>Index</title></head>" +
                "<body>    <p>Welcome!</p></body></html>";

        Assertions.assertAll("HTTPResponse get index does not behave as expected",
            () -> Assertions.assertTrue(response.getHeader().startsWith("HTTP/1.1 200 OK")),
            () -> Assertions.assertEquals(expectedBody, response.getBody())
        );
    }

    @Test
    void testGetNotFound() {
        HTTPRequest request = HTTPRequest.create("GET /not-found");
        HTTPResponse response = HTTPResponse.from(request);

        String expectedBody = "<!DOCTYPE html><html><head>    <title>Error: 404</title></head>" +
                "<body>    <p>Resource not found.</p></body></html>";

        Assertions.assertAll("HTTPResponse get 404 does not behave as expected",
            () -> Assertions.assertTrue(response.getHeader().startsWith("HTTP/1.1 404 Not Found")),
            () -> Assertions.assertEquals(expectedBody, response.getBody())
        );
    }

    @Test
    void testInvalidRequest() {
        HTTPRequest request = HTTPRequest.create("TEST /not-found");
        HTTPResponse response = HTTPResponse.from(request);

        String expectedBody = "<!DOCTYPE html><html><head>    <title>Error: 400</title></head>" +
                "<body>    <p>Bad request.</p></body></html>";

        Assertions.assertAll("HTTPResponse invalid request does not behave as expected",
            () -> Assertions.assertTrue(response.getHeader().startsWith("HTTP/1.1 400 Bad Request")),
            () -> Assertions.assertEquals(expectedBody, response.getBody()));
    }
}
