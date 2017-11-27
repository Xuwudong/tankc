package cn.senninha.sserver.lang.dispatch;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HandleContext {
	
}

class Processor implements Runnable{
	private DelayQueue<Task> queue;
	
	Processor(){
		queue = new DelayQueue<>();
	}
	
	private void addCommand() {
		
	}

	@Override
	public void run() {
		
	}
}

class Task implements Delayed, Runnable{

	@Override
	public int compareTo(Delayed o) {
		return 0;
	}

	@Override
	public void run() {
		
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return 0;
	}
	
}
