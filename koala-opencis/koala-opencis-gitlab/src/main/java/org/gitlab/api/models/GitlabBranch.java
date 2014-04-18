package org.gitlab.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class GitlabBranch {
	public final static String URL = "/repository/branches/";
	
	@JsonProperty("name")
    private String _name;

//    @JsonProperty("commit")
//    private GitlabCommit _commit;
	
	@JsonProperty("protected")
	private boolean _protected;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public boolean isProtected() {
		return _protected;
	}

	public void setProtected(boolean isProtected) {
		this._protected = isProtected;
	}
}
