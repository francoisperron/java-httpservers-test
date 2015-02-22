package yose;

import sunserver.SunHttpServer;
import yose.http.Endpoint;
import yose.http.HttpResponse;
import yose.http.Server;
import yose.http.routing.Route;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static yose.http.routing.Route.route;
import static yose.http.routing.Router.routing;

public class Yose {

    private Server server;
    private Map<Route, Endpoint> routes = new HashMap<Route, Endpoint>()
    {{
        put(route("GET", "/"), (request) -> {
            HttpResponse response = new HttpResponse();
            response.code = 200;
            response.body = "Hello";
            return response;
        });
        put(route("POST", "/"), (request) -> {
            HttpResponse response = new HttpResponse();
            response.code = 201;
            response.body = request.body;
            return response;
        });
        put(route("GET", "/another"), (request) -> {
            HttpResponse response = new HttpResponse();
            response.code = 200;
            response.body = "Bye";
            return response;
        });
    }};

    private int port;

    public Yose(int port) {
        this.port = port;
    }

    public void start() {
        try {
            server = new SunHttpServer(port, routing(routes));
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.stop();
    }
}
