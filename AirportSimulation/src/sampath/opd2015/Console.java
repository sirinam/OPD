package sampath.opd2015;

import java.util.Scanner;

public class Console extends Thread {

	StatusWriter writer;
	Airport airport;

	public Console(Airport airport) {
		super.setName("Console");
		this.airport = airport;
		writer = new StatusWriter(airport.flights);

	}

	public void run() {
		try {
			startWriter();

			while (!writer.statusDrawn) {

			}
			String command = "";
			Scanner sc = new Scanner(System.in);
			while (true) {
				command = sc.nextLine();
				resetCursor();
				writer.setModified(true);
				if (command.equals("e"))
					break;
				airport.sendCommad(command);
			}
			airport.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	private void resetCursor() {
		System.out.print("\u001B[F");  // move cursor up one line and to first column
		System.out.print("\u001B[" + writer.input_prompt.length() +"G"); //move cursor to column to the end of the prompt text
		System.out.print("\u001B[s"); // save the cursor position 
		System.out.print("\u001B[K"); // remove any remaining text on the same line 
	}

	private void startWriter() {
		Thread writerThread = new Thread(writer, "writer");
		writer.executer = writerThread;
		writerThread.start();
	}

	public void update() {
		writer.setModified(true);
	}

}
