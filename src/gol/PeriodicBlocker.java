package gol;

public interface PeriodicBlocker {

	int NO_DURATION = 0;

	void setPeriod(long duration);

	void blockRestOfPeriodAndRestart();

	static PeriodicBlocker defaultWithNoPeriod() {
		return new PeriodicBlocker() {

			private long periodLength = NO_DURATION;
			private long periodStartTime = currentTime();

			@Override
			public void setPeriod(long periodLength) {
				this.periodLength = periodLength;
			}

			@Override
			public void blockRestOfPeriodAndRestart() {
				long timeToDelay = periodLength + periodStartTime
						- currentTime();

				if (timeToDelay >= 0)
					try {
						Thread.sleep(timeToDelay);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}

				periodStartTime = currentTime();
			}

			private long currentTime() {
				return System.currentTimeMillis();
			}
		};
	}

	static PeriodicBlocker none() {
		return new PeriodicBlocker() {
			@Override
			public void setPeriod(long periodLength) {
			}
			@Override
			public void blockRestOfPeriodAndRestart() {
			}
		};
	}
}
