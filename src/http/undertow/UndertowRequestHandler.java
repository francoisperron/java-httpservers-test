package http.undertow;

import application.http.Endpoint;
import application.http.HttpRequest;
import application.http.HttpResponse;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

class UndertowRequestHandler implements HttpHandler {

    private Endpoint endpoint;

    public UndertowRequestHandler(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        handleRequestInWorkerThread(exchange);
    }

    private void handleRequestInWorkerThread(HttpServerExchange exchange) {
        HttpRequest request = buildRequest(exchange);
        HttpResponse response = endpoint.handle(request);
        sendResponse(exchange, response);
    }

    private HttpRequest buildRequest(HttpServerExchange exchange) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.method = exchange.getRequestMethod().toString();
        httpRequest.path = exchange.getRequestURI();
        httpRequest.query = exchange.getQueryString();
        httpRequest.body = readRequestBody(exchange);
        return httpRequest;
    }

    private String readRequestBody(HttpServerExchange exchange) {
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
}
