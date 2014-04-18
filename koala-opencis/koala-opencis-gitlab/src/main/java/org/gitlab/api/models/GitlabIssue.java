package org.gitlab.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class GitlabIssue {
	public enum Action {
		LEAVE, CLOSE, REOPEN
	}
	
	public static final String StateClosed = "closed";
	public static final String StateOpened = "opened";

	public static final String URL = "/issues";
	
	private int _id;
	private int _iid;
	
	@JsonProperty("project_id")
	private int _projectId;
	
	private String _title;
	private String _description;
	private String[] _labels;
	private GitlabMilestone _milestone;
	
	private GitlabUser _assignee;
	private GitlabUser _author;
	
	private String _state;
	
	@JsonProperty("updated_at")
	private Date _updatedAt;
	
	@JsonProperty("created_at")
	private Date _createdAt;

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public int getIid() {
		return _iid;
	}

	public void setIid(int iid) {
		_iid = iid;
	}

	public int getProjectId() {
		return _projectId;
	}

	public void setProjectId(int projectId) {
		_projectId = projectId;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String[] getLabels() {
		return _labels;
	}

	public void setLabels(String[] labels) {
		_labels = labels;
	}

	public GitlabMilestone getMilestone() {
		return _milestone;
	}

	public void setMilestone(GitlabMilestone milestone) {
		_milestone = milestone;
	}

	public GitlabUser getAssignee() {
		return _assignee;
	}

	public void setAssignee(GitlabUser assignee) {
		_assignee = assignee;
	}

	public GitlabUser getAuthor() {
		return _author;
	}

	public void setAuthor(GitlabUser author) {
		_author = author;
	}

	public String getState() {
		return _state;
	}

	public void setState(String state) {
		_state = state;
	}

	public Date getUpdatedAt() {
		return _updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		_updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return _createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		_createdAt = createdAt;
	}

}
