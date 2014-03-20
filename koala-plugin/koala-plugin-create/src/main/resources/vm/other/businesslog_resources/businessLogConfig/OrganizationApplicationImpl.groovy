package vm.other.businesslog_resources.businessLogConfig

class OrganizationApplicationImpl {

    def context

    def OrganizationApplicationImpl_createAsTopOrganization() {
        "${getPreTemplate()}:创建顶层机构:${context._param0.name}"
    }

    def OrganizationApplicationImpl_createCompany() {
        "${getPreTemplate()}:为${context._param0.name},创建分公司:${context._param1.name}"
    }

    def OrganizationApplicationImpl_assignChildOrganization() {
        "${getPreTemplate()}:向${context._param0.name},分配子机构:${context._param1.name},期限为${context._param2}"
    }

    def OrganizationApplicationImpl_createDepartment() {
        "${getPreTemplate()}:在${context._param0.name}下创建部门:${context._param1.name}"
    }

    def OrganizationApplicationImpl_terminateEmployeeOrganizationRelation() {
        "${getPreTemplate()}:终止机构${context._param0.name}"
    }

    def getPreTemplate(){
        "${context._user}-"
    }



}