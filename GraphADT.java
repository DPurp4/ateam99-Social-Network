import java.util.Set;

/**
 * Filename:   GraphADT.java
 * Project:    ateam
 * Authors:    
 * 
 * A simple graph interface
 */
public interface GraphADT {

    public boolean addEdge(Person p1, Person p2);

    public boolean removeEdge(Person p1, Person p2);

    public boolean addNode(Person p);

    public boolean removeNode(Person p);  

    public Set<Person> getNeighbors(Person p);   
    
    public Person getNode(String str);
    
    public Set<Person> getAllNodes();
}
