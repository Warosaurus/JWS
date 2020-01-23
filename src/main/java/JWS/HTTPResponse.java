package JWS;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        headerMap.put("Date", requestTimeStamp());

        body = createBody(request);
        headerMap.put("Content-Length", String.valueOf(body.getBytes().length));
        header = createHeader();
    }

    public static HTTPResponse from(HTTPRequest request) {
        return new HTTPResponse(request);
    }

    private String requestTimeStamp() {
        return ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"))
                                  .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private String createBody(HTTPRequest request) {
        ResourceResolver resolver = new ResourceResolver();
        String resource = request.getRequestPath();

        if (request.getRequestMethod() == null || request.getRequestPath() == null) {
            status = ResponseCode.BAD_REQUEST.code;
            return resolver.error(ResponseCode.BAD_REQUEST);
        }

        URL resourcePath = null;
        if (resource != null) {
            resourcePath = resolver.getRelativePath(resource);
        }

        String responseBody;
        try {
            if (resourcePath != null) {
                status = ResponseCode.OK.code;
                responseBody = resolver.get(resourcePath);
            } else {
                status = ResponseCode.NOT_FOUND.code;
                responseBody = resolver.error(ResponseCode.NOT_FOUND);
            }
        } catch (IOException e) {
            status = ResponseCode.SERVER_ERROR.code;
            responseBody = resolver.error(ResponseCode.SERVER_ERROR);
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

    public ByteBuffer getHeaderAsByteBuffer() {
        return ByteBuffer.wrap(header.getBytes());
    }

    public ByteBuffer getBodyAsByteBuffer() {
        return ByteBuffer.wrap(body.getBytes());
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "JWS.HTTPResponse{\n" +
                "header:\n" + header +
                "body:\n" + body +
                '}';
    }
}
