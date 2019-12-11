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
  
  private Graph graph;

  @Override
  public boolean addFriends(String name1, String name2) {
    Person person1 = new Person(name1);
    Person person2 = new Person(name2);
    return this.graph.addEdge(person1, person2);
    
  }

  @Override
  public boolean removeFriends(String name1, String name2) {
    Person person1 = new Person(name1);
    Person person2 = new Person(name2);
    return this.graph.removeEdge(person1, person2);
  }

  @Override
  public boolean addUser(String name) {
    Person person = new Person(name);
    return this.graph.addNode(person);
  }

  @Override
  public boolean removeUser(String name) {
    Person person = new Person(name);
    return this.graph.removeNode(person);
  }

  @Override
  public Set<Person> getFriends(String name) {
    Person person1 = new Person(name);
    return this.graph.getNeighbors(person1);
  }

  @Override
  public Set<Person> getMutualFriends(String name1, String name2) {
    Set<Person> p1Friends = this.getFriends(name1);
    Set<Person> p2Friends = this.getFriends(name2);
    Set<Person> mutualFriends = new HashSet<>(p1Friends);
    mutualFriends.retainAll(p2Friends);
    return mutualFriends;
  }

  @Override
  public List<Person> getShortestPath(String name1, String name2) {
    LinkedList<Person> bfsList = new LinkedList<Person>();
    Queue<Person> queue = new LinkedList<>();
    Map<String, Person> prev = new HashMap<>();
    Person p1 = graph.getNode(name1);
    Person p2 = graph.getNode(name2);
    Person current = p1;

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
        System.out.println("\nThere is no path between " + name1 + " and " + name2);
        return new LinkedList<Person>();
    }
    for (Person node = p2; node != null; node = prev.get(node.name())) {
        bfsList.add(node);
    }
    Collections.reverse(bfsList);

    return bfsList;

}

    
    


  @Override
  public Set<Graph> getConnectedComponents() {
   // Set<Graph> cc = new HashSet<Graph>();
//    int numVert = graph.getNumVert();
//    boolean[] visited = new boolean[numVert]; 
//    for(int v = 0; v < V; ++v) { 
//        if(!visited[v]) { 
//            // print all reachable vertices 
//            // from v 
//            DFSUtil(v,visited); 
//            System.out.println(); 
   // cc.addAll(graph);
    return null;
   // return null;
  }

  @Override
  public void loadFromFile(File file) {
    Scanner scnr = null;
    try {
        scnr = new Scanner(file);
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            String[] wordList = line.split(" ");
            
            if (wordList.length <= 1)
                continue;
            if (wordList[0].equals("a")) {
                if (wordList.length == 2)
                    this.addUser(wordList[1]);
                if (wordList.length == 3)
                    this.addFriends(wordList[1],wordList[2]);
                
            } else if (wordList[0].equals("r")) {
                if (wordList.length == 2)
                    this.removeUser(wordList[1]);
                if (wordList.length == 3)
                    this.removeFriends(wordList[1],wordList[2]);
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

  
  private void setCentralUser(String name) {
    Set<Person> allUsers = graph.getAllNodes();
    Iterator<Person> itr = allUsers.iterator();
    while (itr.hasNext()) {
      String next = itr.next().name();
      addFriends(name, next);
    }
    
  }

  @Override
  public void saveToFile(File file) {
    try {
      
      PrintWriter writer = new PrintWriter(file);
      Set<Person> allNodes = graph.getAllNodes();
      Iterator<Person> itr = allNodes.iterator();
      while (itr.hasNext()) {
        String next = itr.next().name();
        writer.write("a " + next);
        writer.flush();
      }
      
      if (writer != null) {
        writer.close();
      }
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
}
