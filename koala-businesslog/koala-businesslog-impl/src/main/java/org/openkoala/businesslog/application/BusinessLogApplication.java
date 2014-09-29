package org.openkoala.businesslog.application;

import java.util.List;

import org.dayatang.utils.Page;
import org.openkoala.businesslog.model.DefaultBusinessLog;
import org.openkoala.businesslog.model.DefaultBusinessLogDTO;

/**
 * User: zjzhai
 * Date: 12/11/13
 * Time: 3:11 PM
 */
public interface BusinessLogApplication {

    public DefaultBusinessLogDTO getDefaultBusinessLog(Long id);

    public void removeDefaultBusinessLog(Long id);

    void save(DefaultBusinessLog log);

    public void removeDefaultBusinessLogs(Long[] ids);

    public List<DefaultBusinessLogDTO> findAllDefaultBusinessLog();

    public Page<DefaultBusinessLogDTO> pageQueryDefaultBusinessLog(DefaultBusinessLogDTO defaultBusinessLog, int currentPage, int pageSize);


}
