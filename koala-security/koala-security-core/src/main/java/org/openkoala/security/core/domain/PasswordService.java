package org.openkoala.security.core.domain;

/**
 * 密码加密服务
 * 
 * @author luzhao
 * 
 */
public interface PasswordService {

	String encryptPassword(User user) throws IllegalArgumentException;

	/**
	 * 获取加密策略 例如： MD5
	 * 
	 * @return
	 */
	String getCredentialsStrategy();

	/**
	 * 获取加密次数
	 * 
	 * @return
	 */
	int getHashIterations();
}
