package org.openkoala.opencis.svn;

import org.openkoala.opencis.api.Developer;

/**
 * 默认用户ID、用户名、密码都是Koala的Developer扩展类
 * @author zjh
 *
 */
public class KoalaDeveloper extends Developer {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "Koala";
	}
	
	@Override
	public String getFullName() {
		// TODO Auto-generated method stub
		return "Koala";
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return "Koala";
	}
}
