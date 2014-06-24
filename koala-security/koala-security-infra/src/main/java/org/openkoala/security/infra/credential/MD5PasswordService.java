package org.openkoala.security.infra.credential;

import javax.inject.Named;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.openkoala.security.core.domain.PasswordService;
import org.openkoala.security.core.domain.User;

/**
 * Shiro 默认是16进制加密 TODO 盐值加密
 * 
 * @author luzhao
 * 
 */
@Named("passwordService")
public class MD5PasswordService implements PasswordService {

	private static final int hashIterations = 1;

	@Override
	public String encryptPassword(User user) throws IllegalArgumentException {
		return new Md5Hash(user.getPassword(), null, hashIterations).toHex();
	}

	@Override
	public String getCredentialsStrategy() {
		return Md5Hash.ALGORITHM_NAME;
	}

	@Override
	public int getHashIterations() {
		return hashIterations;
	}

}
