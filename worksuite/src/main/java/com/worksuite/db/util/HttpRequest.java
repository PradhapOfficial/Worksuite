package com.worksuite.db.util;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest
{
    private URLConnection connection;
    private boolean doInput;
    private boolean doOutput;
    private int readTimout;
    private int connectTimout;
    private boolean allowedIserInteraction;
    private boolean useCaches;
    
    public HttpRequest(String urlpath) throws Exception {
        this.doInput = true;
        this.doOutput = true;
        this.readTimout = HttpConstants.READ_TIME_OUT_SEC;
        this.connectTimout = HttpConstants.CONNECTION_TIME_OUT_SEC;
        this.allowedIserInteraction = true;
        this.useCaches = true;
        URL url = new URL(urlpath);
        this.connection = url.openConnection();
    }
    
    public void init(String requestMethod) throws Exception {
    	try {
    		if (!((HttpConstants.GET_METHOD).equals(requestMethod))) {
                this.connection.setDoOutput(this.doOutput);
            }
            ((HttpURLConnection)this.connection).setRequestMethod(requestMethod);
            this.connection.setDoInput(this.doInput);
            this.connection.setReadTimeout(this.readTimout);
            this.connection.setConnectTimeout(this.connectTimout);
            this.connection.setAllowUserInteraction(this.allowedIserInteraction);
            this.connection.setUseCaches(this.useCaches);
    	} catch(Exception e) {
    		throw e;
    	}
    }
    
    public void setRequestProperty(String key, String value) throws Exception {
        this.connection.setRequestProperty(key, value);
    }
    
    private void setPayloadIntoOutputStream(byte[] bytes) throws Exception {
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(this.connection.getOutputStream());
            dataOutputStream.write(bytes);
            dataOutputStream.flush();
        }
        finally {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        }
    }
    
    public void disConnect() throws Exception{
    	((HttpURLConnection) this.connection).disconnect();
    }
    
    public HttpResponse post() throws Exception {
        try {
            this.init(HttpConstants.POST_METHOD);
            return new HttpResponse(this.connection);
        }
        catch (Exception e) {
            throw e;
        }
    }
    
    public HttpResponse post(byte[] bytes) throws Exception {
        try {
            this.init(HttpConstants.POST_METHOD);
            this.setPayloadIntoOutputStream(bytes);
            return new HttpResponse(this.connection);
        }
        catch (Exception e) {
        	throw e;
        }
    }
    
    public HttpResponse put() throws Exception {
        try {
            this.init(HttpConstants.PUT_METHOD);
            return new HttpResponse(this.connection);
        }
        catch (Exception e) {
        	throw e;
        }
    }
    
    public HttpResponse put(byte[] bytes) throws Exception{
        try {
            this.init(HttpConstants.PUT_METHOD);
            this.setPayloadIntoOutputStream(bytes);
            return new HttpResponse(this.connection);
        }
        catch (Exception e) {
        	throw e;
        }
    }
    
    public HttpResponse get() throws Exception{
        try {
            this.init(HttpConstants.GET_METHOD);
            return new HttpResponse(this.connection);
        }
        catch (Exception e) {
           throw e;
        }
    }
    
    public HttpResponse delete() throws Exception{
        try {
            this.init(HttpConstants.DELETE_METHOD);
            return new HttpResponse(this.connection);
        }
        catch (Exception e) {
        	throw e;
        }
    }
    
    public void setReadTimeout(int millSec) {
    	this.readTimout = millSec;
    }
    
    public void setConnectionTimeout(int millSec) {
    	this.connectTimout = millSec;
    }
 }