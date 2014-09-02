
package org.openkoala.bpm.application;

import java.util.List;

import org.dayatang.querychannel.Page;
import org.openkoala.bpm.application.vo.*;

public interface KoalaJbpmVariableApplication {

	public KoalaJbpmVariableVO getKoalaJbpmVariable(Long id);
	
	public KoalaJbpmVariableVO saveKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariable);
	
	public void updateKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariable);
	
	public void removeKoalaJbpmVariable(Long id);
	
	public void removeKoalaJbpmVariables(Long[] ids);
	
	public List<KoalaJbpmVariableVO> findAllKoalaJbpmVariable();
	
	public Page<KoalaJbpmVariableVO> pageQueryKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariable, int currentPage, int pageSize);
	

}

