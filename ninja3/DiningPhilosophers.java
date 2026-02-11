package ninja3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import shared.ExperimentRunner;
import shared.Stats;

public class DiningPhilosophers {

	public static void main(String[] args) throws Exception {
		String[] names = {"Raphael","Donatello","Splinter","Michelangelo","Leonardo"};

		ReentrantLock[] forks = new ReentrantLock[names.length];
		for (int i = 0; i < forks.length; i++) forks[i] = new ReentrantLock(true);

		Semaphore arbitrator = new Semaphore(names.length - 1, true);

		Stats[] stats = ExperimentRunner.createStats(names.length);
		Thread[] pool = new Thread[names.length];

		for (int i = 0; i < names.length; i++) {
			int right = (i + 1) % names.length;
			pool[i] = new Thread(
					new Philosopher(arbitrator, forks[i], forks[right], stats[i]),
					names[i]
			);
			pool[i].start();
		}

		ExperimentRunner.runForSeconds("ninja3_stats.csv", pool, stats, 300);

		for (Thread t : pool) t.interrupt();
		for (Thread t : pool) t.join();
	}
}
