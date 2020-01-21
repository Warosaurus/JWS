import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceResolver {

    public enum defaultResources {
        PAGE_INDEX("index.html"),
        PAGE_404("404.html"),
        PAGE_500("500.html")
        ;

        String name;

        defaultResources(String name) {
            this.name = name;
        }
    }

    public String get(URL relativePath) throws IOException {
        return resourceAsString(relativePath);
    }

    public String error() {
        try {
            return get(getRelativePath(defaultResources.PAGE_500.name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String notFound() {
        try {
            return get(getRelativePath(defaultResources.PAGE_404.name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public URL getRelativePath(String resource) {
        if (resource.equals("/")) {
            resource = defaultResources.PAGE_INDEX.name;
        }
        // TODO: resolve the relative path for other file types. i.e. JS, CSS, etc..
        if (resource.startsWith("/")) {
            resource = "html" + resource;
        }
        else {
            resource = "html/" + resource;
        }
        return Server.class.getResource(resource);
    }

    private String resourceAsString(URL resourcePath) throws IOException {
        Path path = Paths.get(resourcePath.getPath());
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            return null;
        }
        StringBuilder response = new StringBuilder();
        Files.lines(path).forEach(response::append);
        return response.toString();
    }
}
