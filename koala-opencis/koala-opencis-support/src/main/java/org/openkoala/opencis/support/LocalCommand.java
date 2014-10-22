package org.openkoala.opencis.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openkoala.opencis.api.Project;

/**
 * 本地抽象
 * 
 * @author zjh
 * 
 */
public abstract class LocalCommand extends AbstractCommand {

	protected Project project;
	protected String userName;
	protected String password;
	protected String svnAddress; // svn服务器地址,http://ip/xxxx/xxx

	public LocalCommand() {
		// TODO Auto-generated constructor stub
	}

	public LocalCommand(SvnConfig config, Project project) {
		this.project = project;
		this.userName = config.getSvnUser();
		this.password = config.getSvnPassword();
		this.svnAddress = config.getSvnAddress();
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		String strCmd = getCommand();
		System.out.println("执行本地命令：" + strCmd);
		if (CommonUtil.isLinux()) {
			process = runtime.exec(new String[] { "/bin/sh", "-c", strCmd });
		} else {
			process = runtime.exec(strCmd);
		}

		String result = readOutput(process.getInputStream());
		System.out.println(result);
	}

	public abstract String getCommand();
}
