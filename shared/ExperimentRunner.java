package shared;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Shared experiment runner for all Dining Philosophers versions.
 *
 * @author Jacob Baker
 * @version Spring 2026
 */
public final class ExperimentRunner {

	private ExperimentRunner() {}

	public static Stats[] createStats(int n) {
		Stats[] stats = new Stats[n];
		for (int i = 0; i < n; i++) {
			stats[i] = new Stats();
		}
		return stats;
	}

	public static void runForSeconds(
			String csvPath,
			Thread[] threads,
			Stats[] stats,
			int durationSeconds
	) throws Exception {

		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		if (mxBean.isThreadContentionMonitoringSupported()) {
			mxBean.setThreadContentionMonitoringEnabled(true);
		}

		long[] ids = new long[threads.length];
		for (int i = 0; i < threads.length; i++) {
			ids[i] = threads[i].getId();
		}

		long[] prevBlockedMs = new long[threads.length];
		long[] prevEatNanos = new long[threads.length];

		try (PrintWriter out = new PrintWriter(new FileWriter(csvPath))) {
			out.println("second,philosopher,eatCount,eatNanosDelta,blockedMillisDelta");

			for (int sec = 0; sec < durationSeconds; sec++) {
				ThreadInfo[] infos = mxBean.getThreadInfo(ids);

				for (int i = 0; i < threads.length; i++) {
					long blockedMs =
							(infos[i] == null) ? 0L : Math.max(0L, infos[i].getBlockedTime());
					long blockedDelta = blockedMs - prevBlockedMs[i];
					prevBlockedMs[i] = blockedMs;

					long eatNanos = stats[i].eatNanos.get();
					long eatDelta = eatNanos - prevEatNanos[i];
					prevEatNanos[i] = eatNanos;

					out.printf(
							"%d,%s,%d,%d,%d%n",
							sec,
							threads[i].getName(),
							stats[i].eatCount.get(),
							eatDelta,
							blockedDelta
					);
				}

				out.flush();
				Thread.sleep(1000);
			}
		}
	}
}
