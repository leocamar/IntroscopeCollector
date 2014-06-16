package br.com.inmetrics.edp.util.parser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import br.com.inmetrics.edp.util.properties.ResourceUtils;
import br.com.inmetrics.edp.util.properties.ResourceUtils.Constants;
import br.com.inmetrics.edp.util.queue.Queues;

public class ParserMetricName implements Runnable {

	private LinkedBlockingQueue<ResultSet> inputResult;
	private LinkedBlockingQueue<ConcurrentHashMap<String, String>> outputResult;
	private Queues queues;
	private ConcurrentHashMap<String, String> metricsSumarized;
	private ResourceUtils resourceUtils;

	public ParserMetricName(Queues queues, ResourceUtils resourceUtils) {
		this.queues = queues;
		this.inputResult = this.queues.getResultSets();
		this.outputResult = this.queues.getOutputQueue();
		this.resourceUtils = resourceUtils;
	}

	public void run() {
		ResultSet resultSet;
		ConcurrentHashMap<String, String> agentServerMap = new ConcurrentHashMap<>();

		for (String agentServer : resourceUtils.getProperty(
				Constants.INTROSCOPE_AGENT_NAMES).split(";")) {
			agentServerMap.put(agentServer.split(":")[0],
					agentServer.split(":")[1]);
		}

		while (true) {

			if (!this.inputResult.isEmpty()) {
				resultSet = (ResultSet) inputResult.poll();
				
				metricsSumarized = new ConcurrentHashMap<String, String>();

				try {
					while (resultSet.next()) {

						metricsSumarized.put("host",
								agentServerMap.get(resultSet.getString(4)));

						String metricUnique = new String(resultSet.getString(5)
								.replaceAll("[\\_\\-\\|\\@]", "."));
						
						if (resourceUtils.getProperty(Constants.INTROSCOPE_LIST_METRICS).equals("true")){
							System.out.println(metricUnique);
						}

						if (!metricsSumarized.containsKey(metricUnique)) {
							metricsSumarized.put(metricUnique,
									resultSet.getString(16));
						} else if (Integer.valueOf(
								(String) metricsSumarized.get(metricUnique))
								.intValue() < Integer.valueOf(
								resultSet.getString(16)).intValue()) {
							metricsSumarized.replace(metricUnique,
									resultSet.getString(16));

						}

					}
					
					if (resourceUtils.getProperty(Constants.INTROSCOPE_LIST_METRICS).equals("true")){
						System.exit(0);
					}

					this.outputResult.add(metricsSumarized);

				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
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
