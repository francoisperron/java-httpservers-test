package yose;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sunserver.SunHttpServer;
import yose.http.Endpoint;
import yose.http.HttpResponse;
import yose.http.Server;
import yose.http.routing.Route;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static yose.http.routing.Router.routing;
import static yose.testsupport.HttpGetRequest.get;

public class GetWorks {

    private Server server;
    private Map<Route, Endpoint> routes = new HashMap<Route, Endpoint>()
    {{
        put(Route.withPathEqualTo("/"), (request) -> {
            HttpResponse response = new HttpResponse();
            response.code = 200;
            response.body = "Hello";
            return response;
        });
        put(Route.withPathEqualTo("/another"), (request) -> {
            HttpResponse response = new HttpResponse();
            response.code = 200;
            response.body = "Bye";
            return response;
        });
    }};

    @Before
    public void startServer() throws Exception {
        server = new SunHttpServer(8000, routing(routes));
        server.start();
    }

    @After
    public void stopServer() throws IOException {
        server.stop();
    }

    @Test
    public void returns200() {
        HttpResponse response = get("http://localhost:8000/");

        assertThat(response.code, equalTo(200));
    }

    @Test
    public void returnsHello() {
        HttpResponse response = get("http://localhost:8000/");

        assertThat(response.body, equalTo("Hello"));
    }

    @Test
    public void routesToAnotherEndpoint() {
        HttpResponse response = get("http://localhost:8000/another");

        assertThat(response.body, equalTo("Bye"));
    }
}
