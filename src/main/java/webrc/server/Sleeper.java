package webrc.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Allow any thread to sleep for a given period of time,
 * and be woken up by topic
 * @author benjaminmorgan
 * 
 */
public class Sleeper {

	private static Map<Object, Set<Thread>> sleepingThreads = new HashMap<Object, Set<Thread>>();

	/**
	 * causes the current thread to sleep until it it interrupted, 
	 * until someone calls interruptSleepingThreads with the same topic
	 * or until the timeout is up.
	 * 
	 * @param topic
	 * @param timeout
	 */
	public static void sleep(Object topic, long timeout) {
		synchronized (sleepingThreads) {
			Set<Thread> keyedSleepers = null;
			if (sleepingThreads.containsKey(topic))
				keyedSleepers = sleepingThreads.get(topic);
			else {
				keyedSleepers = new HashSet<Thread>();
				sleepingThreads.put(topic, keyedSleepers);
			}
			keyedSleepers.add(Thread.currentThread());
		}
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			// ok
		} finally {
			synchronized (sleepingThreads) {
				Set<Thread> keyedSleepers = sleepingThreads.get(topic);
				if(keyedSleepers != null)
				{
					keyedSleepers.remove(Thread.currentThread());
					if(keyedSleepers.isEmpty())
						sleepingThreads.remove(topic);
				}
			}
		}
	}

	/**
	 * interrupts all threads sleeping under this topic
	 * 
	 * @param topic
	 */
	public static void interruptSleepingThreads(Object topic) {
		synchronized (sleepingThreads) {
			Set<Thread> keyedSleepers = sleepingThreads.get(topic);
			if (keyedSleepers != null)
				for (Thread t : keyedSleepers) {
					t.interrupt();
				}
		}
	}

}
