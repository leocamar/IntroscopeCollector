package br.com.inmetrics.edp.util.parser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import br.com.inmetrics.edp.util.queue.Queues;

public class ParserDiscovery implements Runnable {

	private Queues queues;

	public ParserDiscovery(Queues queues) {
		this.queues = queues;

	}

	@Override
	public void run() {
		ResultSet resultSet;
		while (true) {
			if (!queues.getDiscoveryList().isEmpty()) {
				ArrayList<String> keyValues = new ArrayList<>();

				resultSet = (ResultSet)  queues.getDiscoveryList().poll();
				try {
					resultSet.last();
					int count = resultSet.getRow();
					resultSet.beforeFirst();
					
					System.out.println(count);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				

				try {
					if (resultSet != null) {
						while (resultSet.next()) {
							String metricUnique = new String(resultSet
									.getString(5).replaceAll("[\\_\\-\\|\\@]",
											"."));
							if(!keyValues.contains(metricUnique))
							{
								keyValues.add(metricUnique);
							}
						}
						
						for(String value : keyValues){
							queues.getDiscoveryListOut().add(value);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
