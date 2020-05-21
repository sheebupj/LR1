package com.hpe.lr1;

import org.apache.log4j.Logger;

import com.hpe.utilities.utility;

public class GetQueueSize {
	final static Logger logger = Logger.getLogger(GetQueueSize.class);
	ProcessMessageQueue messageQueue;

	public GetQueueSize() {
		super();
		// TODO Auto-generated constructor stub
		messageQueue= new ProcessMessageQueue();
	}
	public static void main(String[] args) {
		GetQueueSize getQueueSize=new GetQueueSize();
		logger.info("@dxc-info:"+"Queue size:"+getQueueSize.messageQueue.messageQueueSize());
	}
	
	

}
