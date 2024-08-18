package com.worksuite.db.util;

import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.rest.api.common.ConfigConstants;

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
	
	public static Map<Long, Long> getPrivilagedUsers() {
		String  privilagedUserStr = getProperty(ConfigConstants.IS_PRIVILAGED_USER);
		String[] privilagedUsers = privilagedUserStr.split(",");
		
		return Arrays.stream(privilagedUsers).collect(Collectors.toMap(s -> Long.parseLong(s.split(":")[0]), s -> Long.parseLong(s.split(":")[0])));
	}
}
