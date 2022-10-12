package org.mobon.billing.consumer;

import java.util.Map;

import com.owlike.genson.Genson;

public class GensonTest {

	public static void main(String[] args) {
		Place place = new Place();
		place.setName("World");
		Human human = new Human();
		human.setMessage("Hi");
		human.setPlace(place);
		
		// convert to json
		String jsonString = new Genson().serialize(human);
		System.out.println("json " + jsonString); // print "json {"message":"Hi","place":{"name":"World"}}"
		
		// convert from json
		Map map = new Genson().deserialize(jsonString, Map.class);
		System.out.println(map);
	}

}

class Human{
	String message;
	Place place;
	@Override
	public String toString() {
		return "Human [message=" + message + ", place=" + place + "]";
	}
	public void say() {
		System.out.println( this.toString() );
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
	}
}
class Place{
	String name;
	@Override
	public String toString() {
		return "Place [name=" + name + "]";
	}
	public Place() {
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}