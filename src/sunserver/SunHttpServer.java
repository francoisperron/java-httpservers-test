package sunserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import yose.http.HttpRequest;
import yose.http.HttpResponse;
import yose.http.Server;
import yose.http.routing.Router;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SunHttpServer implements Server {

    private final HttpServer server;
    private Router router;

    public SunHttpServer(int port) throws IOException {
        int defaultMaximumNumberOfTcpConnectionQueued = 0;
        server = HttpServer.create(new InetSocketAddress(port), defaultMaximumNumberOfTcpConnectionQueued);
    }

    @Override
    public void start() {
        server.createContext("/", exchange -> handle(exchange));
        server.start();
    }

    @Override
    public void stop() {
        int now = 0;
        server.stop(now);
    }

    @Override
    public void useRouter(Router router) {
        this.router = router;
    }

    private void handle(HttpExchange exchange) throws IOException {
        HttpRequest request = buildRequest(exchange);
        HttpResponse response = router.handle(request);
        sendResponse(exchange, response);
    }

    private HttpRequest buildRequest(HttpExchange exchange) {
        HttpRequest request = new HttpRequest();
        request.query = exchange.getRequestURI().getRawQuery();
        request.path = exchange.getRequestURI().getPath();
        return request;
    }

    private void sendResponse(HttpExchange exchange, HttpResponse response) throws IOException {
        response.headers.forEach((header, value) -> exchange.getResponseHeaders().add(header, value));
        int useChunkedEncoding = 0;
        exchange.sendResponseHeaders(response.code, useChunkedEncoding);
        exchange.getResponseBody().write(response.body.getBytes());
        exchange.close();
    }
}
