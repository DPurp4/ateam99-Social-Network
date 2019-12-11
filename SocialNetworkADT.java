package application;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * 
 */

/**
 * 
 * Defines methods required for social network visualization
 * 
 * @author Devin DuBeau
 *
 */
public interface SocialNetworkADT {
  
  public boolean addFriends(String name1, String name2);
  public boolean removeFriends(String name1, String name2);
  public boolean addUser(String name);
  public boolean removeUser(String name);
  public Set<Person> getFriends(String name);
  public Set<Person> getMutualFriends(String name1, String name2);
  public List<Person> getShortestPath(String name1, String name2);
  public Set<Graph> getConnectedComponents();
  public void loadFromFile(File file);
  public void saveToFile(File file);
  
  
}
