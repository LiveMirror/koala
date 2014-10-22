package org.openkoala.gqc.controller.generalquery;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.utils.Page;
import org.openkoala.gqc.facade.DataSourceFacade;
import org.openkoala.gqc.facade.GqcFacade;
import org.openkoala.gqc.facade.dto.DataSourceDTO;
import org.openkoala.gqc.facade.dto.GeneralQueryDTO;
import org.openkoala.koala.commons.InvokeResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * 通用查询控制层
 *
 */
@Controller
@RequestMapping("/generalquery")
public class GeneralQueryController {

	/**
	 * 数据源应用层接口实例
	 */
	@Inject
	private DataSourceFacade dataSourceFacade;

	/**
	 * 查询通道应用层接口实例
	 */
	@Inject
	private GqcFacade gqcFacade;

	/**
	 * 分页查询通用查询列表
	 * 
	 * @param page
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageJson")
	public Page<GeneralQueryDTO> pageJson(int page, int pagesize, String queryName) {
		if (queryName != null && !queryName.isEmpty()) {
			return gqcFacade.getQueriersByName(queryName, page, pagesize);
		} else {
			return gqcFacade.getQueriers(page, pagesize);
		}
	}

	/**
	 * 新增
	 * 
	 * @param generalQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public InvokeResult add(GeneralQueryDTO dto) {
		//查出所属的dataSource的dto
		dto.setDataSourceDTO(dataSourceFacade.getById(dto.getDataSourceDTO().getId()));
		dto.setCreateDate(new Date());
		return gqcFacade.createQuerier(dto);
	}

	/**
	 * 通过主键查询
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getById")
	public GeneralQueryDTO getById(Long id) {
		return gqcFacade.getQuerier(id);
	}

	/**
	 * 更新
	 * 
	 * @param generalQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public InvokeResult update(GeneralQueryDTO dto) {
		return gqcFacade.updateQuerier(dto);
	}

	/**
	 * 查询所有数据源
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllDataSource")
	public List<DataSourceDTO> findAllDataSource() {
		try {
			return dataSourceFacade.findAllDataSource();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public InvokeResult delete(String ids) {
		String[] sidList = ids.split(",");
		Long[] lidList = new Long[sidList.length];
		for (int i = 0; i < sidList.length; i++) {
			lidList[i] = Long.parseLong(sidList[i]);
		}
		return gqcFacade.removeQueriers(lidList);
	}

	/**
	 * 获取指定数据源所有的表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllTable")
	public List<String> findAllTable(Long id) {
		try {
			List<String> tableList = dataSourceFacade.findAllTable(id);
			Collections.sort(tableList);
			return tableList;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取指定表的所有列
	 * 
	 * @return
	 * @param id
	 *            数据源主键
	 * @param tableName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllColumn")
	public Map<String, Integer> findAllColumn(Long id, String tableName) {
		try {
			return dataSourceFacade.findAllColumn(id, tableName);
		} catch (Exception e) {
			return null;
		}
	}
}
