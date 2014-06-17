package org.openkoala.businesslog.utils;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.openkoala.businesslog.KoalaBusinessLogConfigException;
import org.openkoala.koalacommons.resourceloader.impl.classpath.ClassPathResource;


public class GroovyObjectClassCache {
	
    private static Class standaloneGroovyObjectClass;
    private static Set<Class> groovyObjectClasses = new HashSet<Class>();
    
    public static Class getStandaloneGroovyObjectClass() {
		return standaloneGroovyObjectClass;
	}

	public static Set<Class> getGroovyObjectClasses() {
		return groovyObjectClasses;
	}

	GroovyObjectClassCache() {
    	if (isStandaloneConfig()) {
    		initStandaloneGroovyObjectClass();
    	} else {
    		initGroovyObjectClasses();
    	}
    }

    private void initStandaloneGroovyObjectClass() {
    	if (standaloneGroovyObjectClass != null) return;
    	standaloneGroovyObjectClass = getGroovyClass(getStandaloneConfigFile());
    }
    
    private void initGroovyObjectClasses() {
    	if (!groovyObjectClasses.isEmpty()) return;
    		
    	groovyObjectClasses.clear();
    	File configDir = getFileByPath(ConfigConstant.GROOVY_CONFIG_DIR);

        if (!configDir.exists() || !configDir.isDirectory())
            throw new KoalaBusinessLogConfigException("Not found any businesslog config, you need a " + ConfigConstant.STANDALONE_GROOVY_CONFIG_NAME + " or businessLogConfig director");

        for (File each : configDir.listFiles(new GroovyFileNameFilter())) {
        	groovyObjectClasses.add(getGroovyClass(each));
        }
    }

    void refreshStandaloneGroovyObjectClass() {
    	standaloneGroovyObjectClass = null;
    	initStandaloneGroovyObjectClass();
    }
    
    void refreshGroovyObjectClasses() {
    	groovyObjectClasses.clear();
    	initGroovyObjectClasses();
    }
    
    private Class getGroovyClass(File configFile) {
        try {
        	CompilerConfiguration config = CompilerConfiguration.DEFAULT; 
        	config.setSourceEncoding("UTF-8");
        	GroovyClassLoader groovyClassLoader = new GroovyClassLoader(getClass().getClassLoader(), config);
        	return groovyClassLoader.parseClass(configFile);
        } catch (IOException e) {
            throw new KoalaBusinessLogConfigException("There's a failure when read BusinesslogConfig.groovy", e);
        }
    }

    private File getStandaloneConfigFile() {
    	File file = getFileByPath(ConfigConstant.STANDALONE_GROOVY_CONFIG_NAME);
        if (file == null) {
        	return null;
        }
        return file;
    }

    boolean isStandaloneConfig() {
        return getStandaloneConfigFile() != null && getStandaloneConfigFile().exists();
    }

    private class GroovyFileNameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".groovy");
        }
    }
    
    private File getFileByPath(String path) {
    	try {
			return new ClassPathResource(path).getFile();
		} catch (IOException e) {
			return null;
		}
    }

}
