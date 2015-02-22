package yose.testsupport;

import yose.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpGetRequest {

    public static HttpResponse get(String uri) {
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return readResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpResponse readResponse(HttpURLConnection connection) throws IOException {
        HttpResponse response = new HttpResponse();
        response.code = connection.getResponseCode();
        response.body = readBody(connection);
        return response;
    }

    private static String readBody(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        return br.lines().collect(Collectors.joining("\n"));
    }
}
