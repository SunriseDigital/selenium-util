package jp.gomo.selenium;

public class ConsoleLoading implements Runnable {

	private volatile boolean done = false;
	
	private static ConsoleLoading instance;
	
	private static Thread runningThread;
	
	synchronized public static void start()
	{
		if(runningThread == null || !runningThread.isAlive())
		{
			instance = new ConsoleLoading();
			runningThread = new Thread(instance);
			runningThread.start();
		}
	}
	
	public static void stop()
	{
		instance.shutdown();
	}
	
	public void shutdown()
	{
		done = true;
	}
	
	@Override
	public void run() {
		while (!done) {
			System.out.print(".");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}

}
