package org.openkoala.security.core.domain;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by luzhao on 14-9-11.
 */
@Ignore
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = false)
public class BigDataTest extends AbstractDomainIntegrationTestCase {

    @Test
    public void testSaveRoleBigData10000() {
        for (int i = 1; i < 500; i++) {
            Role role = new Role("testRole" + i);
            role.setDescription("测试角色");
            role.save();
        }
    }

    @Test
    public void testSavePermissionBigData5000() {
        for (int i = 1; i < 500; i++) {
            Permission result = new Permission("测试权限" + i, "testPermission" + i);
            result.setDescription("测试权限");
            result.save();
        }
    }

    @Test
    public void testSavePermissionBigData5000_10000() {
        for (int i = 5000; i < 10000; i++) {
            Permission result = new Permission("测试权限" + i, "testPermission" + i);
            result.setDescription("测试权限");
            result.save();
        }
    }

    @Test
    public void testSaveUrlBigData5000() {
        for (int i = 1; i < 5000; i++) {
            UrlAccessResource result = new UrlAccessResource("测试URL" + i, "/auth/test/" + i);
            result.save();
        }
    }

    @Test
    public void testSaveUrlBigData5000_10000() {
        for (int i = 5000; i < 10000; i++) {
            UrlAccessResource result = new UrlAccessResource("测试URL" + i, "/auth/test/" + i);
            result.save();
        }
    }

    @Test
    public void testSaveMenuBigData10000() {
        for (int k = 5; k < 10; k++) {
            MenuResource result = new MenuResource("测试菜单第1级--"+k);
            result.setMenuIcon("glyphicon  glyphicon-list-alt");
            result.setUrl("/pages/auth/test1--"+k+".jsp");
            result.save();
            for (int i = 1; i < 10; i++) {
                MenuResource result2 = new MenuResource("测试菜单第2级--"+k+"--"+i);
                result2.setMenuIcon("glyphicon  glyphicon-list-alt");
                result2.setUrl("/pages/auth/test2--" +k +"--"+ i + ".jsp");
                result.addChild(result2);
                for (int j = 1; j < 100; j++) {
                    MenuResource result3 = new MenuResource("测试菜单第3--"+k+"--"+i+"级"+j);
                    result3.setMenuIcon("glyphicon  glyphicon-list-alt");
                    result3.setUrl("/pages/auth/test3-" +k+"--"+ i+"--"+j+"+.jsp");
                    result2.addChild(result3);
                }
            }
        }
    }

    @Test
    public void testSavePageElementBigData10000() {
        for (int i = 1; i < 500; i++) {
            PageElementResource result = new PageElementResource("测试页面元素" + i, "testPageElement" + i);
            result.save();
        }
    }

}
