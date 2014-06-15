package br.com.inmetrics.edp;

import br.com.inmetrics.edp.util.properties.ResourceUtils;
import br.com.inmetrics.edp.util.properties.ResourceUtils.Constants;

public class IntroscopeCollector {
	
	private ResourceUtils resourceUtils;

	public ResourceUtils getResourceUtils() {
		return resourceUtils;
	}
	
	public boolean initializeAgent(String properfiesFile){
		try{
			resourceUtils = new ResourceUtils(properfiesFile);
			System.out.println("Agents and Servers: "+resourceUtils.getProperty(Constants.INTROSCOPE_AGENT_NAMES));
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	

}
