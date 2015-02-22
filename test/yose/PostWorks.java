package yose;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yose.http.HttpResponse;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static yose.testsupport.HttpPostRequest.post;

public class PostWorks {

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
