package sampath.opd2015;

import java.util.Random;

public class LTC implements Runnable {

	Airport airport;
	final int frequency;

	public LTC(Airport airport) {
		this.airport = airport;
		frequency = (Flight.riMax + Flight.roMax) * 1;
	}

	@Override
	public void run() {
		Random random = new Random();
		int i = 0;
		createFlight(random, i);
		while (airport.open) {
			i = createFlight(random, i);
		}

	}

	private int createFlight(Random random, int i) {
		Flight f = new Flight(airport, 10 + random.nextInt(290), 
				"A3" + (10 + random.nextInt(8)),   "OOPD"+ ++i);
		Thread ft = new Thread(f, f.flightNo);
		f.executionar = ft;
		ft.start();
		try {
			Thread.sleep(frequency * 1000);
		} catch (InterruptedException e) {
			// nothing much to do if interrupted
		}
		return i;
	}

}
