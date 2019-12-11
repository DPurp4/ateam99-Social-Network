import java.util.HashSet;
import java.util.Set;

public class Person {
	private Set<Person> pred = new HashSet<>();// predecessor list
	private Set<Person> succ = new HashSet<>();// successor list
	private String name;// name of the Person
	
	public Person(String name) {
		this.name = name;
	}
	
	public Set<Person> pred() {
		return this.pred;
	}
	
	public Set<Person> succ() {
		return this.succ;
	}
	
	public String name() {
		return this.name;
	}
}
