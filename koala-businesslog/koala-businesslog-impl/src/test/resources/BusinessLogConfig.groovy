import business.ContractApplication
import org.dayatang.domain.InstanceFactory

class BusinessLogConfig {

    def context

    def InvoiceApplicationImpl_addInvoice() {

        ContractApplication contractApplication =
            InstanceFactory.getInstance(ContractApplication.class, "contractApplication")

        String xx = contractApplication.addContract(22)

        "add invoice " + xx
        
    }

    def ProjectApplicationImpl_findSomeProjects() {

        "ProjectApplicationImpl_findSomeProjects ${context.xx}"
    }

}
