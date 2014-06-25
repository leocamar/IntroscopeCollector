package br.com.inmetrics.edp.util.parser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import br.com.inmetrics.edp.util.properties.ResourceUtils;
import br.com.inmetrics.edp.util.properties.ResourceUtils.Constants;
import br.com.inmetrics.edp.util.queue.Queues;

public class ParserDiscovery implements Runnable {

	private Queues queues;
	private ResourceUtils resourceUtils;

	public ParserDiscovery(Queues queues, ResourceUtils resourceUtils) {
		this.queues = queues;
		this.resourceUtils = resourceUtils;
	}

	@Override
	public void run() {
		ResultSet resultSet;
		ConcurrentHashMap<String, String> agentServerMap = new ConcurrentHashMap<>();

		for (String agentServer : resourceUtils.getProperty(
				Constants.INTROSCOPE_AGENT_NAMES).split(";")) {
			agentServerMap.put(agentServer.split(":")[0],
					agentServer.split(":")[1]);
		}

		while (true) {
			if (!queues.getDiscoveryList().isEmpty()) {
				ArrayList<String> keyValues = new ArrayList<>();

				resultSet = (ResultSet) queues.getDiscoveryList().poll();

				try {
					if (resultSet != null) {

						while (resultSet.next()) {
							if (keyValues.size() == 0)
								keyValues.add(agentServerMap.get(resultSet
										.getString(4)));
							String metricUnique = new String(resultSet
									.getString(5).replaceAll("[\\_\\-\\|\\@]",
											"."));
							if (!keyValues.contains(metricUnique)) {
								keyValues.add(metricUnique);
							}
						}

						for (String value : keyValues) {
							queues.getDiscoveryListOut().add(value);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
