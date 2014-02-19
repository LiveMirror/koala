package org.openkoala.opencis.gitlab;

/**
 * just for test
 * User: zjzhai
 * Date: 2/8/14
 * Time: 2:23 PM
 */
public class GUser {
    private String email;




    private String private_token;

    public String getPrivate_token() {
        return private_token;
    }

    public void setPrivate_token(String private_token) {
        this.private_token = private_token;
    }

    @Override
    public String toString() {
        return "GUser{" +
                "private_token='" + private_token + '\'' +
                '}';
    }
}
