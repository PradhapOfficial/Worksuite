package com.worksuite.db.util;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;

public class HttpResponse
{
    private URLConnection connection;
    private int responseCode = -1;
    private BufferedReader successBufferedReader = null;
    private BufferedReader errorBufferedReader = null;
    public HttpResponse( URLConnection connection) {
        this.connection = connection;
    }
    
    public String getResponseMessageAsString() throws Exception {
        try {
        	if(successBufferedReader == null) {
        		successBufferedReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            }
            StringBuilder responseContent = new StringBuilder();
            String content;
            while ((content = successBufferedReader.readLine()) != null) {
                responseContent.append(String.valueOf(content) + "\n");
            }
            return responseContent.toString();
        }
        finally {
        	successBufferedReader.close();
        }
    }
    
    public String getErrorResponseMessageAsString() throws Exception {
        try {
        	if(errorBufferedReader == null) {
        		errorBufferedReader = new BufferedReader(new InputStreamReader(((HttpURLConnection)this.connection).getErrorStream()));
            }
            StringBuilder responseContent = new StringBuilder();
            String content = "";
            while ((content = errorBufferedReader.readLine()) != null) {
                responseContent.append(String.valueOf(content) + "\n");
            }
            return responseContent.toString();
        }
        finally {
        	errorBufferedReader.close();
        }
    }
    
    public int getResponseCode() throws Exception {
    	if(responseCode == -1) {
    		responseCode = ((HttpURLConnection)this.connection).getResponseCode();
    	}
        return responseCode;
    }
}

