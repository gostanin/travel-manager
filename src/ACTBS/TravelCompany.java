package ACTBS;

import java.util.*;
import ACTBS.SystemExceptions.*;

public class TravelCompany {
	private String name;
	private ArrayList<TravelType> travelList = new ArrayList<TravelType>(); 
	
	public TravelCompany(String name) {
		if(name.length() > 6 || name.length() < 1) {
			throw new AirlineNameLengthException("TravelCompany name is greater than 5 char OR les than 1 char");
		}
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<TravelType> findTravelTypesByOriginDestination(String origin, String destination) {
		ArrayList<TravelType> travelTypes = new ArrayList<TravelType>();
		for(TravelType i: this.travelList) {
			if(origin.equals(i.getOrigin()) && destination.equals(i.getDestination())) {
				travelTypes.add(i); 
			}
		}
		return travelTypes;
	}
	
	public ArrayList<TravelType> findTravelTypes(String origin, String destination, int year, int month, int day) {
		ArrayList<TravelType> travelTypes = new ArrayList<TravelType>();
		for(TravelType i: this.travelList) {
			if(origin.equals(i.getOrigin()) && destination.equals(i.getDestination()) && year == i.getYear() && month == i.getMonth() && day == i.getDay()) {
				travelTypes.add(i); 
			}
		}
		return travelTypes;
	}
	
	public boolean book(String fID, SeatClass seatClass, int row, char col)  {
		for(TravelType i: travelList) {
			if(fID.equals(i.getID())) {
				return i.book(seatClass, row, colToNum(col));
			}
		}
		return false; 
	}

	public boolean addTravelType(String origin, String destination, int year, int month, int day, String ID, TransportationType type) {
		
		TravelType t;
		try {
			if(type.equals(TransportationType.FLIGHT)) {
				t = new Flight(origin, destination, year, day, month, ID);
			} else if(type.equals(TransportationType.SHIP)) {
				t = new Ship(origin, destination, year, day, month, ID);
			} else {
				throw new IllegalArgumentException("TravelType not created, ship or flight not specified");
			}

			for(TravelType i : this.travelList) {
				if(ID.equals(i.getID())) {
					throw new SameFlightIDException("Duplicate TravelType ID");
				}
			}

			this.travelList.add(t);
			return true;
		
		} catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return false;
		}

	}
	
	public boolean addTravelTypeSection(String ID, int rows, int cols, SeatClass seatClass, double price) {
		for(TravelType t : this.travelList) {
			if(t.getID().equals(ID)) {
				try {
					

					//IF flight is not unique, realprice will be set to already established price
					boolean flightUnique = true;
					double realPrice = price; 
					for(TravelType tt: this.travelList) {
						if(tt.getOrigin().equals(t.getOrigin()) && tt.getDestination().equals(t.getDestination())) {
							flightUnique = false;
							List<Section> sections = tt.getSections();
							realPrice = sections.get(sections.indexOf(seatClass)).getPrice();
						}
					}
					if(!flightUnique) {
						System.out.println("Origin/Destination combination not unique, so the price provided was overwritten by previously established price");
					}
					
					t.addSection(new Section(rows, cols, seatClass, price));
					return true;
				}
				catch(RuntimeException e) {
					return false;
				}
			}
		}
		return false;
	}

	/*
	fix it to one line inside of the method that calls it
	 */
	private int colToNum(char col) {
		int colNum = 0;
		switch(Character.toLowerCase(col)) {
        	case 'a' :
        		colNum = 0;
        		break;
        	case 'b' :
        		colNum = 1;
        		break;
        	case 'c' :
        		colNum = 2;
        		break;
        	case 'd' :
        		colNum = 3;
        		break;
        	case 'e' :
        		colNum = 4;
        		break;
        	case 'f' :
        		colNum = 5;
        		break;
        	case 'g' :
        		colNum = 6;
        		break;
        	case 'h' :
        		colNum = 7;
        		break;
        	case 'i' :
        		colNum = 8;
        		break;
        	case 'j' :
        		colNum = 9;
        		break;
        	default :
        		break;
		}
		return colNum;
		
	}
	
	@Override 
	public String toString() {
		String result = "";
		for(TravelType f : this.travelList) {
			result += String.format("%10s %s", this.name, f.toString());
		}
		return result;
	}
}