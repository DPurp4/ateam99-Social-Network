///////////////////////////////////////////////////////////////////////////////
//                  ALL STUDENTS COMPLETE THESE SECTIONS
// Title:           Social Network Visualizer Program
// Files:           GraphADT.java
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

import java.util.Set;

/**
 * Filename:   GraphADT.java
 * Project:    a3
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
