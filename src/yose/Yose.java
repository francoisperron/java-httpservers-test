package yose;

import http.simple.SimpleServer;
import yose.http.Server;

import static yose.YoseRoutes.yoseRoutes;
import static yose.http.routing.Router.routing;

public class Yose {

    private Server server;
    private int port;

    public Yose(int port) {
        this.port = port;
    }

    public void start() {
        server = new SimpleServer(port, routing(yoseRoutes()));
        server.start();
    }

    public void stop() {
        server.stop();
    }

    public static void main(String[] args) {
        new Yose(8080).start();
        System.out.println(":o)");
    }
}
