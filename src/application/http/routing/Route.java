package application.http.routing;

import application.http.HttpRequest;

public class Route {

    private String method;
    private String path;

    public static Route route(String method, String path) {
        return new Route(method, path);
    }

    public Route(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public boolean matches(HttpRequest request) {
        return request.method.equals(method) && request.path.equals(path);
    }
}
