package http.sunserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import application.http.Endpoint;
import application.http.HttpRequest;
import application.http.HttpResponse;
import application.http.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SunHttpServer implements Server {

    private final HttpServer server;
    private Endpoint endpoint;

    public SunHttpServer(int port, Endpoint endpoint) {
        try {
            this.endpoint = endpoint;
            int defaultMaximumNumberOfTcpConnectionQueued = 0;
            server = HttpServer.create(new InetSocketAddress(port), defaultMaximumNumberOfTcpConnectionQueued);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        server.setExecutor(Executors.newFixedThreadPool(100));
        server.createContext("/", exchange -> handle(exchange));
        server.start();
    }

    @Override
    public void stop() {
        int now = 0;
        server.stop(now);
    }

    private void handle(HttpExchange exchange) throws IOException {
        HttpRequest request = buildRequest(exchange);
        HttpResponse response = endpoint.handle(request);
        sendResponse(exchange, response);
    }

    private HttpRequest buildRequest(HttpExchange exchange) {
        HttpRequest request = new HttpRequest();
        request.method = exchange.getRequestMethod();
        request.path = exchange.getRequestURI().getPath();
        request.query = exchange.getRequestURI().getRawQuery();
        request.body = readRequestBody(exchange);
        return request;
    }

    private static String readRequestBody(HttpExchange exchange) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader((exchange.getRequestBody())));
            return br.ready() ? br.lines().collect(Collectors.joining("\n")) : "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(HttpExchange exchange, HttpResponse response) throws IOException {
        response.headers.forEach((header, value) -> exchange.getResponseHeaders().add(header, value));
        int useChunkedEncoding = 0;
        exchange.sendResponseHeaders(response.code, useChunkedEncoding);
        exchange.getResponseBody().write(response.body.getBytes());
        exchange.close();
    }
}
