package org.openkoala.businesslog;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

/**
 * User: zjzhai
 * Date: 2/26/14
 * Time: 10:40 AM
 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTest {

    @Test
    public void test1_create_company() throws Exception {
        Request.Post("http://localhost:8080/organization/create-company.koala?parentId=1")
                .bodyForm(Form.form().add("sn", "company1").add("name", "广州分公司").build()).elementCharset("UTF-8")
                .execute();
        Thread.sleep(1000);

    }


    @Test
    public void test22_() throws IOException {

        Request.Post("http://localhost:8080/job/create.koala")
                .bodyForm(Form.form().add("sn", "jingli").add("name", "经理").build())
                .elementCharset("UTF-8")
                .execute();
    }

}
