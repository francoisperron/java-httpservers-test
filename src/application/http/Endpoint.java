package application.http;

public interface Endpoint {
    HttpResponse handle(HttpRequest request);
}
