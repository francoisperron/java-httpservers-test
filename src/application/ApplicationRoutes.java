package application;

import application.http.Endpoint;
import application.http.HttpRequest;
import application.http.HttpResponse;
import application.http.routing.Route;

import java.util.HashMap;

import static application.http.routing.Route.route;

public class ApplicationRoutes extends HashMap<Route, Endpoint>{

    public static HashMap<Route, Endpoint> applicationRoutes(){
        return new ApplicationRoutes();
    }

    public ApplicationRoutes() {
        put(route("GET",  "/"), (request) -> spin200msAndAnswer(request));
        put(route("POST", "/"), (request) -> postResponse(request));
        put(route("GET", "/another"), (request) -> getResponse());
    }

    private HttpResponse spin200msAndAnswer(HttpRequest request) {
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < 200){}

        HttpResponse response = new HttpResponse();
        response.code = 200;
        response.body = helloOrGreetings(request);
        return response;
    }

    private String helloOrGreetings(HttpRequest request) {
        if (request.query == null || request.query.isEmpty())
            return "Hello";
        else
            return request.query.split("=")[1];
    }

    private HttpResponse postResponse(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        response.code = 201;
        response.body = request.body;
        return response;
    }

    private HttpResponse getResponse() {
        HttpResponse response = new HttpResponse();
        response.code = 200;
        response.body = "Bye";
        return response;
    }
}
