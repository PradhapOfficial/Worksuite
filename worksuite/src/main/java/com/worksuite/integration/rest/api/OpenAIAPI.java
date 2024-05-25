package com.worksuite.integration.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.external.rest.api.OpenAI;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.bean.Song;
import com.worksuite.integration.util.OpenAIUtil;
import com.worksuite.rest.api.common.APIUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

@Path("{orgId}/openai/{userId}")
public class OpenAIAPI extends APIUtil {
	
	private static Logger LOGGER = LogManager.getLogger(OpenAIAPI.class);
	
//	@POST
//	@Path("{appId}/{integrationId}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String getChat(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, @PathParam("integrationId") long integrationId, String jsonStr) throws RestException {
//		try {
//			
//			isScopeRegistered(orgId, appId);
//			
//			LOGGER.log(Level.INFO, "scopePojo data :: " +  getScopePojo().toString());
//			
//			isValidIntegId(integrationId);
//		
//			IntegrationMasterPOJO integrationMasterPOJO = getIntegrationMasterPOJO();
//			LOGGER.log(Level.INFO, "integrationMasterPOJO data :: " + integrationMasterPOJO.toString());
//			
//			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
//			
//			jsonObj = OpenAIUtil.setChatRequiredFileds(integrationMasterPOJO, jsonObj);
//			OpenAI openAI = new OpenAI();
//			
//			return openAI.getChat(integrationMasterPOJO.getAuthDetails().getToken(), jsonObj).toString();
//		}catch(RestException re) {
//			throw re;
//		}catch(Exception e) {
//			LOGGER.log(Level.ERROR, "Exception occured while getChat :: ", e);
//			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	@POST
	@Path("{appId}/{integrationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getChat(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, @PathParam("integrationId") long integrationId, String jsonStr) throws RestException {
		try {
			
			//Connection Configuration
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			configuration.addAnnotatedClass(Song.class);
			
			//Create Session Factory
			SessionFactory sessionFactory = configuration.buildSessionFactory();
			
			//Create Session Object
			Session session = sessionFactory.openSession();
			
			Song song = new Song()
					.setId(4l)
					.setSongName("Uthodoram")
					.setArtist("pradhap");
			
			session.beginTransaction();
			session.save(song);
			session.getTransaction().commit();
			LOGGER.log(Level.INFO, "Session COmpleted ");
			return null;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getChat :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
