package shared;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Per-thread statistics container.
 *
 * @author Jacob Baker
 * @version Spring 2026
 */
public class Stats {

	public final AtomicLong eatCount = new AtomicLong(0);
	public final AtomicLong eatNanos = new AtomicLong(0);
	public final AtomicLong blockedNanos = new AtomicLong(0);

	public void bindToCurrentThread() {
		Thread.currentThread().setName(Thread.currentThread().getName());
	}
}
