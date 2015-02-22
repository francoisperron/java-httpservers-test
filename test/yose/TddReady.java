package yose;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class TddReady {

    @Test
    public void testFrameworkWorks(){
        assertThat(true, equalTo(true));
    }
}
