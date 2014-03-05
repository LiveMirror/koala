import business.ContractApplication
import org.dayatang.domain.InstanceFactory

class BusinesslogConfig {

    def context


    def InvoiceApplicationImpl_addInvoice() {

        ContractApplication contractApplication =
            InstanceFactory.getInstance(ContractApplication.class, "contractApplication")

        String xx = contractApplication.addContract(22)


        return "add invoice " + xx
    }

    def ProjectApplicationImpl_findSomeProjects() {
        println "ProjectApplicationImpl_findSomeProjects "
    }

}