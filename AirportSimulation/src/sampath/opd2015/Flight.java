package sampath.opd2015;

public class Flight implements Runnable {
	final static int riMax = 4;
	final static int roMax = 3;
	final static int vt = 20;
	final static double PASSENGER_FACTOR = 0.002;
	final static Position DEST_RUNWAY = new Position(Airport.RUNWAY_TAG, 0, 0, null);
	
	
	String flightNo;
	String model;
	int passangers;
	Airport airport;
	Position gate;
	Position currentPosition;
	boolean supposeToMove = true;
	long lastSleepTime = 0;
	Thread executionar;
	boolean sleeping;

	public Flight(Airport airport, int passengers, String flightNo, String model) {
		this.airport = airport;
		passangers = passengers;
		this.flightNo = flightNo;
		this.model = model;

		createGroundMovementPlan();
	}

	private void createGroundMovementPlan() {
		//int turnaroundTime = (int)(PASSENGER_FACTOR * passengers);
		int turnaroundTime = 10;
		int ri = 3;

		int sleepduration = Airport.HeadWay / vt;
		int numSectTs = Airport.TaxiWayLength / Airport.HeadWay;

		gate = new Position(Airport.GATE_TAG, Airport.defaultGate,
				turnaroundTime, DEST_RUNWAY);
		gate.status = "Boarding";

		currentPosition = new Position("In air", 0, 0, DEST_RUNWAY);
		currentPosition.addNext(new Position(Airport.RUNWAY_TAG, 0, ri, gate));

		Position temp1 = currentPosition.next;
		Position temp2 = gate;
		for (int i = 1; i <= numSectTs; i++) {
			temp1 = temp1.addNext(new Position(Airport.TSFG_TAG, i,
					sleepduration, gate));
			temp2 = temp2.addNext(new Position(Airport.TSFR_TAG, i,
					sleepduration, DEST_RUNWAY));
		}
		temp1.next = gate;
		temp2.next = DEST_RUNWAY;
	}

	@Override
	public void run() {
		long sleptDuration = 0;
		
		int token = airport.addFlight(this);
		boolean tripFinished = currentPosition.next == null;
		while (airport.open) {
			if (tripFinished) {
				airport.removeFlight(token);
				break;
			} else {
				try {
					if (supposeToMove) {
						currentPosition.status = "Moving";
						sleptDuration = System.currentTimeMillis() - lastSleepTime;
						
						if (sleptDuration > currentPosition.duration ) {
							moveToNextPosition();
							sleeping = true;
							// sleeping to simulate moving
							lastSleepTime = System.currentTimeMillis();
							Thread.sleep(currentPosition.duration);
						} else {
							// sleeping to simulate moving
							sleeping = true;
							Thread.sleep(currentPosition.duration - sleptDuration);
						}
					} else {
						currentPosition.status = "Waiting";
						// sleeping to simulate waiting
						sleeping = true;
						Thread.sleep(1000);

					}
				} catch (InterruptedException e) {
				}
				sleeping = false;
			}
			tripFinished = currentPosition.next == null;
		}

	}

	private void moveToNextPosition(){
		Position oldpos;
		oldpos = currentPosition;
		currentPosition = currentPosition.next;
		airport.handlePositionChange(oldpos,
				currentPosition);
	}

	public long etaToNextPos() {
		return (currentPosition.duration - System.currentTimeMillis() + lastSleepTime) / 1000;
	}

	public void handleCommand(String[] tkn) {
		if (tkn.length > 2 && tkn[1].equals("g")) {
			try {
				int n = Integer.parseInt(tkn[2]);
				gate.number = n > 0 && n < 5 ? n : gate.number;
			} catch (NumberFormatException e) {

			}
		} else {
			supposeToMove = tkn[1].equals("m")
					|| (!tkn[1].equals("w") && supposeToMove);
			if (sleeping)
				executionar.interrupt();
		}

	}
}
