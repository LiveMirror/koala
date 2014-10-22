package org.openkoala.security.org.facade.impl;

import org.junit.Test;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.CreateUserCommand;

import javax.inject.Inject;

public class UserTest extends AbstractOrgFacadeIntegrationTestCase {

    @Inject
    private SecurityConfigFacade securityConfigFacade;

    @Test
    public void testSave(){
        User user = new User("ceshi","测试");
        user.save();
    }

    @Test
    public void testSave1(){
        CreateUserCommand command = new CreateUserCommand();
        command.setName("测试。。。");
        command.setUserAccount("test.....");
        securityConfigFacade.createUser(command);
    }
}
