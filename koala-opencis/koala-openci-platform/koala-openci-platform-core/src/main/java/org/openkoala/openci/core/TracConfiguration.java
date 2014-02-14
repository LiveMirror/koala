package org.openkoala.openci.core;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("trac")
public class TracConfiguration extends ToolConfiguration {

	private static final long serialVersionUID = 6657318907975649270L;

	@Column(name = "save_path")
	private String savePath;

	public TracConfiguration(String name, String serviceUrl, String username, String password, String savePath) {
		super(name, serviceUrl, username, password);
		this.savePath = savePath;
	}

	public TracConfiguration() {
		super();
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

}
