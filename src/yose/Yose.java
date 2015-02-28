package yose;

import http.jettyservlet.JettyServletServer;
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
        server = new JettyServletServer(port, routing(yoseRoutes()));
//        server = new SimpleServer(port, routing(yoseRoutes()));
//        server = new UndertowServer(port, routing(yoseRoutes()));
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
