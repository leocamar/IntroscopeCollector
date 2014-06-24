package br.com.inmetrics.edp;

import java.util.Timer;
import java.util.TimerTask;

import br.com.inmetrics.edp.core.Discovery;
import br.com.inmetrics.edp.core.Executor;
import br.com.inmetrics.edp.util.parser.ParserDiscovery;
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
		ParserDiscovery parserDiscovery;
		TimerTask executor;
		TimerTask discovery;
		Timer timer = new Timer();
		;
		final Thread parserThread;
		final Thread senderThread;
		final Thread discoveryThread;

		introscopeCollector = new IntroscopeCollector();
		introscopeCollector.initializeAgent(propertiesFile);

		parser = new ParserMetricName(queues,
				introscopeCollector.getResourceUtils());

		parserDiscovery = new ParserDiscovery(queues);

		executor = new Executor(introscopeCollector.getResourceUtils(), queues);
		discovery = new Discovery(introscopeCollector.getResourceUtils(),
				queues);

		
		//TODO VOltar para o Collect_Interval
		 timer.schedule(executor, 5000,
		 Integer.valueOf(introscopeCollector.getResourceUtils()
		 .getProperty(Constants.DISCOVERY_INTERVAL)) * 1000);

		timer.schedule(discovery, 5000,
				Integer.valueOf(introscopeCollector.getResourceUtils()
						.getProperty(Constants.DISCOVERY_INTERVAL)) * 1000);

		sender = new Sender(queues, introscopeCollector.getResourceUtils());

		parserThread = new Thread(parser, "Parser");
		parserThread.start();

		senderThread = new Thread(sender, "Sender");
		senderThread.start();

		discoveryThread = new Thread(parserDiscovery, "Discovery");
		discoveryThread.start();

	}

}
