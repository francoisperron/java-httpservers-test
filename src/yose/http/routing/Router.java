package yose.http.routing;

import yose.http.Endpoint;
import yose.http.HttpRequest;
import yose.http.HttpResponse;

import java.util.Map;

public class Router {

    private Map<Route, Endpoint> routes;

    public Router(Map<Route, Endpoint> routes) {
        this.routes = routes;
    }

    public HttpResponse handle(HttpRequest request) {
        return endpointMappedTo(request).handle(request);
    }

    private Endpoint endpointMappedTo(HttpRequest request) {
        return routes.entrySet().stream().filter(r -> r.getKey().matches(request)).findFirst().get().getValue();
    }
}
