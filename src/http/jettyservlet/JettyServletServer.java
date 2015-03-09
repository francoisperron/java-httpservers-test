package http.jettyservlet;

import application.http.Endpoint;
import application.http.HttpRequest;
import application.http.HttpResponse;
import application.http.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class JettyServletServer implements Server {

    private org.eclipse.jetty.server.Server server;

    public JettyServletServer(int port, Endpoint endpoint) {
        server = new org.eclipse.jetty.server.Server(port);
        server.setHandler(handleTo(endpoint));
    }

    private AbstractHandler handleTo(final Endpoint endpoint) {
        return new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
                HttpRequest request = buildRequest(httpServletRequest);
                HttpResponse response = endpoint.handle(request);
                sendResponse(httpServletResponse, response);
                baseRequest.setHandled(true);
            }
        };
    }

    private HttpRequest buildRequest(HttpServletRequest httpServletRequest) {
        HttpRequest request = new HttpRequest();
        request.query = httpServletRequest.getQueryString();
        request.path = httpServletRequest.getRequestURI();
        request.method = httpServletRequest.getMethod();
        request.body = readRequestBody(httpServletRequest);
        return request;
    }

    private static String readRequestBody(HttpServletRequest httpServletRequest) {
        try {
            BufferedReader br = httpServletRequest.getReader();
            return br.ready() ? br.lines().collect(Collectors.joining("\n")) : "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(HttpServletResponse httpServletResponse, HttpResponse response) throws IOException {
        response.headers.forEach((header, value) -> httpServletResponse.setHeader(header, value));
        httpServletResponse.setStatus(response.code);
        httpServletResponse.getWriter().println(response.body);
    }

    @Override
    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
