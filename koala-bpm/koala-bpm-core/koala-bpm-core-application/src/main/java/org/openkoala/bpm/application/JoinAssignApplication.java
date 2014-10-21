
package org.openkoala.bpm.application;

import java.util.List;

import org.dayatang.querychannel.Page;
import org.openkoala.bpm.application.vo.*;

public interface JoinAssignApplication {

	public JoinAssignVO getJoinAssign(Long id);
	
	public JoinAssignVO saveJoinAssign(JoinAssignVO joinAssign);
	
	public void updateJoinAssign(JoinAssignVO joinAssign);
	
	public void removeJoinAssign(Long id);
	
	public void removeJoinAssigns(Long[] ids);
	
	public List<JoinAssignVO> findAllJoinAssign();
	
	public Page<JoinAssignVO> pageQueryJoinAssign(JoinAssignVO joinAssign, int currentPage, int pageSize);
	
	public JoinAssignVO getJoinAssignByName(String name);

}

