package br.com.inmetrics.edp.util.parser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import br.com.inmetrics.edp.util.queue.Queues;

public class ParserDiscovery implements Runnable {

	private LinkedBlockingQueue<ResultSet> inputResult;

	public ParserDiscovery(Queues queues) {
		this.inputResult = queues.getDiscoveryList();
		
	}

	@Override
	public void run() {
		ResultSet resultSet;
		while (true) {
			if (!inputResult.isEmpty()) {
				ArrayList<String> keyValues = new ArrayList<>();
				
				resultSet = inputResult.poll();
				
				try {
					while (resultSet.next()){
						
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
