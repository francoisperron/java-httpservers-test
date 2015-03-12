package http.undertow;

import application.http.Endpoint;
import application.http.Server;
import io.undertow.Undertow;
import org.xnio.Options;

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
                .setBufferSize(1024 * 16)
                .setIoThreads(100)
                .setWorkerThreads(100)
                .setSocketOption(Options.BACKLOG, 10000)
                .addHttpListener(port, "localhost")
                .setHandler(new UndertowRequestHandler(endpoint))
                .build();
        server.start();
    }

    @Override
    public void stop() {
        server.stop();
    }
}
