package JWS;

enum ResponseCode {
    // Successful codes
    OK("200 OK"),

    // Client Error
    BAD_REQUEST("400 Bad Request"),
    NOT_FOUND("404 Not Found"),

    // Server error
    SERVER_ERROR("500 Internal Server Error"),
    SERVICE_UNAVAILABLE("503 Service Unavailable"),
    ;

    String code;

    ResponseCode(String code) {
        this.code = code;
    }

    String response() {
        return this.code;
    }
}
