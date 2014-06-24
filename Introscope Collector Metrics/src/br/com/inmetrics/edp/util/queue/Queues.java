package br.com.inmetrics.edp.util.queue;

import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Queues {

	private LinkedBlockingQueue<ResultSet> resultSets;
	private LinkedBlockingQueue<ConcurrentHashMap<String, String>> outputQueue;
	private LinkedBlockingQueue<ResultSet> discoveryListIn;
	private LinkedBlockingQueue<String> discoveryListOut;

	public Queues() {
		this.resultSets = new LinkedBlockingQueue<ResultSet>();
		this.outputQueue = new LinkedBlockingQueue<ConcurrentHashMap<String, String>>();
		this.discoveryListIn = new LinkedBlockingQueue<ResultSet>();
		this.discoveryListOut = new LinkedBlockingQueue<String>();
	}

	public LinkedBlockingQueue<ResultSet> getResultSets() {
		return resultSets;
	}

	public void setResultSets(LinkedBlockingQueue<ResultSet> resultSets) {
		this.resultSets = resultSets;
	}

	public LinkedBlockingQueue<ConcurrentHashMap<String, String>> getOutputQueue() {
		return outputQueue;
	}

	public void setOutputQueue(LinkedBlockingQueue<ConcurrentHashMap<String, String>> outputQueue) {
		this.outputQueue = outputQueue;
	}

	public LinkedBlockingQueue<ResultSet> getDiscoveryList() {
		return discoveryListIn;
	}

	public void setDiscoveryList(LinkedBlockingQueue<ResultSet> discoveryList) {
		this.discoveryListIn = discoveryList;
	}

	public LinkedBlockingQueue<ResultSet> getDiscoveryListIn() {
		return discoveryListIn;
	}

	public void setDiscoveryListIn(LinkedBlockingQueue<ResultSet> discoveryListIn) {
		this.discoveryListIn = discoveryListIn;
	}

	public LinkedBlockingQueue<String> getDiscoveryListOut() {
		return discoveryListOut;
	}

	public void setDiscoveryListOut(LinkedBlockingQueue<String> discoveryListOut) {
		this.discoveryListOut = discoveryListOut;
	}
	
	
}
