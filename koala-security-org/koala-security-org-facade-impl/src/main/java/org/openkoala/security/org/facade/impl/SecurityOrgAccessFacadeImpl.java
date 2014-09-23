package org.openkoala.security.org.facade.impl;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.core.domain.EmployeeUser;
import org.openkoala.security.org.facade.SecurityOrgAccessFacade;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;
import org.openkoala.security.org.facade.dto.OrganizationScopeDTO;
import org.openkoala.security.org.facade.impl.assembler.EmployeeUserAssembler;

import javax.inject.Named;
import java.text.MessageFormat;
import java.util.*;

@Named
public class SecurityOrgAccessFacadeImpl implements SecurityOrgAccessFacade {

    @Override
    public List<OrganizationScopeDTO> findAllOrganizationScopesTree() {
        List<OrganizationScopeDTO> results = findTopOrganizationScopes();
        List<OrganizationScopeDTO> childrenOrganizationScopeDTOs = findChidrenOrganizationScopes();
        Set<OrganizationScopeDTO> all = new HashSet<OrganizationScopeDTO>();
        all.addAll(results);
        all.addAll(childrenOrganizationScopeDTOs);
        addOrganizationScopeChildrenToParent(all);
        return results;
    }

    @Override
    public InvokeResult pagingQueryEmployeeUsers(int pageIndex, int pageSize, EmployeeUserDTO queryEmployeeUserCondition) {
        Map<String, Object> conditionVals = new HashMap<String, Object>();
        StringBuilder jpql = new StringBuilder("FROM EmployeeUser _user where 1=1");
        assembleUserJpqlAndConditionValues(queryEmployeeUserCondition, jpql, "_user", conditionVals);

        Page<EmployeeUser> results = getQueryChannelService().createJpqlQuery(jpql.toString())//
                .setParameters(conditionVals)//
                .setPage(pageIndex, pageSize)//
                .pagedList();

        return InvokeResult.success(new Page<EmployeeUserDTO>(results.getStart(), results.getResultCount(), pageSize, transformTo(results.getData())));
    }

    private List<EmployeeUserDTO> transformTo(List<EmployeeUser> employeeUsers) {
        List<EmployeeUserDTO> results = new ArrayList<EmployeeUserDTO>();
        for (EmployeeUser employeeUser : employeeUsers) {
            EmployeeUserDTO result = EmployeeUserAssembler.toEmployeeUserDTO(employeeUser);
            if(null != employeeUser.getEmployee()){
                result.setEmployeeName(employeeUser.getEmployee().getName());
            }
            results.add(result);
        }
        return results;
    }

    private void addOrganizationScopeChildrenToParent(Set<OrganizationScopeDTO> all) {
        LinkedHashMap<Long, OrganizationScopeDTO> map = new LinkedHashMap<Long, OrganizationScopeDTO>();
        for (OrganizationScopeDTO organizationScopeDTO : all) {
            map.put(organizationScopeDTO.getId(), organizationScopeDTO);
        }
        for (OrganizationScopeDTO organizationScopeDTO : map.values()) {
            Long parentId = organizationScopeDTO.getParentId();
            if (!StringUtils.isBlank(parentId + "") && map.get(parentId) != null) {
                map.get(parentId).getChildren().add(organizationScopeDTO);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<OrganizationScopeDTO> findChidrenOrganizationScopes() {

        StringBuilder jpql = new StringBuilder(
                "SELECT NEW org.openkoala.security.facade.dto.OrganizationScopeDTO(_organizationScope.id, _organizationScope.name, _organizationScope.description, _organizationScope.parent.id) FROM OrganizationScope _organizationScope");
        jpql.append(" WHERE _organizationScope.level > :level");
        jpql.append(" GROUP BY _organizationScope.id");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("level", 0);

        List<OrganizationScopeDTO> results = getQueryChannelService()//
                .createJpqlQuery(jpql.toString())//
                .setParameters(parameters)//
                .list();

        return results;
    }

    private List<OrganizationScopeDTO> findTopOrganizationScopes() {
        StringBuilder jpql = new StringBuilder(
                "SELECT NEW org.openkoala.security.facade.dto.OrganizationScopeDTO(_organizationScope.id, _organizationScope.name, _organizationScope.description, _organizationScope.parent.id) FROM OrganizationScope _organizationScope");
        jpql.append(" WHERE _organizationScope.parent IS NULL");
        jpql.append(" AND _organizationScope.level = :level");
        jpql.append(" GROUP BY _organizationScope.id");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("level", 0);

        List<OrganizationScopeDTO> results = getQueryChannelService()//
                .createJpqlQuery(jpql.toString())//
                .setParameters(parameters)//
                .list();

        return results;
    }

    private QueryChannelService queryChannelService;

    public QueryChannelService getQueryChannelService() {
        if (queryChannelService == null) {
            queryChannelService = InstanceFactory.getInstance(QueryChannelService.class,"queryChannel_security");
        }
        return queryChannelService;
    }

    private void assembleUserJpqlAndConditionValues(EmployeeUserDTO queryEmployeeUserCondition, StringBuilder jpql, String conditionPrefix, Map<String, Object> conditionVals) {

        String andCondition = " AND " + conditionPrefix;

        if (null != queryEmployeeUserCondition.getDisabled() && !"".equals(queryEmployeeUserCondition.getDisabled())) {
            jpql.append(andCondition);
            jpql.append(".disabled = :disabled");
            conditionVals.put("disabled", queryEmployeeUserCondition.getDisabled());
        }

        if (!StringUtils.isBlank(queryEmployeeUserCondition.getName())) {
            jpql.append(andCondition);
            jpql.append(".name LIKE :name");
            conditionVals.put("name", MessageFormat.format("%{0}%", queryEmployeeUserCondition.getName()));
        }
        if (!StringUtils.isBlank(queryEmployeeUserCondition.getUserAccount())) {
            jpql.append(andCondition);
            jpql.append(".userAccount LIKE :userAccount");
            conditionVals.put("userAccount", MessageFormat.format("%{0}%", queryEmployeeUserCondition.getUserAccount()));
        }
        if (!StringUtils.isBlank(queryEmployeeUserCondition.getEmail())) {
            jpql.append(andCondition);
            jpql.append(".email LIKE :email");
            conditionVals.put("email", MessageFormat.format("%{0}%", queryEmployeeUserCondition.getEmail()));
        }
        if (!StringUtils.isBlank(queryEmployeeUserCondition.getTelePhone())) {
            jpql.append(andCondition);
            jpql.append(".telePhone LIKE :telePhone");
            conditionVals.put("telePhone", MessageFormat.format("%{0}%", queryEmployeeUserCondition.getTelePhone()));
        }
        if (!StringUtils.isBlank(queryEmployeeUserCondition.getEmployeeName())) {
            jpql.append("AND .employee.name LIKE :employeeName");
            conditionVals.put("employeeName", MessageFormat.format("%{0}%", queryEmployeeUserCondition.getEmployeeName()));
        }


    }
}
