package org.openkoala.businesslog;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

/**
 * User: zjzhai
 * Date: 2/26/14
 * Time: 10:40 AM
 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTest  {

    @Test
    public void test1_() throws Exception {
        for (int i = 0; i < 1000; i++) {
            Request.Post("http://localhost:8080/organization/create-company.koala?parentId=1")
                    .bodyForm(Form.form().add("sn", "vip11" + i).add("name", "secret" + i).build())
                    .execute();
            Thread.sleep(1000);
        }

        for (int i = 0; i < 1000; i++) {
            Request.Post("http://localhost:8080/organization/create-company.koala?parentId=1")
                    .bodyForm(Form.form().add("sn", "vip11" + i).add("name", "secret" + i).build())
                    .execute();
            Thread.sleep(1000);
        }

    }
}
