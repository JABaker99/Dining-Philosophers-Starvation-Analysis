package ninja2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

import shared.Stats;

public class Philosopher implements Runnable {

	private final ReentrantLock leftChopstick;
	private final ReentrantLock rightChopstick;
	private final Stats stats;

	public Philosopher(ReentrantLock left, ReentrantLock right, Stats stats) {
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
				if (!leftChopstick.tryLock(50, TimeUnit.MILLISECONDS)) continue;
				try {
					if (!rightChopstick.tryLock(50, TimeUnit.MILLISECONDS)) continue;
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
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
