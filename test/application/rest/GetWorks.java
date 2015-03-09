package application.rest;

import application.HttpApplication;
import application.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static application.testsupport.HttpGetRequest.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetWorks {

    private HttpApplication application;

    @Before
    public void start() {
        application = new HttpApplication(8000);
        application.start();
    }

    @After
    public void stop() {
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
    public void returnsQueryParamGreetingsAsBody() {
        HttpResponse response = get("http://localhost:8000?greetings=Bonjour");

        assertThat(response.body, equalTo("Bonjour"));
    }

    @Test
    public void routesToAnotherEndpoint() {
        HttpResponse response = get("http://localhost:8000/another");

        assertThat(response.body, equalTo("Bye"));
    }
}
