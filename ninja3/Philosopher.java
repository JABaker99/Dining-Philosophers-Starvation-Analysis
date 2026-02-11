package ninja3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

import shared.Stats;

public class Philosopher implements Runnable {

	private final Semaphore arbitrator;
	private final ReentrantLock leftChopstick;
	private final ReentrantLock rightChopstick;
	private final Stats stats;

	public Philosopher(
			Semaphore arbitrator,
			ReentrantLock left,
			ReentrantLock right,
			Stats stats
	) {
		this.arbitrator = arbitrator;
		this.leftChopstick = left;
		this.rightChopstick = right;
		this.stats = stats;
	}

	private void pauseRandom(int maxMs) throws InterruptedException {
		Thread.sleep(ThreadLocalRandom.current().nextInt(maxMs));
	}

	@Override
	public void run() {
		stats.bindToCurrentThread();

		try {
			while (!Thread.currentThread().isInterrupted()) {
				pauseRandom(100);

				long waitStart = System.nanoTime();
				arbitrator.acquire();
				try {
					leftChopstick.lock();
					try {
						rightChopstick.lock();
						try {
							stats.blockedNanos.addAndGet(System.nanoTime() - waitStart);

							long start = System.nanoTime();
							pauseRandom(100);
							long end = System.nanoTime();

							stats.eatCount.incrementAndGet();
							stats.eatNanos.addAndGet(end - start);
						} finally {
							rightChopstick.unlock();
						}
					} finally {
						leftChopstick.unlock();
					}
				} finally {
					arbitrator.release();
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
