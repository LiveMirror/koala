package org.openkoala.application.impl;

import java.text.MessageFormat;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.QueryItemVO;

public class BaseImpl {
	private static QueryChannelService queryChannel;

	public static QueryChannelService queryChannel() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_ss");
		}
		return queryChannel;
	}

	protected String genQueryCondition(QueryConditionVO search) {
		StringBuilder result = new StringBuilder(MessageFormat.format("select m from {0} m where 1=1",
				search.getObjectName()));
		for (QueryItemVO qi : search.getItems()) {
			result.append(MessageFormat.format(" and m.{0} {1} {2}", qi.getPropName(),
					QueryConditionVO.genOperatorStirng(qi.getOperaType()), qi.getPropValue()));
		}
		result.append(" and m.abolishDate>:abolishDate");
		return result.toString();
	}

}
