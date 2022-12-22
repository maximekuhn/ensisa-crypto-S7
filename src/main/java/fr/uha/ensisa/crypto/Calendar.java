package fr.uha.ensisa.crypto;

public class Calendar {
	private EventTable eventTable;
	private String name;
	
	public Calendar(String name) {
		eventTable = new EventTable();
		this.name = name;
	}
	
	public EventTable getEventTable() {
		return eventTable;
	}
	
	public void saveCalendar() {
		
	}
	
}
