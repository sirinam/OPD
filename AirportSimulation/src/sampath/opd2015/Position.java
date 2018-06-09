package sampath.opd2015;

public class Position {
	String tag;
	int number;
	long duration;
	Position next;
	Position destination;
	String status = "Moving";

	public Position(String tag, int number, long duration, Position destination) {
		this.tag = tag;
		this.number = number;
		this.destination = destination;
		this.duration = duration * 1000;

	}

	Position addNext(Position next) {
		this.next = next;
		return next;
	}

	String displyTag(){
		return tag + "-" + number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (number != other.number)
			return false;
		if (!tag.equals(other.tag))
			return false;
		return true;
	}
	
	
}
