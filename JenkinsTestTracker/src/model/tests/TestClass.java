/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
 package model.tests;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Interface defining the properties of a test class being run by Jenkins.
 */
public interface TestClass {

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
    * Provides the property defining how long it took to run the {@link TestClass}.
    * @return the {@link DoubleProperty}.
    */
   public DoubleProperty durationProperty();

   /**
    * Provides the {@link ObservableList} of {@link TestCase}s defined in the {@link TestClass}.
    * @return the {@link ObservableList}.
    */
   public ObservableList< TestCase > testCasesList();

}//End Interface
