package org.openkoala.security.org.facade.impl.systeminit;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.openkoala.koalacommons.resourceloader.Resource;
import org.openkoala.koalacommons.resourceloader.impl.classpath.ClassPathResource;

public class SystemInitFactory {

	public static final SystemInitFactory INSTANCE = new SystemInitFactory();

	public SystemInit getSystemInit() {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance("org.openkoala.security.org.facade.impl.systeminit");
			Unmarshaller u = jc.createUnmarshaller();
			return (SystemInit) u.unmarshal(getSystemInitXml());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private InputStream getSystemInitXml() throws IOException {
		Resource resource = new ClassPathResource("/META-INF/securityOrgSystemInit/systemInit.xml", SystemInitFactory.class);
		return resource.getInputStream();
	}
}
