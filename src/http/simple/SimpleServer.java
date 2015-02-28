package http.simple;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import yose.http.Endpoint;
import yose.http.HttpRequest;
import yose.http.HttpResponse;
import yose.http.Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;

public class SimpleServer implements Server {

    private Connection connection;
    private int port;
    private Endpoint endpoint;

    public SimpleServer(int port, Endpoint endpoint) {
        this.port = port;
        this.endpoint = endpoint;
    }

    @Override
    public void start() {
        try {
            SocketProcessor server = new ContainerSocketProcessor(this::handleSimpleRequest, 100);
            connection = new SocketConnection(server);
            connection.connect(new InetSocketAddress(port));
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSimpleRequest(Request simpleRequest, Response simpleResponse) {
        HttpRequest request = buildRequest(simpleRequest);
        HttpResponse response = endpoint.handle(request);
        sendResponse(simpleResponse, response);
    }

    private HttpRequest buildRequest(Request simpleRequest) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.method = simpleRequest.getMethod();
        httpRequest.path = simpleRequest.getPath().toString();
        return httpRequest;
    }

    private void sendResponse(Response simpleResponse, HttpResponse response) {
        try{
            simpleResponse.setStatus(Status.getStatus(response.code));
            PrintStream body = simpleResponse.getPrintStream();
            body.print(response.body);
            body.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
