package sampath.opd2015;


public class AirportWithThread  extends Airport implements Runnable {
	
	Position oldpos;
	Position currentPosition;
	boolean checkPosition = false;
	String command;
	boolean newCommand;
	Thread executor;
	boolean sleeping = false;

	public AirportWithThread() {
		console = new Console(this);
		ltc= new LTC(this);
	}

	@Override
	public void run() {
		super.openForBusiness();
		while(true){
			if(!open){
				super.close();
				break;
			} else if(newCommand){
				newCommand = false;
				super.sendCommad(command);
			} else if(checkPosition){
				checkPosition = false;
				super.handlePositionChange(oldpos, currentPosition);
			} else {
				try {
					sleeping = true;
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				sleeping = false;
			}
			
		}
	}
	
	@Override
	public void openForBusiness() {
		open = true;
		executor = new Thread(this, "Airport");
		executor.start();
	}
	

	@Override
	public void handlePositionChange(Position oldpos, Position currentPosition) {
		this.oldpos = oldpos;
		this.currentPosition = currentPosition;
		checkPosition = true;
		if(sleeping)
			executor.interrupt();
	}
	
	@Override
	public void sendCommad(String command) {
		this.command = command;
		newCommand = true;
		if (sleeping)
			executor.interrupt();
	}
	
	
	@Override
	public void close() {
		open = false;
		if (sleeping)
			executor.interrupt();
	}
}
