package ninja1;

import shared.ExperimentRunner;
import shared.Stats;

public class DiningPhilosophers {

	public static void main(String[] args) throws Exception {
		String[] names = {"Raphael","Donatello","Splinter","Michelangelo","Leonardo"};
		Object[] chopsticks = new Object[names.length];
		for (int i = 0; i < chopsticks.length; i++) chopsticks[i] = new Object();

		Stats[] stats = ExperimentRunner.createStats(names.length);
		Thread[] pool = new Thread[names.length];

		for (int i = 0; i < names.length; i++) {
			int right = (i + 1) % names.length;
			pool[i] = new Thread(
					new Philosopher(chopsticks[i], chopsticks[right], stats[i]),
					names[i]
			);
			pool[i].start();
		}

		ExperimentRunner.runForSeconds("ninja1_stats.csv", pool, stats, 300);

		for (Thread t : pool) t.interrupt();
		for (Thread t : pool) t.join();
	}
}
