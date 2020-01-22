package JWS;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class HTTPResponse {

    private Map<String, String> headerMap;
    private String status;
    private String header;
    private String body;

    private HTTPResponse(HTTPRequest request) {
        this.headerMap = new HashMap<>();

        headerMap.put("Content-Type", "text/html; charset=utf-8");
        headerMap.put("Server", "JWS");
        headerMap.put("Accept-Ranges", "bytes");
        headerMap.put("Date", ZonedDateTime.now().toString());

        body = createBody(request.getRequestPath());

        headerMap.put("Content-Length", String.valueOf(body.getBytes().length));
        header = createHeader();
    }

    public static HTTPResponse from(HTTPRequest request) {
        return new HTTPResponse(request);
    }

    private String createBody(String resource) {
        ResourceResolver resolver = new ResourceResolver();
        URL resourcePath = resolver.getRelativePath(resource);

        String responseBody;
        try {
            if (resourcePath != null) {
                status = ResponseCode.OK.code;
                responseBody = resolver.get(resourcePath);
            } else {
                status = ResponseCode.NOT_FOUND.code;
                responseBody = resolver.notFound();
            }
        } catch (IOException e) {
            status = ResponseCode.SERVER_ERROR.code;
            responseBody = resolver.error();
        }
        return responseBody;
    }

    private String createHeader() {

        StringBuilder headerBuilder = new StringBuilder("HTTP/1.1 ").append(status).append("\n");

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            headerBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        // Add new line to separate header from body
        headerBuilder.append("\n");

        return headerBuilder.toString();
    }

    public ByteBuffer getHeader() {
        return ByteBuffer.wrap(header.getBytes());
    }

    public ByteBuffer getBody() {
        return ByteBuffer.wrap(body.getBytes());
    }

    @Override
    public String toString() {
        return "JWS.HTTPResponse{\n" +
                "header:\n" + header +
                "body:\n" + body +
                '}';
    }
}
