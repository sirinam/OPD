package sampath.opd2015;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Airport {

	public static final int HeadWay = 100;
	public static final int TaxiWayLength = 300;
	public static final String TSFG_TAG = "TSFG";
	public static final String TSFR_TAG = "TSFR";
	public static final String GATE_TAG = "GATE";
	public static final int defaultGate = 1;
	public static final String RUNWAY_TAG = "Runway";
	Map<String, Position> posMap = new HashMap<String, Position>();

	Console console;
	LTC ltc;

	public Airport() {
		console = new Console(this);
		ltc = new LTC(this);
	}

	Map<Integer, Flight> flights = new HashMap<Integer, Flight>();
	public boolean open = false;
	private Thread ltcThrd;

	public int addFlight(Flight flight) {
		int key = flights.size() + 1;
		flights.put(key, flight);
		console.update();
		return key;
	}

	public void handlePositionChange(Position oldpos, Position currentPosition) {
		String displyTag = currentPosition.displyTag();
		if (posMap.keySet().contains(displyTag)) {
			posMap.get(displyTag).status = "Collided";
			currentPosition.status = "Collided";
			console.update();
			while(console.writer.redrawRequired);
			close();
		} else {
			posMap.put(displyTag, currentPosition);
			posMap.remove(oldpos.displyTag());
			console.update();
		}
	}

	public void openForBusiness() {
		open = true;
		console.start();
		ltcThrd = new Thread(ltc, "LTC");
		ltcThrd.start();

	}

	public void close() {

		open = false;

		try {
			console.writer.alive = false;
			console.writer.executer.join();
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		try {
			System.in.close();
			Thread executionar = console;
			if(!Thread.currentThread().equals(executionar))
				console.join();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			//e2.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		try {
			ltcThrd.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (Integer index : flights.keySet()) {
			try {
				Thread executionar = flights.get(index).executionar;
				if(!Thread.currentThread().equals(executionar))
					executionar.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}

	}

	public void removeFlight(int index) {
		flights.remove(index);
	}

	public void sendCommad(String command) {
		String[] tkn = command.split(" ");
		try {
			int index = Integer.parseInt(tkn[0]);
			flights.get(index).handleCommand(tkn);
		} catch (NumberFormatException nfe) {

		}
	}

}
