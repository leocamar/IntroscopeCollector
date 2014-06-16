package br.com.inmetrics.edp;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import br.com.inmetrics.edp.core.Executor;
import br.com.inmetrics.edp.util.parser.ParserMetricName;
import br.com.inmetrics.edp.util.properties.ResourceUtils.Constants;
import br.com.inmetrics.edp.util.queue.Queues;
import br.com.inmetrics.edp.zabbix.sender.Sender;

public class EntryPoint {

	public static IntroscopeCollector introscopeCollector;

	public static void main(String[] args) {

		String propertiesFile = args[0];
		Queues queues = new Queues();
		ParserMetricName parser;
		Sender sender;
		TimerTask executor;
		Timer timer;
		final Thread parserThread;
		final Thread senderThread;

		introscopeCollector = new IntroscopeCollector();
		introscopeCollector.initializeAgent(propertiesFile);

		parser = new ParserMetricName(queues,
				introscopeCollector.getResourceUtils());

		executor = new Executor(introscopeCollector.getResourceUtils(), queues);
		timer = new Timer();

		timer.schedule(executor, 5000,
				Integer.valueOf(introscopeCollector.getResourceUtils()
						.getProperty(Constants.COLLECT_INTERVAL)) * 1000);

		sender = new Sender(queues, introscopeCollector.getResourceUtils());

		parserThread = new Thread(parser);
		parserThread.start();

		senderThread = new Thread(sender);
		senderThread.start();
		

	}

}
