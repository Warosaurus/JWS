package JWS;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HTTPRequestTest {

    @Test
    void testGET() {
        HTTPRequest request = HTTPRequest.create("GET /");
        Assertions.assertEquals("GET", request.getRequestMethod());
        Assertions.assertEquals("/", request.getRequestPath());
    }

    @Test
    void testMethodNotSupported() {
        HTTPRequest request = HTTPRequest.create("TEST /");
        Assertions.assertNull(request.getRequestMethod());
        Assertions.assertEquals("/", request.getRequestPath());
    }
}
