package org.openkoala.businesslog.utils;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.openkoala.businesslog.KoalaBusinessLogConfigException;


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
    	File configDir = new File(getClass().getResource(ConfigConstant.GROOVY_CONFIG_DIR).getFile());

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
        	return new GroovyClassLoader(getClass().getClassLoader()).parseClass(configFile);
        } catch (IOException e) {
            throw new KoalaBusinessLogConfigException("There's a failure when read BusinesslogConfig.groovy", e);
        }
    }

    private File getStandaloneConfigFile() {
        if (getClass().getResource(ConfigConstant.STANDALONE_GROOVY_CONFIG_NAME) == null) return null;

        return new File(getClass().getResource(ConfigConstant.STANDALONE_GROOVY_CONFIG_NAME).getFile());
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

}
