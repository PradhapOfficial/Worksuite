package com.worksuite.integration.util;

public class IntegrationConstants {

	public final static String VERSION = "VERSION";
	
	public enum OpenAI {
		
		APP_ID(98765l),
		
		VERSION("VERSION");
		
		Object value;
		
		private OpenAI(Object obj){
			this.value = obj;
		}
		
		public Object getValue() {
			return this.value;
		}
	}
	
	public static boolean isValidAppId(long appId) {
		Apps[] apps = Apps.values();
		for(Apps app : apps) {
			if(app.getValue() == appId) {
				return true;
			}
		}
		return false;
	}
	
	public enum Apps {
		
		OPENAI_APP_ID(7845457649l),
		
		GEMINI_AI_APP_ID(7845457650l);
		
		private long value;
		
		private Apps(long value){
			this.value = value;
		}
		
		public long getValue() {
			return this.value;
		}
	}
	
	public enum Roles {
		
		SUPER_ADMIN(1),
		
		ADMIN(2),
		
		MEMBER(3);
		
		int value;
		
		private Roles(int value){
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
}
