package org.openkoala.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.dayatang.querychannel.Page;
import org.dayatang.utils.DateUtils;
import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.auth.application.ResourceTypeApplication;
import org.openkoala.auth.application.vo.ResourceTypeVO;
import org.openkoala.koala.auth.core.domain.ResourceType;
import org.openkoala.koala.auth.core.domain.ResourceTypeAssignment;
import org.springframework.transaction.annotation.Transactional;

@Named
@Remote
@Stateless(name = "ResourceTypeApplication")
@Transactional(value = "transactionManager_security")
// @Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
public class ResourceTypeApplicationImpl extends BaseImpl implements ResourceTypeApplication {

	public boolean isExist(ResourceTypeVO resourceTypeVO) {
		String queryJpql = "from ResourceType o where o.name=:name and o.abolishDate>:abolishDate";

		ResourceType resourceType = (ResourceType) queryChannel().createJpqlQuery(queryJpql).addParameter("name", resourceTypeVO.getName())
				.addParameter("abolishDate", new Date()).singleResult();

		if (resourceType != null) {
			return true;
		}
		return false;
	}

	public void save(ResourceTypeVO resourceTypeVO) {
		if (isExist(resourceTypeVO)) {
			throw new ApplicationException("resourceType.exist");
		}

		ResourceType resourceType = new ResourceType();
		resourceType.setName(resourceTypeVO.getName());
		resourceType.setCreateDate(new Date());
		resourceType.setAbolishDate(DateUtils.MAX_DATE);
		resourceType.setSerialNumber("0");
		resourceType.setSortOrder(0);
		resourceType.save();
		resourceTypeVO.setId(String.valueOf(resourceType.getId()));
	}

	public void delete(ResourceTypeVO resourceTypeVO) {
		delete(new ResourceTypeVO[] { resourceTypeVO });
	}

	public void delete(ResourceTypeVO[] resourceTypeVOs) {
		for (ResourceTypeVO resourceTypeVO : resourceTypeVOs) {
			ResourceType resourceType = ResourceType.load(ResourceType.class, Long.valueOf(resourceTypeVO.getId()));
			// 如果ResourceType已经被引用,则抛出异常
			if (resourceType.getResources().size() > 0) {
				throw new ApplicationException("resourceType.hasResource");
			}
			resourceType.setAbolishDate(new Date());
			removeResourceTypeAssignment(resourceType);
		}
	}

	/**
	 * 删除与资源的关系
	 * 
	 * @param resourceType
	 */
	private void removeResourceTypeAssignment(ResourceType resourceType) {
		for (ResourceTypeAssignment assignment : resourceType.getResources()) {
			assignment.setAbolishDate(new Date());
		}
	}

	public void update(ResourceTypeVO resourceTypeVO) {
		ResourceType resourceType = ResourceType.load(ResourceType.class, Long.valueOf(resourceTypeVO.getId()));

		if (resourceType.getName().equals(resourceTypeVO.getName())) {
			return;
		}

		if (isExist(resourceTypeVO)) {
			throw new ApplicationException("resourceType.exist");
		}

		resourceType.setName(resourceTypeVO.getName());
	}

	public Page<ResourceTypeVO> pageQuery(int currentPage, int pageSize) {
		String queryJpql = "from ResourceType o where o.name<>:name1 and o.name<>:name2 and o.abolishDate>:abolishDate";

		Page<ResourceType> page = queryChannel().createJpqlQuery(queryJpql).addParameter("name1", "KOALA_MENU").addParameter("name2", "KOALA_DIRETORY")
				.addParameter("abolishDate", new Date()).setPage(currentPage - 1, pageSize).pagedList();

		List<ResourceTypeVO> list = new ArrayList<ResourceTypeVO>();
		for (ResourceType resourceType : page.getData()) {
			ResourceTypeVO resourceTypeVO = new ResourceTypeVO();
			resourceTypeVO.setId(String.valueOf(resourceType.getId()));
			resourceTypeVO.setName(resourceType.getName());
			list.add(resourceTypeVO);
		}
		return new Page<ResourceTypeVO>(page.getStart(), page.getResultCount(), page.getPageSize(), list);
	}

	public List<ResourceTypeVO> findResourceType() {
		List<ResourceType> resourceTypes = ResourceType.findAllResourceType();
		List<ResourceTypeVO> result = new ArrayList<ResourceTypeVO>();
		for (ResourceType resourceType : resourceTypes) {
			ResourceTypeVO resourceTypeVO = new ResourceTypeVO();
			resourceTypeVO.setId(String.valueOf(resourceType.getId()));
			resourceTypeVO.setName(resourceType.getName());
			resourceTypeVO.setText(resourceType.getName());
			result.add(resourceTypeVO);
		}
		return result;
	}

	public List<ResourceTypeVO> findMenuType() {
		List<ResourceTypeVO> result = new ArrayList<ResourceTypeVO>();
		for (ResourceType resourceType : ResourceType.findMenuType()) {
			ResourceTypeVO resourceTypeVO = new ResourceTypeVO();
			resourceTypeVO.setId(String.valueOf(resourceType.getId()));
			resourceTypeVO.setName(resourceType.getName());
			result.add(resourceTypeVO);
		}
		return result;
	}

}
