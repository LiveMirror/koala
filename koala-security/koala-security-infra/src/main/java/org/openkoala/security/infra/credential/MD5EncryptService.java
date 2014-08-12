package org.openkoala.security.infra.credential;

import javax.inject.Named;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.openkoala.security.core.domain.EncryptService;
/**
 * Shiro 默认是16进制加密 TODO 盐值加密
 * 
 * @author luzhao
 * 
 */
@Named("encryptService")
public class MD5EncryptService implements EncryptService {

	private static final int hashIterations = 1;

	@Override
	public String encryptPassword(String password, String salt) throws IllegalArgumentException {
		return new Md5Hash(password, salt, hashIterations).toHex();
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
