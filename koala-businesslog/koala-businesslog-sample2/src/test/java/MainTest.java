import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

/**
 * User: zjzhai
 * Date: 2/26/14
 * Time: 10:40 AM
 */
public class MainTest {

    @Ignore
    @Test
    public void testName() throws Exception {
        Request.Post("http://localhost:8080/organization/employee/test")
                .bodyForm(Form.form().add("s11n", "vip11").add("name", "secret").build())
                .execute();

    }
}
