package org.openkoala.opencis.svn.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.SSHCommand;
import org.openkoala.opencis.support.SSHConnectConfig;
import org.openkoala.opencis.support.SvnConfig;

/**
 * 命令模式之余，此抽象类又是子类的模板，子类只需要实现对应的抽象方法即可
 */
public abstract class SvnCommand extends SSHCommand {

	protected String svnAddress;					//svn服务器地址,http://ip/xxxx/xxx
	protected String svnUser;						//SVN用户
	protected String svnPassword;					//SVN密码
	
    public SvnCommand() {
        super();
    }

    public SvnCommand(SvnConfig config, Project project) {
        super(config, project);
        this.svnAddress = config.getSvnAddress();
        this.storePath = CommonUtil.validatePath(config.getStorePath());
    }
}
