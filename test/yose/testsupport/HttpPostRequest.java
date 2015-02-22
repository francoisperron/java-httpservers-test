package yose.testsupport;

import yose.http.HttpResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpPostRequest {

    public static HttpResponse post(String uri, String body) {
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            writeBody(connection, body);
            connection.connect();
            return readResponse(connection);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeBody(HttpURLConnection connection, String body) throws IOException {
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();
    }

    private static HttpResponse readResponse(HttpURLConnection connection) throws IOException {
        HttpResponse response = new HttpResponse();
        response.code = connection.getResponseCode();
        response.body = readBody(connection);
        return response;
    }

    private static String readBody(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        return br.ready() ? br.lines().collect(Collectors.joining("\n")) : "";
    }
}
