package businessLogConfig

import business.ContractApplication
import org.dayatang.domain.InstanceFactory

/**
 * User: zjzhai
 * Date: 3/6/14
 * Time: 2:32 PM
 */
class InvoiceConfig99 {

    def context

    def InvoiceApplicationImpl_addInvoice() {
        ContractApplication contractApplication =
            InstanceFactory.getInstance(ContractApplication.class, "contractApplication")

        String xx = contractApplication.addContract(22)

        [category: "发票",
                log : "add invoice " + xx]
    }
}
