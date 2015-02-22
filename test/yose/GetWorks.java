package yose;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yose.http.HttpResponse;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static yose.testsupport.HttpGetRequest.get;

public class GetWorks {

    private Yose yose;

    @Before
    public void startYose() throws Exception {
        yose = new Yose(8000);
        yose.start();
    }

    @After
    public void stopYose() throws IOException {
        yose.stop();
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
