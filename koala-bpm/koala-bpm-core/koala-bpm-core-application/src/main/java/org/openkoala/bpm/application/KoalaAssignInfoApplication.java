
package org.openkoala.bpm.application;

import java.util.List;

import org.dayatang.querychannel.Page;
import org.openkoala.bpm.application.vo.*;

public interface KoalaAssignInfoApplication {

	public KoalaAssignInfoVO getKoalaAssignInfo(Long id);
	
	public KoalaAssignInfoVO saveKoalaAssignInfo(KoalaAssignInfoVO koalaAssignInfo);
	
	public void updateKoalaAssignInfo(KoalaAssignInfoVO koalaAssignInfo);
	
	public void removeKoalaAssignInfo(Long id);
	
	public void removeKoalaAssignInfos(Long[] ids);
	
	public List<KoalaAssignInfoVO> findAllKoalaAssignInfo();
	
	public Page<KoalaAssignInfoVO> pageQueryKoalaAssignInfo(KoalaAssignInfoVO koalaAssignInfo, int currentPage, int pageSize);
	


			
	public Page<KoalaAssignDetailVO> findJbpmNamesByKoalaAssignInfo(Long id, int currentPage, int pageSize);		
	
}

