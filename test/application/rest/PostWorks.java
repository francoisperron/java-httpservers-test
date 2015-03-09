package application.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import application.HttpApplication;
import application.http.HttpResponse;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static application.testsupport.HttpPostRequest.post;

public class PostWorks {

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
    public void returns201() {
        HttpResponse response = post("http://localhost:8000/", "");

        assertThat(response.code, equalTo(201));
    }

    @Test
    public void returnsGivenBody(){
        HttpResponse response = post("http://localhost:8000/", "look at my body!");

        assertThat(response.body, equalTo("look at my body!"));
    }
}
