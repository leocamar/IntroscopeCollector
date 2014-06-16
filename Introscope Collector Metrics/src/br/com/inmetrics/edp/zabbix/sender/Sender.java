package br.com.inmetrics.edp.zabbix.sender;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import br.com.inmetrics.edp.util.properties.ResourceUtils;
import br.com.inmetrics.edp.util.properties.ResourceUtils.Constants;
import br.com.inmetrics.edp.util.queue.Queues;
import br.com.inmetrics.edp.zabbix.sender.ZabbixSender.ZabbixSenderItem;
import br.com.inmetrics.edp.zabbix.sender.ZabbixSender.ZabbixSenderResponse;

public class Sender implements Runnable {

	private Queues queues;

	private ResourceUtils resourceUtils;

	public Sender(Queues queues, ResourceUtils resourceUtils) {
		this.queues = queues;
		this.resourceUtils = resourceUtils;
	}

	public void run() {

		LinkedBlockingQueue<ConcurrentHashMap<String, String>> outputResult = this.queues
				.getOutputQueue();
		ZabbixSender sender = new ZabbixSender(
				resourceUtils.getProperty(Constants.ZABBIX_SERVER),
				Integer.valueOf(resourceUtils
						.getProperty(Constants.ZABBIX_PORT)));
		ZabbixSenderResponse response = null;

		while (true) {
			ConcurrentHashMap<String, String> result;
			String host = "";
			if (!outputResult.isEmpty()) {

				result = (ConcurrentHashMap<String, String>) outputResult
						.poll();
				host = result.get("host");
				Iterator<String> resuIterator = result.keySet().iterator();
				ArrayList<String> keys = new ArrayList<>();

				while (resuIterator.hasNext()) {
					String key = resuIterator.next();
					if (!key.equals("host"))
						keys.add(key);
				}

				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat(
						Constants.DATE_FORMAT);
				ArrayList<ZabbixSenderItem> senderItens = new ArrayList<>();
				ZabbixSenderItem senderItem = null;
				for (String key : keys) {
					senderItem = new ZabbixSenderItem(host, key,
							result.get(key));
					senderItens.add(senderItem);
				}
				senderItem = new ZabbixSenderItem(host,"introscope.collector.ping","1");
				senderItens.add(senderItem);

				try {
					response = sender.sendItems(senderItens);
					System.out.println(response.toString().substring(21, 29)
							.equals("Failed 1") ? format.format(date)
							+ " : Falhou: " + senderItem.toString() : format
							.format(date) + " : " + response.toString());
				} catch (IOException e) {
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
