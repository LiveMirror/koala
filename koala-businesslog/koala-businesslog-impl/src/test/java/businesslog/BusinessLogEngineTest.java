package businesslog;

import static org.mockito.Mockito.*;
import business.*;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.InstanceProvider;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.impl.BusinessLogConsoleExporter;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQueryExecutor;
import org.openkoala.businesslog.impl.BusinessLogFreemarkerDefaultRender;
import org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/9/13
 * Time: 8:45 AM
 */
@Ignore
public class BusinessLogEngineTest {
    @Test
    public void testName() throws Exception {

        InstanceProvider provider = mock(InstanceProvider.class);
        String operation = "Invoice business.InvoiceApplication.addInvoice(String,long)";

        InstanceFactory.setInstanceProvider(provider);
        when((ProjectApplication)provider.getInstance(Class.forName("business.ProjectApplication"), "projectApplication"))
                .thenReturn(new ProjectApplicationImpl());

        when((ContractApplication)provider.getInstance(Class.forName("business.ContractApplication"), "contractApplication"))
                .thenReturn(new ContractApplicationImpl());

        BusinessLogXmlConfigDefaultAdapter adapter = new BusinessLogXmlConfigDefaultAdapter();
        BusinessLogEngine businessLogEngine = new BusinessLogEngine(
                adapter,
                new BusinessLogFreemarkerDefaultRender(),
                new BusinessLogDefaultContextQueryExecutor()
                );
        businessLogEngine.setInitContext(getContext());


        BusinessLogExporter exporter = new BusinessLogConsoleExporter();



        assert "向项目项目xxxx的合同一期合同添加发票：编号： yyyyyy".equals(
                businessLogEngine.exportLogBy(operation, exporter).getLog());
    }


    private Map<String, Object> getContext() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("user", "张三");
        result.put("ip", "192.168.1.1");
        result.put("_param0", "ppp");
        result.put("_param1", 1l);
        result.put("_methodReturn", new Invoice("编号： yyyyyy"));
        Contract contract = new Contract();
        contract.setProject(new Project("op"));
        contract.setInvoice(new Invoice("编号： okkokokk"));
        result.put("contract", contract);
        return result;
    }
}
