package edu.mayo.cts2.framework.plugin.service.bprdf.dao;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class ApiKeyProvider implements InitializingBean {

	private Log log = LogFactory.getLog(this.getClass());
	
	private static final String API_KEY_PROP = "apiKey";

	private String apiKey;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.apiKey == null) {
			this.setUpApiKey();
		}
	}

	/**
	 * Sets the up api key.
	 */
	protected void setUpApiKey() {
		// check for environment variable
		String apiKeyEnvVar = System.getProperty(API_KEY_PROP);
		if (StringUtils.isNotBlank(apiKeyEnvVar)) {
			log.info("Using APIKEY from System Property.");
			this.apiKey = apiKeyEnvVar;

			return;
		}

		if (apiKey != null) {
			log.info("Using APIKEY from Configuration File.");
			
			return;
		}

		log.warn("No Bioportal API Key Set! Please sent one via the System Variable: "
				+ API_KEY_PROP + " or in the Bioportal config file.");
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
}
