
package org.openkoala.bpm.designer.application;

import java.util.List;

import org.dayatang.querychannel.Page;
import org.openkoala.bpm.designer.application.dto.PublishURLVO;

public interface BpmDesignerApplication {

	public PublishURLVO getPublishURL(Long id) throws Exception;
	
	public PublishURLVO savePublishURL(PublishURLVO publishURL) throws Exception;
	
	public void updatePublishURL(PublishURLVO publishURL) throws Exception;
	
	public void removePublishURL(Long id) throws Exception;
	
	public void removePublishURL(Long[] ids) throws Exception;
	
	public List<PublishURLVO> findAllPublishURL() throws Exception;
	
	public Page<PublishURLVO> pageQueryPublishURL(PublishURLVO publishURL, int currentPage, int pageSize) throws Exception;
	

}

