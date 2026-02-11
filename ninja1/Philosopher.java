package ninja1;

import shared.Stats;

/**
 * Philosopher - Version 1 (naive locking).
 *
 * @author CS4225
 * @version Spring 2026
 */
public class Philosopher implements Runnable {

	private final Object leftChopstick;
	private final Object rightChopstick;
	private final Stats stats;

	public Philosopher(Object left, Object right, Stats stats) {
		this.leftChopstick = left;
		this.rightChopstick = right;
		this.stats = stats;
	}

	private void pauseRandom(int maxMs) throws InterruptedException {
		Thread.sleep((int) (Math.random() * maxMs));
	}

	@Override
	public void run() {
		stats.bindToCurrentThread();

		try {
			while (true) {
				pauseRandom(100);

				long waitStart = System.nanoTime();
				synchronized (leftChopstick) {
					synchronized (rightChopstick) {
						stats.blockedNanos.addAndGet(System.nanoTime() - waitStart);

						long start = System.nanoTime();
						pauseRandom(100);
						long end = System.nanoTime();

						stats.eatCount.incrementAndGet();
						stats.eatNanos.addAndGet(end - start);
					}
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
