package yose;

import yose.http.Endpoint;
import yose.http.HttpRequest;
import yose.http.HttpResponse;
import yose.http.routing.Route;

import java.util.HashMap;

import static yose.http.routing.Route.route;

public class YoseRoutes extends HashMap<Route, Endpoint>{

    public static HashMap<Route, Endpoint> yoseRoutes(){
        return new YoseRoutes();
    }

    public YoseRoutes() {
        put(route("GET",  "/"), (request) -> spin200msAndAnswer());
        put(route("POST", "/"), (request) -> postResponse(request));

        put(route("GET", "/another"), (request) -> getResponse());
    }

    private HttpResponse wait200msAndAnwer() {
        try {
            Thread.sleep(200);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HttpResponse response = new HttpResponse();
        response.code = 200;
        response.body = "Hello";
        return response;
    }

    public static HttpResponse spin200msAndAnswer() {
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < 200){}

        HttpResponse response = new HttpResponse();
        response.code = 200;
        response.body = "Hello";
        return response;
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
