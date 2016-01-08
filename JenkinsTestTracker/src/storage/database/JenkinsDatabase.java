/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package storage.database;

import javafx.collections.ObservableList;
import model.tests.TestCase;
import model.tests.TestClass;

/**
 * {@link JenkinsDatabase} defines the interface for storing Jenkins data such
 * as {@link TestCase}s and {@link TestClass}es.
 */
public interface JenkinsDatabase {

   /**
    * Method to determine whether the {@link JenkinsDatabase} is empty.
    * @return true if the {@link JenkinsDatabase} has no {@link TestClass}es.
    */
   public boolean isEmpty();

   /**
    * Method to determine whether the {@link JenkinsDatabase} has a {@link TestClass} matching the
    * given {@link TestClassKey}.
    * @param testClassKey the {@link TestClassKey} to identify the {@link TestClass}.
    * @return true if the {@link TestClass} is present.
    */
   public boolean has( TestClassKey testClassKey );

   /**
    * Method to store the given {@link TestClass}. Note that this will replace anything matching the same resulting
    * {@link TestClassKey}.
    * @param testClass the {@link TestClass} to store.
    */
   public void store( TestClass testClass );

   /**
    * Method to get the {@link TestClass} matching the given {@link TestClassKey}.
    * @param testClassKey the {@link TestClassKey} to identify the {@link TestClass}.
    * @return the matching {@link TestClass}, or null.
    */
   public TestClass get( TestClassKey testClassKey );

   /**
    * Method to remove the {@link TestClass} matching the given {@link TestClassKey}.
    * @param testClassKey the {@link TestClassKey} to identify the {@link TestClass}.
    * @return the removed {@link TestClass}.
    */
   public TestClass remove( TestClassKey testClassKey );

   /**
    * Provides the {@link ObservableList} of {@link TestClass}es held by the {@link JenkinsDatabase}.
    * @return the {@link ObservableList} of {@link TestClass}es.
    */
   public ObservableList< TestClass > testClasses();

}//End Interface
