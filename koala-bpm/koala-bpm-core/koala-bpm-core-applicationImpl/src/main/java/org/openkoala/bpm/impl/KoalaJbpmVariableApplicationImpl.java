package org.openkoala.bpm.impl;

import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;
import javax.inject.Named;

import org.openkoala.bpm.application.KoalaJbpmVariableApplication;
import org.openkoala.bpm.application.vo.*;
import org.openkoala.bpm.core.*;
import org.openkoala.exception.extend.KoalaException;
import org.apache.commons.beanutils.BeanUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;

@Named
public class KoalaJbpmVariableApplicationImpl implements
		KoalaJbpmVariableApplication {

	private static QueryChannelService queryChannel;

	public static QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(
					QueryChannelService.class, "queryChannel");
		}
		return queryChannel;
	}

	public KoalaJbpmVariableVO getKoalaJbpmVariable(Long id) {

		String jpql = "select _koalaJbpmVariable from KoalaJbpmVariable _koalaJbpmVariable  where _koalaJbpmVariable.id=:id";
		KoalaJbpmVariable koalaJbpmVariable = (KoalaJbpmVariable) getQueryChannelService()
				.createJpqlQuery(jpql).addParameter("id", id).singleResult();
		KoalaJbpmVariableVO koalaJbpmVariableVO = new KoalaJbpmVariableVO();
		// 将domain转成VO
		try {
			BeanUtils.copyProperties(koalaJbpmVariableVO, koalaJbpmVariable);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return koalaJbpmVariableVO;
	}

	public KoalaJbpmVariableVO saveKoalaJbpmVariable(
			KoalaJbpmVariableVO koalaJbpmVariableVO) {
		KoalaJbpmVariable koalaJbpmVariable = new KoalaJbpmVariable();
		if (KoalaJbpmVariable.isVariableExists(koalaJbpmVariableVO.getScope(),
				koalaJbpmVariableVO.getKey())) {
			throw new KoalaException("101", "变量已存在，不能重复新增");
		}
		try {
			BeanUtils.copyProperties(koalaJbpmVariable, koalaJbpmVariableVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		koalaJbpmVariable.save();
		return koalaJbpmVariableVO;
	}

	public void updateKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariableVO) {
		KoalaJbpmVariable koalaJbpmVariable = KoalaJbpmVariable.get(
				KoalaJbpmVariable.class, koalaJbpmVariableVO.getId());
		// 设置要更新的值
		try {
			BeanUtils.copyProperties(koalaJbpmVariable, koalaJbpmVariableVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeKoalaJbpmVariable(Long id) {
		this.removeKoalaJbpmVariables(new Long[] { id });
	}

	public void removeKoalaJbpmVariables(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			KoalaJbpmVariable koalaJbpmVariable = KoalaJbpmVariable.load(
					KoalaJbpmVariable.class, ids[i]);
			koalaJbpmVariable.remove();
		}
	}

	public List<KoalaJbpmVariableVO> findAllKoalaJbpmVariable() {
		List<KoalaJbpmVariableVO> list = new ArrayList<KoalaJbpmVariableVO>();
		List<KoalaJbpmVariable> all = KoalaJbpmVariable
				.findAll(KoalaJbpmVariable.class);
		for (KoalaJbpmVariable koalaJbpmVariable : all) {
			KoalaJbpmVariableVO koalaJbpmVariableVO = new KoalaJbpmVariableVO();
			// 将domain转成VO
			try {
				BeanUtils
						.copyProperties(koalaJbpmVariableVO, koalaJbpmVariable);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(koalaJbpmVariableVO);
		}
		return list;
	}

	public Page<KoalaJbpmVariableVO> pageQueryKoalaJbpmVariable(
			KoalaJbpmVariableVO queryVo, int currentPage, int pageSize) {
		List<KoalaJbpmVariableVO> result = new ArrayList<KoalaJbpmVariableVO>();
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder(
				"select _koalaJbpmVariable from KoalaJbpmVariable _koalaJbpmVariable   where 1=1 ");

		if (queryVo.getKey() != null && !"".equals(queryVo.getKey())) {
			jpql.append(" and _koalaJbpmVariable.key like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryVo.getKey()));
		}

		if (queryVo.getValue() != null && !"".equals(queryVo.getValue())) {
			jpql.append(" and _koalaJbpmVariable.value like ?");
			conditionVals
					.add(MessageFormat.format("%{0}%", queryVo.getValue()));
		}

		if (queryVo.getType() != null && !"".equals(queryVo.getType())) {
			jpql.append(" and _koalaJbpmVariable.type like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryVo.getType()));
		}

		if (queryVo.getScope() != null && !"".equals(queryVo.getScope())) {
			jpql.append(" and _koalaJbpmVariable.scope like ?");
			conditionVals
					.add(MessageFormat.format("%{0}%", queryVo.getScope()));
		}

		Page<KoalaJbpmVariable> pages = getQueryChannelService()
				.createJpqlQuery(jpql.toString()).setParameters(conditionVals)
				.setPage(currentPage, pageSize).pagedList();
		for (KoalaJbpmVariable koalaJbpmVariable : pages.getData()) {
			KoalaJbpmVariableVO koalaJbpmVariableVO = new KoalaJbpmVariableVO();
			// 将domain转成VO
			try {
				BeanUtils
						.copyProperties(koalaJbpmVariableVO, koalaJbpmVariable);

				String scope = koalaJbpmVariableVO.getScope();
				if (scope.startsWith(KoalaJbpmVariable.KOALA_GLOBAL)) {
					koalaJbpmVariableVO
							.setScopeType(KoalaJbpmVariable.KOALA_GLOBAL);
					koalaJbpmVariableVO.setScope("");
				}
				if (scope.startsWith(KoalaJbpmVariable.KOALA_PACKAGE)) {
					koalaJbpmVariableVO
							.setScopeType(KoalaJbpmVariable.KOALA_PACKAGE);
					koalaJbpmVariableVO
							.setScope(scope
									.substring(KoalaJbpmVariable.KOALA_PACKAGE
											.length()));
				}
				if (scope.startsWith(KoalaJbpmVariable.KOALA_PROCESS)) {
					koalaJbpmVariableVO
							.setScopeType(KoalaJbpmVariable.KOALA_PROCESS);
					koalaJbpmVariableVO
							.setScope(scope
									.substring(KoalaJbpmVariable.KOALA_PROCESS
											.length()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			result.add(koalaJbpmVariableVO);
		}
		return new Page<KoalaJbpmVariableVO>(pages.getStart(),
				pages.getResultCount(), pages.getPageSize(), result);
	}

}
