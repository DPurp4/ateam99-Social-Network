import java.util.HashSet;
import java.util.Set;

/**
 * Filename: Person.java
 * Project: ateam3
 * Authors:
 * 
 * Person implementation
 */
public class Person {
	private Set<Person> neighbors = new HashSet<>();// all the neighbors of the person
	private String name;// name of the Person
	private boolean visited;// whether the person is visited

	/*
	 * Constructor
	 */
	public Person(String name) {
		this.name = name;
	}

	/**
	 * get all the neighbors of a person
	 * 
	 * @return a set containing all the neighbors of the person
	 */
	public Set<Person> neighbors() {
		return this.neighbors;
	}

	/**
	 * get the name of a person
	 * 
	 * @return name of the person
	 */
	public String name() {
		return this.name;
	}

	/**
	 * set whether the person is visited
	 * 
	 * @param whether the person is visited
	 */
	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	/**
	 * get whether the person is visited
	 * 
	 * @return whether the person is visited
	 */
	public boolean getVisited() {
		return visited;
	}

}