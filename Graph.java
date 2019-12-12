package application;

import java.util.HashSet;
import java.util.Set;

/**
 * Filename: Graph.java
 * Project: ateam3
 * Authors:
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
	private Set<Person> verts;// set that stores all the vertices

	/*
	 * Default no-argument constructor
	 */
	public Graph() {
		verts = new HashSet<>();
	}

	/**
	 * Add new Person to the graph.
	 *
	 * @param p person
	 * @return false if Person is null or already exists, true otherwise
	 */
	public boolean addNode(Person p) {
		if (p == null || has(p))
			return false;

		verts.add(p);// add Person
		return true;
	}

	/**
	 * Remove a Person and all associated edges from the graph
	 * 
	 * @param p person
	 * @return false if Person is null or does not exist, true otherwise
	 */
	public boolean removeNode(Person p) {
		if (p == null || !has(p))
			return false;

		for (Person vert : verts)
			if (vert.name().equals(p.name())) {
				for (Person neighbor : getNeighbors(vert))
					removeEdge(vert, neighbor);
				verts.remove(vert);// delete Person
				break;
			}
		return true;
	}

	/**
	 * Add the edge from p1 to p2 to this graph If either Person does not exist, add
	 * Person, and add edge.
	 * 
	 * @param p1 person1
	 * @param p2 person2
	 * @return false if either Person is null, or if an edge from p1 to p2 already
	 *         exists, true if add is successful
	 */
	public boolean addEdge(Person p1, Person p2) {
		if (p1 == null || p2 == null)
			return false;

		if (!has(p1))
			addNode(p1);
		if (!has(p2))
			addNode(p2);
		boolean edgeExist = false;
		for (Person v2 : verts)// go through all the Person
			if (v2.name().equals(p2.name()))
				for (Person v1 : verts)
					if (v1.name().equals(p1.name())) {
						for (Person v : v2.neighbors())
							if (v.equals(v1))// edge found
								edgeExist = true;
						if (edgeExist)
							return false;

						v2.neighbors().add(v1);// update neighbors
						v1.neighbors().add(v2);
						break;
					}
		return true;
	}

	/**
	 * Remove the edge from p1 to p2 from this graph
	 * 
	 * @param p1 person1
	 * @param p2 person2
	 * @return false if either Person is null, or if an edge from p1 to p2 does not
	 *         exist, true if remove is successful
	 */
	public boolean removeEdge(Person p1, Person p2) {
		boolean edgeExist = false;
		if (p1 == null || p2 == null || !has(p1) || !has(p2))
			return false;
		for (Person v2 : verts)// go through all the Person
			if (v2.name().equals(p2.name()))
				for (Person v1 : verts)
					if (v1.name().equals(p1.name())) {// edge found
						v2.neighbors().remove(v1);// update neighbors
						v1.neighbors().remove(v2);
						edgeExist = true;
						break;
					}
		if (!edgeExist)
			return false;

		return true;
	}

	/**
	 * Returns a Set that contains all the people
	 * 
	 * @return a set containing all the people
	 */
	public Set<Person> getAllNodes() {
		return verts;
	}

	/**
	 * Get all the neighbors of a Person
	 *
	 * @param p person
	 * @return a set containing all the neighbors of a person
	 */
	public Set<Person> getNeighbors(Person p) {
		return getNode(p.name()).neighbors();
	}

	/**
	 * Return a person given its name
	 *
	 * @param name the name of the person
	 * @return the person corresponds to the given name
	 */
	public Person getNode(String name) {
		for (Person vert : verts) {
			if (vert.name().equals(name))// contain
				return vert;
		}
		return null;
	}

	/**
	 * Check if a person is in the list
	 *
	 * @param p person
	 * @return true if the person exists, false otherwise
	 */
	private boolean has(Person p) {
		for (Person vert : verts) {
			if (vert.name().equals(p.name()))// contain
				return true;
		}
		return false;
	}
}
