package sampath.opd2015;

public class Launcher {

	public static void main(String[] args) {
		
		//Airport airport = new Airport();
		Airport airport = new AirportWithThread();
		airport.openForBusiness();
	}
}
