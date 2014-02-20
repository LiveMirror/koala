package org.openkoala.openci.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.AbstractEntity;

@Entity
@Table(name = "project_details")
public class ProjectDetail extends AbstractEntity {

	private static final long serialVersionUID = -6656585419704042454L;

	@Column(name = "artifact_id")
	private String artifactId;

	@Column(name = "group_id")
	private String groupId;

	@Column(name = "is_integration_cas")
	private boolean isIntegrationCas;

	@Column(name = "project_save_path")
	private String projectSavePath;

	@Enumerated(EnumType.STRING)
	@Column(name = "scm_type")
	private ScmType scmType;

	@Column(name = "scm_repository_url")
	private String scmRepositoryUrl;

	public ProjectDetail() {
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isIntegrationCas() {
		return isIntegrationCas;
	}

	public void setIntegrationCas(boolean isIntegrationCas) {
		this.isIntegrationCas = isIntegrationCas;
	}

	public String getProjectSavePath() {
		return projectSavePath;
	}

	public void setProjectSavePath(String projectSavePath) {
		this.projectSavePath = projectSavePath;
	}

	public ScmType getScmType() {
		return scmType;
	}

	public void setScmType(ScmType scmType) {
		this.scmType = scmType;
	}

	public String getScmRepositoryUrl() {
		return scmRepositoryUrl;
	}

	public void setScmRepositoryUrl(String scmRepositoryUrl) {
		this.scmRepositoryUrl = scmRepositoryUrl;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProjectDetail)) {
			return false;
		}
		ProjectDetail that = (ProjectDetail) other;
		return new EqualsBuilder().append(getArtifactId(), that.getArtifactId()).append(getGroupId(), that.getGroupId()).isEquals();

	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getArtifactId()).append(getGroupId()).hashCode();
	}

	@Override
	public String toString() {
		return getArtifactId() + getGroupId();
	}

	@Override
	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
