/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */package uk.dangrew.jtt.model.tests;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 * Interface to the object representing a single test case being run on Jenkins.
 */
public interface TestCase {

   public static final double DEFAULT_DURATION = 0.0;
   public static final boolean DEFAULT_SKIPPED = false;
   public static final TestResultStatus DEFAULT_STATUS = TestResultStatus.FAILED;
   public static final int DEFAULT_AGE = 0;
   
   /**
    * Provides access to the property associated with the duration of the test case.
    * @return the {@link DoubleProperty} providing how long the test case took to run.
    */
   public DoubleProperty durationProperty();

   /**
    * Provides access to the property associated with the name of the test case.
    * @return the {@link StringProperty} providing the name of the test case.
    */
   public StringProperty nameProperty();

   /**
    * Provides access to the property associated with whether the test case was skipped or not.
    * @return the {@link BooleanProperty} providing whether the case was skipped.
    */
   public BooleanProperty skippedProperty();

   /**
    * Provides access to the property associated with the status of the test case.
    * @return the {@link ObjectProperty} providing the current status as a {@link TestResultStatus}.
    */
   public ObjectProperty< TestResultStatus > statusProperty();

   /**
    * Provides access to the property associated with the {@link TestClass} of the test case.
    * @return the {@link DoubleProperty} providing the {@link TestClass} the {@link TestClass} belongs to.
    */
   public ObjectProperty< TestClass > testClassProperty();

   /**
    * Provides access to the property associated with the age of the test case when failing.
    * @return the {@link IntegerProperty} providing the age of the failure.
    */
   public IntegerProperty ageProperty();

}//End Interface
