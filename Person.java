import java.util.HashSet;
import java.util.Set;

public class Person {
//	private Set<Person> pred = new HashSet<>();// predecessor list
//	private Set<Person> succ = new HashSet<>();// successor list
	private Set<Person> neighbors = new HashSet<>();
	private String name;// name of the Person
	private boolean visited;

	public Person(String name) {
		this.name = name;
	}

//	public Set<Person> pred() {
//		return this.pred;
//	}
//
//	public Set<Person> succ() {
//		return this.succ;
//	}

	public Set<Person> neighbors() {
		return this.neighbors;
	}
		
	public String name() {
		return this.name;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean getVisited() {
		return visited;
	}

}
