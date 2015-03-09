package http.undertow;

import application.http.Endpoint;
import application.http.HttpRequest;
import application.http.HttpResponse;
import application.http.Server;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import org.xnio.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class UndertowServer implements Server {

    private Undertow server;
    private int port;
    private Endpoint endpoint;

    public UndertowServer(int port, Endpoint endpoint) {
        this.port = port;
        this.endpoint = endpoint;
    }

    @Override
    public void start() {
        server = Undertow.builder()
//                .setBufferSize(1024 * 16) // no performance gain with these
//                .setIoThreads(100)
//                .setWorkerThreads(100)
                .setSocketOption(Options.BACKLOG, 10000) // to stop dropping connections
                .addHttpListener(port, "localhost")
                .setHandler(exchange -> handleRequest(exchange))
                .build();
        server.start();
    }

    private void handleRequest(HttpServerExchange exchange) {
        HttpRequest request = buildRequest(exchange);
        HttpResponse response = endpoint.handle(request);
        sendResponse(exchange, response);
    }

    private HttpRequest buildRequest(HttpServerExchange exchange) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.method = exchange.getRequestMethod().toString();
        httpRequest.path = exchange.getRequestURI();
        httpRequest.body = readRequestBody(exchange);
        return httpRequest;
    }

    private static String readRequestBody(HttpServerExchange exchange) {
        exchange.startBlocking();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader((exchange.getInputStream())));
            return br.ready() ? br.lines().collect(Collectors.joining("\n")) : "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(HttpServerExchange exchange, HttpResponse response) {
        exchange.setResponseCode(response.code);
        exchange.getResponseSender().send(response.body);
    }

    @Override
    public void stop() {
        server.stop();
    }
}
