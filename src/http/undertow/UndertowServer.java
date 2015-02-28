package http.undertow;

import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import org.xnio.Options;
import yose.http.Endpoint;
import yose.http.HttpRequest;
import yose.http.HttpResponse;
import yose.http.Server;

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
        return httpRequest;
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
