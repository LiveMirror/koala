package businesslog;

import business.*;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.businesslog.model.DefaultBusinessLog;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Ignore
public class BusinessLogInterceptorTest extends AbstractIntegrationTest {

    @Inject
    private ContractApplication contractApplication;

    @Inject
    private InvoiceApplication invoiceApplication;

    @Inject
    private ProjectApplication projectApplication;

    @Test
    public void testFindProjects() throws ClassNotFoundException, InterruptedException {

        ThreadLocalBusinessLogContext.put("user", "张三");
        ThreadLocalBusinessLogContext.put("time", new Date());
        ThreadLocalBusinessLogContext.put("ip", "202.11.22.33");


        invoiceApplication.addInvoice("发票编号", 1l);

        invoiceApplication.addInvoice("发票编号2", 22l);

        List<String> names = new ArrayList<String>();

        names.add("1");
        names.add("2");
        names.add("3");
        names.add("4");

        projectApplication.findSomeProjects(names);


        assert DefaultBusinessLog.findAll(DefaultBusinessLog.class).size() == 3;


    }

}
