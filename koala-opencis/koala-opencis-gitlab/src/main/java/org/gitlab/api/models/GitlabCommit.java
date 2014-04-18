package org.gitlab.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class GitlabCommit {

    public final static String URL = "/commits";

    private String _id;
    private String _title;

    @JsonProperty("short_id")
    private String _shortId;

    @JsonProperty("author_name")
    private String _authorName;

    @JsonProperty("author_email")
    private String _authorEmail;

    @JsonProperty("created_at")
    private Date _createdAt;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getShortId() {
        return _shortId;
    }

    public void setShortId(String shortId) {
        _shortId = shortId;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getAuthorName() {
        return _authorName;
    }

    public void setAuthorName(String authorName) {
        _authorName = authorName;
    }

    public String getAuthorEmail() {
        return _authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        _authorEmail = authorEmail;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        _createdAt = createdAt;
    }
}
