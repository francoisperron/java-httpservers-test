package http.jettyservlet;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import yose.http.Endpoint;
import yose.http.HttpRequest;
import yose.http.HttpResponse;
import yose.http.Server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        return request;
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
