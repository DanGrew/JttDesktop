/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
 package uk.dangrew.jtt.model.tests;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Interface defining the properties of a test class being run by Jenkins.
 */
public interface TestClass {
   
   public static final double DEFAULT_DURATION = 0.0;

   /**
    * Provides the property defining the name of the {@link TestClass}.
    * @return the {@link StringProperty}.
    */
   public StringProperty nameProperty();

   /**
    * Provides the property defining the location of the {@link TestClass}.
    * @return the {@link StringProperty}.
    */
   public StringProperty locationProperty();
   
   /**
    * Method to get a description of the {@link TestClass}. This represents the full name of the 
    * {@link TestClass} as {@link #locationProperty()}"."{@link #nameProperty()}.
    * @return a {@link String} description.
    */
   public String getDescription();

   /**
    * Provides the property defining how long it took to run the {@link TestClass}.
    * @return the {@link DoubleProperty}.
    */
   public DoubleProperty durationProperty();

   /**
    * Provides the {@link ObservableList} of {@link TestCase}s defined in the {@link TestClass}.
    * @return the {@link ObservableList}.
    */
   public ObservableList< TestCase > testCasesList();
   
   /**
    * Method to add a {@link TestCase} to the {@link TestClass}. This will replace ant {@link TestCase}
    * that shares the same name.
    * @param testCase the {@link TestCase} to add.
    */
   public void addTestCase( TestCase testCase );
   
   /**
    * Method to determine whether the {@link TestClass} has a {@link TestCase} matching the given name.
    * @param name the name of the {@link TestCase}.
    * @return true if found.
    */
   public boolean hasTestCase( String name );
   
   /**
    * Method to get the {@link TestCase} held by this {@link TestClass} for the given name.
    * @param name the name of the {@link TestCase} to get.
    * @return the {@link TestCase} found.
    */
   public TestCase getTestCase( String name );
   
   /**
    * Method to remove the given {@link TestCase} from the {@link TestClass}.
    * @param testCase the {@link TestCase} to remove.
    */
   public void removeTestCase( TestCase testCase );

}//End Interface
