package yose;

import sunserver.SunHttpServer;
import yose.http.Server;

import java.io.IOException;

import static yose.YoseRoutes.yoseRoutes;
import static yose.http.routing.Router.routing;

public class Yose {

    private Server server;
    private int port;

    public Yose(int port) {
        this.port = port;
    }

    public void start() {
        try {
            server = new SunHttpServer(port, routing(yoseRoutes()));
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.stop();
    }

    public static void main(String[] args) {
        new Yose(8080).start();
    }
}
