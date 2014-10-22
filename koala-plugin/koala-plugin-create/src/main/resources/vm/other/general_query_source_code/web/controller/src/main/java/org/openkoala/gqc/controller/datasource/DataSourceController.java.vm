package org.openkoala.gqc.controller.datasource;

import javax.inject.Inject;

import org.dayatang.utils.Page;
import org.openkoala.gqc.facade.DataSourceFacade;
import org.openkoala.gqc.facade.dto.DataSourceDTO;
import org.openkoala.koala.commons.InvokeResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * 数据源控制层
 * 
 */
@Controller
@RequestMapping("/dataSource")
public class DataSourceController {

	/**
	 * 数据源应用层接口实例
	 */
	@Inject
	private DataSourceFacade dataSourceFacade;

	/**
	 * 增加数据源
	 * 
	 * @param dsDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public InvokeResult add(DataSourceDTO dsDTO) {
		return dataSourceFacade.createDataSource(dsDTO);
	}

	/**
	 * 更新数据源
	 * 
	 * @param dataSourceVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public InvokeResult update(DataSourceDTO dto) {
		return dataSourceFacade.updateDataSource(dto);
	}

	/**
	 * 分页查询数据源列表
	 * 
	 * @param page
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageJson")
	public Page<DataSourceDTO> pageJson(int page, int pagesize) {
		return dataSourceFacade.pageQueryDataSource(new DataSourceDTO(), page, pagesize);
	}

	/**
	 * 删除数据源
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public InvokeResult delete(String ids) {
		if (ids == null) {
			return InvokeResult.failure("数据源ID不能为空");
		}
		String[] idArrs = ids.split(",");
		Long[] idsLong = new Long[idArrs.length];
		for (int i = 0; i < idArrs.length; i++) {
			idsLong[i] = Long.parseLong(idArrs[i]);
		}
		return dataSourceFacade.removeDataSources(idsLong);
	}

	/**
	 * 查询指定数据源
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get/{id}")
	public DataSourceDTO get(@PathVariable("id") Long id) {
		return dataSourceFacade.getById(id);
	}

	/**
	 * 检测数据源是否可用，修改页面
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkDataSourceById")
	public InvokeResult checkDataSourceById(Long id) {
		return dataSourceFacade.checkDataSourceCanConnect(id);
	}

	/**
	 * 检测数据源是否可用，新增页面
	 * 
	 * @param dataSourceVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkDataSource")
	public InvokeResult checkDataSourceById(DataSourceDTO dataSourceDTO) {
		return dataSourceFacade.checkDataSourceCanConnect(dataSourceDTO);
	}
}