
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 */

/**
 * Implementation of SocialNetworkADT
 * 
 * @author Devin DuBeau
 *
 */
public class SocialNetwork implements SocialNetworkADT {

  private Graph graph; // graph of all users in social network
  private String centralUser; // central user (perspective from which network will be explored

  /**
   * Getter method for central user
   * 
   * @return the central user
   */
  public String getCentralUser() {
    return centralUser;
  }



  /**
   * Constructor
   */
  public SocialNetwork() {
    graph = new Graph();

  }

  /**
   * Gets all the people in this social network
   * 
   * @return a set containing all people in the network
   */
  public Set<Person> getAllPeople() {
    return graph.getAllNodes();
  }

  /**
   * Adds a friendship between two existing users
   * 
   * Note: friendships are undirected, if user A is friends with user B, then user B is friends with
   * user A
   */
  @Override
  public boolean addFriends(String name1, String name2) {
    Person person1 = new Person(name1);
    Person person2 = new Person(name2);
    return this.graph.addEdge(person1, person2);

  }

  /**
   * Removes a friendship between two existing users
   */
  @Override
  public boolean removeFriends(String name1, String name2) {
    Person person1 = new Person(name1);
    Person person2 = new Person(name2);
    return this.graph.removeEdge(person1, person2);
  }

  /**
   * Adds a user to this social network
   */
  @Override
  public boolean addUser(String name) {
    Person person = new Person(name);
    return this.graph.addNode(person);
  }

  /**
   * Removes a user from this social network
   */
  @Override
  public boolean removeUser(String name) {
    Person person = new Person(name);
    return this.graph.removeNode(person);
  }

  /**
   * Returns all the friends of the user specified
   */
  @Override
  public Set<Person> getFriends(String name) {
    Person person1 = new Person(name);
    return this.graph.getNeighbors(person1);
  }

  /**
   * Returns all the mutual friends between the two users specified
   */
  @Override
  public Set<Person> getMutualFriends(String name1, String name2) {
    Set<Person> p1Friends = this.getFriends(name1);
    Set<Person> p2Friends = this.getFriends(name2);
    Set<Person> mutualFriends = new HashSet<>(p1Friends);
    mutualFriends.retainAll(p2Friends); // intersection between two sets
    return mutualFriends;
  }

  /**
   * Gets the shortest path between two users
   */
  @Override
  public List<Person> getShortestPath(String name1, String name2) {
    LinkedList<Person> bfsList = new LinkedList<Person>();
    Queue<Person> queue = new LinkedList<>();
    Map<String, Person> prev = new HashMap<>();
    Person p1 = graph.getNode(name1); // start vertex
    Person p2 = graph.getNode(name2); // end vertex
    Person current = p1; // current vertex

    queue.add(current);
    current.setVisited(true);

    while (!queue.isEmpty()) {

      current = queue.remove();

      if (current.name().equals(name2)) {
        break;
      } else {
        Set<Person> currentFriends = graph.getNeighbors(current);
        for (Person currentFriend : currentFriends) {
          if (!currentFriend.getVisited()) {
            queue.add(currentFriend);
            currentFriend.setVisited(true);
            prev.put(currentFriend.name(), current);
          }
        }
      }
    }
    if (!current.name().equals(name2)) {
      return null;
    }
    for (Person node = p2; node != null; node = prev.get(node.name())) {
      bfsList.add(node);
    }
    Collections.reverse(bfsList);

    return bfsList;

  }


  /**
   * Depth first search recursive helper method for connectedComponents
   * 
   * @param p    - person being visited & added to temp
   * @param temp - temporary graph containing connected components
   */
  void DFSUtil(Person p, Graph temp) {
    p.setVisited(true); // mark user as visited
    temp.addNode(p); // add user to temporary graph
    for (Person neighbor : graph.getNeighbors(p)) { // for each friend the person has
      if (!neighbor.getVisited()) // if the friend has not been visited yet
        DFSUtil(neighbor, temp); // call recursive helper method
    }
  }

  void connectedComponents(Graph temp, Set<Graph> cc) {
    for (Person visited : graph.getAllNodes()) {
      visited.setVisited(false); // initially mark all users as not visited
    }
    for (Person person : graph.getAllNodes()) { // for each person in the list of people
      if (!person.getVisited()) { // if person has not been visited yet
        DFSUtil(person, temp); // call recursive helper method
        cc.add(temp); // add temp as one element of the "connected components" set
        temp = new Graph(); // clear temp
      }
    }
  }



  /**
   * Returns a set of graphs, each graph containing the connected components of this social network
   */
  @Override
  public Set<Graph> getConnectedComponents() {
    Graph temp = new Graph();
    Set<Graph> cc = new HashSet<Graph>();
    connectedComponents(temp, cc);
    return cc;
  }

  /**
   * Reads from a text file and creates a graph network
   */
  @Override
  public void loadFromFile(File file) {
    Scanner scnr = null;
    try {
      scnr = new Scanner(file);
      boolean hasDefault = false;
      while (scnr.hasNextLine()) {
        String line = scnr.nextLine().trim();
        String[] wordList = line.split(" ");

        if (wordList.length <= 1)
          continue;
        if (wordList[0].equals("a")) {
          if (!hasDefault) {
            hasDefault = true;
            centralUser = wordList[1];
          }
          if (wordList.length == 2)
            this.addUser(wordList[1]);
          if (wordList.length == 3)
            this.addFriends(wordList[1], wordList[2]);

        } else if (wordList[0].equals("r")) {
          if (wordList.length == 2)
            this.removeUser(wordList[1]);
          if (wordList.length == 3)
            this.removeFriends(wordList[1], wordList[2]);
        } else if (wordList[0].equals("s")) {
          setCentralUser(wordList[1]);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      if (scnr != null)
        scnr.close();
    }
  }

  /**
   * Sets central user
   * 
   * @param name
   */

  public void setCentralUser(String name) {
    this.centralUser = name;

  }

  /**
   * Writes current graph network to a text file in a specific format.
   */
  @Override
  public void saveToFile(File file) {
    try {

      PrintWriter writer = new PrintWriter(file);
      Set<Person> allNodes = graph.getAllNodes();
      Iterator<Person> itr = allNodes.iterator();
      while (itr.hasNext()) {
        if (itr.next().name() != null) {
          String next = itr.next().name();
          writer.write("a " + next + "\n");
          writer.flush();
        }
      }

      if (writer != null) {
        writer.close();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

}


