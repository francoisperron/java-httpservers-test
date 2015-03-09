package application.http.routing;

import application.http.Endpoint;
import application.http.HttpRequest;
import application.http.HttpResponse;

import java.util.Map;

public class Router implements Endpoint {

    private Map<Route, Endpoint> routes;

    public static Endpoint routing(Map<Route, Endpoint> routes) {
        return new Router(routes);
    }

    private Router(Map<Route, Endpoint> routes) {
        this.routes = routes;
    }

    public HttpResponse handle(HttpRequest request) {
        return endpointMappedTo(request).handle(request);
    }

    private Endpoint endpointMappedTo(HttpRequest request) {
        return routes.entrySet().stream().filter(r -> r.getKey().matches(request)).findFirst().get().getValue();
    }
}
