package application.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import application.HttpApplication;
import application.http.HttpResponse;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static application.testsupport.HttpGetRequest.get;

public class GetWorks {

    private HttpApplication application;

    @Before
    public void start() throws Exception {
        application = new HttpApplication(8000);
        application.start();
    }

    @After
    public void stop() throws IOException {
        application.stop();
    }

    @Test
    public void returns200() {
        HttpResponse response = get("http://localhost:8000/");

        assertThat(response.code, equalTo(200));
    }

    @Test
    public void returnsHello() {
        HttpResponse response = get("http://localhost:8000/");

        assertThat(response.body, equalTo("Hello"));
    }

    @Test
    public void routesToAnotherEndpoint() {
        HttpResponse response = get("http://localhost:8000/another");

        assertThat(response.body, equalTo("Bye"));
    }
}
