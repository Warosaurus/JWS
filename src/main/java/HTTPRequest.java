public class HTTPRequest {

    private String requestMethod;
    private String requestPath;

    private HTTPRequest(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public static HTTPRequest create(String request) {
        String requestLine = request.split("\n")[0];
        String[] requestLineSplit = requestLine.split(" ");
        String requestMethod = requestLineSplit[0];
        String requestPath = requestLineSplit[1];
        return new HTTPRequest(requestMethod, requestPath);
    }

    @Override
    public String toString() {
        return "HTTPRequest{" +
                "requestMethod='" + requestMethod + '\'' +
                ", requestPath='" + requestPath + '\'' +
                '}';
    }
}
