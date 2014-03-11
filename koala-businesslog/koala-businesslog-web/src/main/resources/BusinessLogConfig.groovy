import business.ContractApplication
import org.dayatang.domain.InstanceFactory

class BusinessLogConfig {

    def context

    def InvoiceApplicationImpl_addInvoice() {

        "sample"
        
    }

    def ProjectApplicationImpl_findSomeProjects() {

        "ProjectApplicationImpl_findSomeProjects ${context.xx}"
    }

}