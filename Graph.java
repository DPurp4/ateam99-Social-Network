import java.util.HashSet;
import java.util.Set;

/**
 * Filename: Graph.java Project: p4 Authors:
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
	private int numEdge;// number of edges
	private int numVert;// number of vertices
	private Set<Person> verts;// set that stores all the vertices

	/*
	 * Default no-argument constructor
	 */
	public Graph() {
		numEdge = 0;
		numVert = 0;
		verts = new HashSet<>();
	}

	/**
	 * Add new Person to the graph.
	 *
	 * If Person is null or already exists, method ends without adding a Person or
	 * throwing an exception.
	 * 
	 * Valid argument conditions: 1. Person is non-null 2. Person is not already in
	 * the graph
	 */
	public boolean addNode(Person p) {
		try {
			if (p == null || contains(p))
				throw new Exception();

			verts.add(p);// add Person
			numVert++;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Remove a Person and all associated edges from the graph.
	 * 
	 * If Person is null or does not exist, method ends without removing a Person,
	 * edges, or throwing an exception.
	 * 
	 * Valid argument conditions: 1. Person is non-null 2. Person is not already in
	 * the graph
	 */
	public boolean removeNode(Person p) {
		try {
			if (p == null || !contains(p))
				throw new Exception();

			for (Person vert : verts)
				if (vert.name().equals(p.name())) {
					verts.remove(vert);// delete Person
					numVert--;
					break;
				}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Add the edge from p1 to p2 to this graph. (edge is directed and unweighted)
	 * If either Person does not exist, add Person, and add edge, no exception is
	 * thrown. If the edge exists in the graph, no edge is added and no exception is
	 * thrown.
	 * 
	 * Valid argument conditions: 1. neither Person is null 2. both vertices are in
	 * the graph 3. the edge is not in the graph
	 */
	public boolean addEdge(Person p1, Person p2) {
		try {
			if (p1 == null || p2 == null || (!contains(p1) && !contains(p2)))
				throw new Exception();

			if (!contains(p1))
				addNode(p1);
			if (!contains(p2))
				addNode(p2);
			boolean edgeExist = false;
			for (Person v2 : verts)// go through all the Person
				if (v2.name().equals(p2.name()))
					for (Person v1 : verts)
						if (v1.name().equals(p1.name())) {
							for (Person v : v2.pred())
								if (v.equals(v1))// edge found
									edgeExist = true;
							if (edgeExist)
								break;

							v2.pred().add(v1);// update predecessor and successor
							v1.succ().add(v2);
							numEdge++;
							break;
						}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Remove the edge from p1 to p2 from this graph. (edge is directed and
	 * unweighted) If either Person does not exist, or if an edge from p1 to p2 does
	 * not exist, no edge is removed and no exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither Person is null 2. both vertices are in
	 * the graph 3. the edge from p1 to p2 is in the graph
	 */
	public boolean removeEdge(Person p1, Person p2) {
		try {
			boolean edgeExist = false;
			if (p1 == null || p2 == null || !contains(p1) || !contains(p2))
				throw new Exception();
			for (Person v2 : verts)// go through all the Person
				if (v2.name().equals(p2.name()))
					for (Person v1 : verts)
						if (v1.name().equals(p1.name())) {// edge found
							v2.pred().remove(v1);// update predecessor and successor
							v1.succ().remove(v2);
							numEdge--;
							edgeExist = true;
							break;
						}
			if (!edgeExist)
				throw new Exception();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a Set that contains all the vertices
	 * 
	 */
	public Set<Person> getAllNodes() {
		return verts;
	}

	/**
	 * Get all the neighbor (adjacent) vertices of a Person
	 *
	 */
	public Set<Person> getNeighbors(Person p) {
		Set<Person> neighbors = new HashSet<Person>();
		for (Person v : verts)
			if (v.name().equals(p.name())) {// get predecessors
				for (Person adjV : v.succ())
					neighbors.add(adjV);
				for (Person adjV : v.pred())
					if (!neighbors.contains(adjV))
						neighbors.add(adjV);
				break;
			}
		return neighbors;
	}

	public Person getNode(String name) {
		for (Person vert : verts) {
			if (vert.name().equals(name))// contain
				return vert;
		}
		return null;
	}
//	/**
//	 * Returns the number of edges in this graph.
//	 */
//	public int size() {
//		return numEdge;
//	}
//
//	/**
//	 * Returns the number of vertices in this graph.
//	 */
//	public int order() {
//		return numVert;
//	}

	/**
	 * Check if a Person is in the list
	 *
	 */
	private boolean contains(Person p) {
		for (Person vert : verts) {
			if (vert.name().equals(p.name()))// contain
				return true;
		}
		return false;
	}

}
