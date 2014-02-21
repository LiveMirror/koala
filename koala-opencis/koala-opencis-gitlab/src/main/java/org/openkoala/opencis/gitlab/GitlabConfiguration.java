package org.openkoala.opencis.gitlab;

import java.text.MessageFormat;

public class GitlabConfiguration {

    private String token;

    private String gitlabHostURL;

    private String username;

    private String password;

    private String email;

    public String getGitlabUserUrl() {
        return MessageFormat.format("{0}/{1}/", gitlabHostURL, username);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGitlabHostURL() {
        return gitlabHostURL;
    }

    public void setGitlabHostURL(String gitlabHostURL) {
        this.gitlabHostURL = gitlabHostURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
