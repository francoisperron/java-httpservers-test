package application.testsupport;

import static application.testsupport.HttpGetRequest.get;

public class Getter implements Runnable {
    private final String uri;
    public int responseCode;
    public long elapsedTime;
    public Exception exception;

    Getter(String url) {
        this.uri = url;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            responseCode = get(uri).code;
        } catch (Exception e) {
            exception = e;
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.print(".");
    }
}
