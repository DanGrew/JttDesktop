/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.tests;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Provides the basic implementation of the {@link TestCase}.
 */
public class TestCaseImpl implements TestCase {
   
   private StringProperty name;
   private ObjectProperty< TestClass > testClass;
   private DoubleProperty duration;
   private BooleanProperty skipped;
   private ObjectProperty< TestResultStatus > status;
   private IntegerProperty age;

   /**
    * Constructs a new {@link TestCaseImpl}.
    * @param name the name of the {@link TestCase}, cannot be null.
    * @param testClass the {@link TestClass} the {@link TestCase} is associated with, cannot be null.
    */
   public TestCaseImpl( String name, TestClass testClass ) {
      if ( name == null ) throw new IllegalArgumentException( "Null name for test case." );
      if ( name.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid name for test case." );
      if ( testClass == null ) throw new IllegalArgumentException( "Null test class for test case." );
      
      this.name = new SimpleStringProperty( name );
      this.testClass = new SimpleObjectProperty< TestClass >( testClass );
      this.duration = new SimpleDoubleProperty( DEFAULT_DURATION );
      this.skipped = new SimpleBooleanProperty( DEFAULT_SKIPPED );
      this.status = new SimpleObjectProperty< TestResultStatus >( DEFAULT_STATUS );
      this.age = new SimpleIntegerProperty( DEFAULT_AGE );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public DoubleProperty durationProperty() {
      return duration;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public StringProperty nameProperty() {
      return name;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public BooleanProperty skippedProperty() {
      return skipped;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< TestResultStatus > statusProperty() {
      return status;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< TestClass > testClassProperty() {
      return testClass;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty ageProperty() {
      return age;
   }//End Method

}//End Class
