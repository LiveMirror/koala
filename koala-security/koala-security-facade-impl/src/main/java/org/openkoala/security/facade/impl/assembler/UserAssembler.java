package org.openkoala.security.facade.impl.assembler;

import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.command.CreateUserCommand;
import org.openkoala.security.facade.dto.UserDTO;

/**
 * 用户装配器
 * 
 * @author luzhao
 * 
 */
public class UserAssembler {

	public static User toUser(CreateUserCommand command) {
		User result = new User(command.getName(), command.getUserAccount());
		result.setDescription(command.getDescription());
		result.setCreateOwner(command.getCreateOwner());
		return result;
	}
	
	public static UserDTO toUserDTO(User user) {
		UserDTO result = new UserDTO(user.getUserAccount(), user.getPassword(), user.getCreateDate(),user.getDescription());
		result.setId(user.getId());
		result.setName(user.getName());
		result.setEmail(user.getEmail());
		result.setTelePhone(user.getTelePhone());
		result.setLastLoginTime(user.getLastLoginTime());
		result.setVersion(user.getVersion());
		result.setCreateOwner(user.getCreateOwner());
		result.setLastModifyTime(user.getLastModifyTime());
		result.setDisabled(user.isDisabled());
		return result;
	}

}
