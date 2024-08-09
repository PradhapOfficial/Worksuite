package com.worksuite.db.util;

import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationUtils {
	
	private static Logger LOGGER = LogManager.getLogger(ApplicationUtils.class);
	
	private static ResourceBundle resourceBundleProperties;
	
	static {
		if(resourceBundleProperties == null) {
			resourceBundleProperties = ResourceBundle.getBundle("app");
		}
	}
	
	public static String getProperty(String propertyName) {
		try {
			String systemPropertyValue = System.getProperty(propertyName);
			if(systemPropertyValue != null) {
				return systemPropertyValue;
			}
			
			return resourceBundleProperties.getString(propertyName);
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getProperty :: ", e);
		}
		return null;
	}
	
	public static boolean isDevelopment() {
		try {
			String systemPropertyValue = System.getProperty("IS_DEVELOPMENT");
			if(systemPropertyValue != null) {
				return Boolean.parseBoolean(systemPropertyValue);
			}
			
			return Boolean.parseBoolean(resourceBundleProperties.getString("IS_DEVELOPMENT"));
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getProperty :: ", e);
		}
		return false;
	}
}
