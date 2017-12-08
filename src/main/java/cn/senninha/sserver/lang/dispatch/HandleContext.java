package cn.senninha.sserver.lang.dispatch;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.MapHelper;

import cn.senninha.sserver.lang.message.BaseMessage;

public class HandleContext {
	private Logger logger = LoggerFactory.getLogger(HandleContext.class);
	private static HandleContext context;
	private Processor[] processor;
	
	private HandleContext() {
		init();
	}
	
	/**
	 * 初始化场景线程
	 */
	private void init() {
		processor = new Processor[1];
		processor[0] = new Processor("场景线程");
		processor[0].start();
		
		//移动检测
		processor[0].addCommand(new Task(100, true, -1, TimeUnit.MILLISECONDS, new Runnable() {
			
			@Override
			public void run() {
				MapHelper.move();
			}
		}));
		
		//开火检测
		processor[0].addCommand(new Task(50, true, -1, TimeUnit.MILLISECONDS, new Runnable() {
			
			@Override
			public void run() {
				MapHelper.pushFireMessage();
			}
		}));
		
		logger.error("初始化场景线程完成");
		
	}
	
	public void dispatch(int sessionId, BaseMessage message) {
		processor[0].addCommand(new Task(0, false, 0, TimeUnit.MILLISECONDS, new Runnable() {
			public void run() {
				HandlerFactory.getInstance().dispatch(message, sessionId);
			}
		}));

	}
	
	public static HandleContext getInstance() {
		if(context == null) {
			synchronized (HandleContext.class) {
				if(context == null) {
					context = new HandleContext();
				}
			}
		}
		return context;
	}
	public static void main(String[] args) {
		Processor p = new Processor("senninha");
		p.addCommand(new Task(1000, true, 10, TimeUnit.MILLISECONDS, new Runnable() {

			@Override
			public void run() {
				System.out.println(new Date().toString());
			}
		}));
		p.start();
	}
	
	public void addCommand(int line, Task task) {
		this.processor[line].addCommand(task);
	}
}

class Processor extends Thread {
	private DelayQueue<Task> queue;

	Processor(String name) {
		super(name);
		queue = new DelayQueue<>();
	}

	public void addCommand(Task task) {
		queue.add(task);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Task task = queue.take();
				Runnable r = task.getRunnable();
				if (r != null) {
					try{
						r.run();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (task.isNeedRepeat() && task.getRepeatTime() != 1) {
						task.correctTime(); // 修正执行时间
						addCommand(task);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


