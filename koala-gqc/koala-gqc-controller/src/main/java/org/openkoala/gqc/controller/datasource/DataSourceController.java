package org.openkoala.gqc.controller.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.facade.DataSourceFacade;
import org.openkoala.gqc.facade.dto.DataSourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
	 * @param dataSourceVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(DataSourceDTO dataSourceVO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();

		try {
			String errorMsg = dataSourceFacade.saveDataSource(dataSourceVO);
			if (errorMsg == null) {
				dataMap.put("result", "success");
			} else {
				dataMap.put("result", errorMsg);
			}
		} catch (Exception e) {
			dataMap.put("result", "新增失败！");
			e.printStackTrace();
		}

		return dataMap;
	}

	/**
	 * 更新数据源
	 * 
	 * @param dataSourceVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(DataSourceDTO dataSourceVO) {
		// Json对象
		Map<String, Object> dataMap = null;
		try {
			dataMap = new HashMap<String, Object>();

			dataSourceFacade.updateDataSource(dataSourceVO);

			dataMap.put("result", "success");
		} catch (Exception e) {
			if (dataMap != null) {
				dataMap.put("result", "保存失败！");
			}
		}
		return dataMap;
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
	public Page pageJson(int page, int pagesize) {
		// Json对象
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
	public Map<String, Object> delete(String ids) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			if (ids != null) {
				String[] idArrs = ids.split(",");
				Long[] idsLong = new Long[idArrs.length];
				for (int i = 0; i < idArrs.length; i++) {
					idsLong[i] = Long.parseLong(idArrs[i]);
				}
				dataSourceFacade.removeDataSources(idsLong);
			}

			dataMap.put("result", "success");
		} catch (RuntimeException e) {
			dataMap.put("result", e.getMessage());
		} catch (Exception e) {
			dataMap.put("result", "删除失败！");
			e.printStackTrace();
		}
		return dataMap;
	}

	/**
	 * 查询指定数据源
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get/{id}")
	public Map<String, Object> get(@PathVariable("id") Long id) {
		// Json对象
		Map<String, Object> dataMap = null;
		try {
			dataMap = new HashMap<String, Object>();

			dataMap.put("data", dataSourceFacade.getDataSourceDTOById(id));
		} catch (Exception e) {
			if (dataMap != null) {
				dataMap.put("error", "查询指定数据源失败！");
			}
		}
		return dataMap;
	}

	/**
	 * 检测数据源是否可用
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkDataSourceById")
	public Map<String, Object> checkDataSourceById(Long id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			boolean result = dataSourceFacade.testConnection(id);
			if (result) {
				dataMap.put("result", "该数据源可用");
			} else {
				dataMap.put("result", "该数据源不可用");
			}
		} catch (RuntimeException e) {
			dataMap.put("result", e.getMessage());
		} catch (Exception e) {
			dataMap.put("result", "检测数据源是否可用失败！");
			e.printStackTrace();
		}
		return dataMap;
	}

	/**
	 * 检测数据源是否可用
	 * 
	 * @param dataSourceVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkDataSource")
	public Map<String, Object> checkDataSource(DataSource dataSource) {
		// Json对象
		Map<String, Object> dataMap = null;
		try {
			dataMap = new HashMap<String, Object>();

			boolean result = dataSourceFacade.checkDataSourceCanConnect(dataSource);
			if (result) {
				dataMap.put("result", "该数据源可用");
			} else {
				dataMap.put("result", "该数据源不可用");
			}
		} catch (Exception e) {
			if (dataMap != null) {
				dataMap.put("error", "检测数据源是否可用失败！");
			}
		}
		return dataMap;
	}

}