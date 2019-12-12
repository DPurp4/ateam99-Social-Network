//                  ALL STUDENTS COMPLETE THESE SECTIONS
// Title:           Social Network Visualizer Program
// Files:           Person.java
// Semester:        Autumn 2019
//
// ATeam members:	Devin DuBeau, LEC 002, ddubeau@wisc.edu, dubeau
//					Mihir Arora, LEC 001, mrarora@wisc.edu, marora
//					Xiaoyuan Liu, LEC 001, xliu798@wisc.edu, xiaoyuanl
//					Yuehan Qin, LEC 001, yqin43@wisc.edu, yuehan
//					Reid Chen, LEC 001, ychen878@wisc.edu, reid
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//                   must fully acknowledge and credit those sources of help.
//                   Instructors and TAs do not have to be credited here,
//                   but tutors, roommates, relatives, strangers, etc do.
//
// Persons:          Identify persons by name, relationship to you, and email.
//                   Describe in detail the the ideas and help they provided.
//
// Online sources:   avoid web searches to solve your problems, but if you do
//                   search, be sure to include Web URLs and description of
//                   of any information you find.
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.HashSet;
import java.util.Set;

/**
 * Filename: Person.java
 * Project: a3
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
