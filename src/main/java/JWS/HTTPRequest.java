package JWS;

/* HTTPRequest
*    A container class for a given HTTP request.
    Note: does validation on request method.
*/

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

    private enum requestMethods {
        GET,
        POST
        ;

        static boolean contains(String value) {
            for (requestMethods method : requestMethods.values()) {
                if (method.name().equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static HTTPRequest create(String request) {
        // TODO: More rigorous validation on the request.
        if (request.isBlank()) {
            return new HTTPRequest(null, null);
        }

        String requestLine = request.split("\n")[0];
        String[] requestLineSplit = requestLine.split(" ");
        String requestMethod = requestLineSplit[0];
        String requestPath = requestLineSplit[1];

        if (!requestMethods.contains(requestMethod)) {
            return new HTTPRequest(null, requestPath);
        }

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
