import hudson.security.ProjectMatrixAuthorizationStrategy

import jenkins.model.Jenkins

ProjectMatrixAuthorizationStrategy authorizations = Jenkins.getInstance().getAuthorizationStrategy();
authorizations.add(Jenkins.READ, "{0}");
Jenkins.getInstance().setAuthorizationStrategy(authorizations);