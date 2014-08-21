package org.openkoala.security.core.domain;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by luzhao on 14-8-20.
 */
@Ignore
public class ResourceAssignmentTest extends  AbstractDomainIntegrationTestCase{

    @Test
    public void testFindAllUrlAccessResourceByGroupByResource(){
       List<ResourceAssignment> resourceAssignments =  ResourceAssignment.findAll();
    }
}
