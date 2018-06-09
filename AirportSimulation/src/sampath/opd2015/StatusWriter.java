package sampath.opd2015;

import java.util.Map;

public class StatusWriter implements Runnable {

	Map<Integer, Flight> flights;

	String input_prompt = "Enter command > ";
	boolean statusDrawn;
	boolean sleeping;
	boolean alive = true;
	boolean redrawRequired;
	Thread executer;
	
	
	public StatusWriter(Map<Integer, Flight> flights) {
		this.flights = flights;
	}

	public void run() {
		drawPrompt();
		drawStatus();
		statusDrawn = true;
		while (alive) {
			if (redrawRequired) {
				redraw();
				redrawRequired = false;
			} else {
				
				// nothing is modified so going to sleep
				try {
					Thread.sleep(800);
					// redraw is required to update ets to next pos
					redrawRequired = true;
				} catch (InterruptedException e) {
				}
			}
		}

	}

	private void drawStatus() {
		System.out.print("\n\n");
		System.out
				.printf("%1$10s  %2$10s  %3$10s  %4$10s  %5$10s  %6$6s  %7$14s  %8$10s\n\n",
						"SN", "FlightNo", "Aircraft", "Passanger", "Position",
						"Status", "ETA to next Pos", "Destination");

		
		for (Integer index : flights.keySet()) {
			Flight flight = flights.get(index);
			Position pos = flight.currentPosition;
			System.out
					.printf("%1$10s  %2$10s  %3$10s  %4$10s  %5$10s  %6$6s  %7$6s  %8$18s\n",
							String.valueOf(index), flight.flightNo, flight.model,
							flight.passangers, pos.displyTag(), pos.status,
							String.valueOf(flight.etaToNextPos()), pos.destination.displyTag());
		}
		goToPrompt();
	}

	private void goToPrompt() {
		System.out.print("\u001B[u"); // take the cursor back to the last save location (first row) 
	}

	private void drawPrompt() {
		System.out.print("\u001B[2J"); // clear screen
		System.out.print(input_prompt); // print prompt
		System.out.print("\u001B[s"); // save cursor
	}

	public void redraw() {
		System.out.print("\u001B[s");  // save cursor location
		System.out.print("\u001B[d"); // goto the start of the screen
		System.out.print("\u001B[E"); //come one line down
		System.out.print("\u001B[J"); //erase every thing below
		drawStatus();                    // draw status
		//System.out.print("\u001B[u");
	}

	public void setModified(boolean modified) {
		this.redrawRequired = modified;
		executer.interrupt();
	}

}
