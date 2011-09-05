package net.awired.impl;

import junit.framework.TestCase;
import net.awired.Bean;

public class BeanImplTest extends TestCase {

    public void testBeanIsABean() {
	Bean aBean = new BeanImpl();
        assertTrue(aBean.isABean());
    }

}