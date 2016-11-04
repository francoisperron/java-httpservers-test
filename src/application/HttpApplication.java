package application;

import application.http.Server;
import http.sunserver.SunHttpServer;
import http.simple.SimpleServer;
import http.jettyservlet.JettyServletServer;
import http.undertow.UndertowServer;

import static application.ApplicationRoutes.applicationRoutes;
import static application.http.routing.Router.routing;

public class HttpApplication {

    private Server server;
    private int port;

    public HttpApplication(int port) {
        this.port = port;
    }

    public void start() {
//        server = new SunHttpServer(port, routing(applicationRoutes()));
//        server = new SimpleServer(port, routing(applicationRoutes()));
//        server = new JettyServletServer(port, routing(applicationRoutes()));
        server = new UndertowServer(port, routing(applicationRoutes()));
        server.start();
    }

    public void stop() {
        server.stop();
    }

    public static void main(String[] args) {
        new HttpApplication(8080).start();
        System.out.println(":o)");
    }
}
