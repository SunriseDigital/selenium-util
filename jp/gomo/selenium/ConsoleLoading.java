package jp.gomo.selenium;

public class ConsoleLoading implements Runnable {

	private volatile boolean done = false;
	
	
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
