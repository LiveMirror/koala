package businesslog;

import business.ContractApplication;
import business.InvoiceApplication;
import business.ProjectApplication;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: zjzhai
 * Date: 3/4/14
 * Time: 8:45 AM
 */
@Ignore
public class MainTest extends AbstractIntegrationTest {
    @Inject
    private ContractApplication contractApplication;

    @Inject
    private InvoiceApplication invoiceApplication;

    @Inject
    private ProjectApplication projectApplication;

    @Test
    public void testName() throws Exception {
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


    }
}
