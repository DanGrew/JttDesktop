/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database;

import javafx.collections.ObservableList;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.tests.TestClass;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.storage.database.events.JenkinsJobPropertyListener;
import uk.dangrew.jtt.storage.database.events.JenkinsUserPropertyListener;

/**
 * {@link JenkinsDatabase} defines the interface for storing Jenkins data such
 * as {@link TestCase}s and {@link TestClass}es.
 */
public interface JenkinsDatabase {

   /**
    * Method to determine whether the {@link JenkinsDatabase} has any {@link TestClass}es.
    * @return true if the {@link JenkinsDatabase} has no {@link TestClass}es.
    */
   public boolean hasNoTestClasses();
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has any {@link JenkinsJob}s.
    * @return true if the {@link JenkinsDatabase} has no {@link JenkinsJob}s.
    */
   public boolean hasNoJenkinsJobs();

   /**
    * Method to determine whether the {@link JenkinsDatabase} has any {@link JenkinsUser}s.
    * @return true if the {@link JenkinsDatabase} has no {@link JenkinsUser}s.
    */
   public boolean hasNoJenkinsUsers();
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has any {@link JenkinsNode}s.
    * @return true if the {@link JenkinsDatabase} has no {@link JenkinsNode}s.
    */
   public boolean hasNoJenkinsNodes();
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has the given {@link TestClass}.
    * @param testClass the {@link TestClass} in question.
    * @return true if contained.
    */
   public boolean containsTestClass( TestClass testClass );
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return true if contained.
    */
   public boolean containsJenkinsJob( JenkinsJob jenkinsJob );
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has the given {@link JenkinsUser}.
    * @param jenkinsUser the {@link JenkinsUser} in question.
    * @return true if contained.
    */
   public boolean containsJenkinsUser( JenkinsUser jenkinsUser );
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has the given {@link JenkinsNode}.
    * @param jenkinsNode the {@link JenkinsNode} in question.
    * @return true if contained.
    */
   public boolean containsJenkinsNode( JenkinsNode jenkinsNode );
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has a {@link TestClass} matching the
    * given {@link TestClassKey}.
    * @param testClassKey the {@link TestClassKey} to identify the {@link TestClass}.
    * @return true if the {@link TestClass} is present.
    */
   public boolean hasTestClass( TestClassKey testClassKey );
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has a {@link JenkinsJob} matching the
    * given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsJob}.
    * @return true if the {@link JenkinsJob} is present.
    */
   public boolean hasJenkinsJob( String key );
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has a {@link JenkinsUser} matching the
    * given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsUser}.
    * @return true if the {@link JenkinsUser} is present.
    */
   public boolean hasJenkinsUser( String key );
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} has a {@link JenkinsNode} matching the
    * given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsNode}.
    * @return true if the {@link JenkinsNode} is present.
    */
   public boolean hasJenkinsNode( String key );

   /**
    * Method to store the given {@link TestClass}. Note that this will replace anything matching the same resulting
    * {@link TestClassKey}.
    * @param testClass the {@link TestClass} to store.
    */
   public void store( TestClass testClass );
   
   /**
    * Method to store the given {@link JenkinsJob}. Note that this will replace anything matching the same resulting
    * {@link String} key.
    * @param jenkinsJob the {@link JenkinsJob} to store.
    */
   public void store( JenkinsJob jenkinsJob );
   
   /**
    * Method to store the given {@link JenkinsUser}. Note that this will replace anything matching the same resulting
    * {@link String} key.
    * @param jenkinsUser the {@link JenkinsUser} to store.
    */
   public void store( JenkinsUser jenkinsUser );
   
   /**
    * Method to store the given {@link JenkinsNode}. Note that this will replace anything matching the same resulting
    * {@link String} key.
    * @param jenkinsNode the {@link JenkinsNode} to store.
    */
   public void store( JenkinsNode jenkinsNode );

   /**
    * Method to get the {@link TestClass} matching the given {@link TestClassKey}.
    * @param testClassKey the {@link TestClassKey} to identify the {@link TestClass}.
    * @return the matching {@link TestClass}, or null.
    */
   public TestClass getTestClass( TestClassKey testClassKey );
   
   /**
    * Method to get the {@link JenkinsJob} matching the given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsJob}.
    * @return the matching {@link JenkinsJob}, or null.
    */
   public JenkinsJob getJenkinsJob( String key );
   
   /**
    * Method to get the {@link JenkinsUser} matching the given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsUser}.
    * @return the matching {@link JenkinsUser}, or null.
    */
   public JenkinsUser getJenkinsUser( String key );
   
   /**
    * Method to get the {@link JenkinsNode} matching the given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsNode}.
    * @return the matching {@link JenkinsNode}, or null.
    */
   public JenkinsNode getJenkinsNode( String key );

   /**
    * Method to remove the {@link TestClass} matching the given {@link TestClassKey}.
    * @param testClassKey the {@link TestClassKey} to identify the {@link TestClass}.
    * @return the removed {@link TestClass}.
    */
   public TestClass removeTestClass( TestClassKey testClassKey );
   
   /**
    * Method to remove the {@link JenkinsJob} matching the given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsJob}.
    * @return the removed {@link JenkinsJob}.
    */
   public JenkinsJob removeJenkinsJob( String key );
   
   /**
    * Method to remove the {@link JenkinsUser} matching the given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsUser}.
    * @return the removed {@link JenkinsUser}.
    */
   public JenkinsUser removeJenkinsUser( String key );
   
   /**
    * Method to remove the {@link JenkinsNode} matching the given {@link String} name key.
    * @param key the {@link String} to identify the {@link JenkinsNode}.
    * @return the removed {@link JenkinsNode}.
    */
   public JenkinsNode removeJenkinsNode( String key );
   
   /**
    * Method to remove the given {@link TestClass} from the {@link JenkinsDatabase}.
    * @param testClass the {@link TestClass} to remove.
    * @return true if removed.
    */
   public boolean removeTestClass( TestClass testClass );
   
   /**
    * Method to remove the given {@link JenkinsJob} from the {@link JenkinsDatabase}.
    * @param jenkinsJob the {@link JenkinsJob} to remove.
    * @return true if removed.
    */
   public boolean removeJenkinsJob( JenkinsJob jenkinsJob );
   
   /**
    * Method to remove the given {@link JenkinsUser} from the {@link JenkinsDatabase}.
    * @param jenkinsUser the {@link JenkinsUser} to remove.
    * @return true if removed.
    */
   public boolean removeJenkinsUser( JenkinsUser jenkinsUser );
   
   /**
    * Method to remove the given {@link JenkinsNode} from the {@link JenkinsDatabase}.
    * @param jenkinsNode the {@link JenkinsNode} to remove.
    * @return true if removed.
    */
   public boolean removeJenkinsNode( JenkinsNode jenkinsNode );
   
   /**
    * Provides the {@link ObservableList} of {@link TestClass}es held by the {@link JenkinsDatabase}.
    * @return the {@link ObservableList} of {@link TestClass}es.
    */
   public ObservableList< TestClass > testClasses();

   /**
    * Provides the {@link ObservableList} of {@link JenkinsJob}s held by the {@link JenkinsDatabase}.
    * @return the {@link ObservableList} of {@link JenkinsJob}s.
    */
   public ObservableList< JenkinsJob > jenkinsJobs();

   /**
    * Provides the {@link ObservableList} of {@link JenkinsUser}s held by the {@link JenkinsDatabase}.
    * @return the {@link ObservableList} of {@link JenkinsUser}s.
    */
   public ObservableList< JenkinsUser > jenkinsUsers();
   
   /**
    * Provides the {@link ObservableList} of {@link JenkinsNode}s held by the {@link JenkinsDatabase}.
    * @return the {@link ObservableList} of {@link JenkinsNode}s.
    */
   public ObservableList< JenkinsNode > jenkinsNodes();
   
   /**
    * Getter for access to the {@link JenkinsJobPropertyListener} allowing global registrations.
    * @return the {@link JenkinsJobPropertyListener}.
    */
   public JenkinsJobPropertyListener jenkinsJobProperties();
   
   /**
    * Getter for access to the {@link JenkinsUserPropertyListener} allowing global registrations.
    * @return the {@link JenkinsUserPropertyListener}.
    */
   public JenkinsUserPropertyListener jenkinsUserProperties();

}//End Interface
