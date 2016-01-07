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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Basic implementation of the {@link TestClass}.
 */
public class TestClassImpl implements TestClass {

   static final double DEFAULT_DURATION = 0.0;
   
   private StringProperty name;
   private StringProperty location;
   private DoubleProperty duration;
   private ObservableList< TestCase > testCases;

   /**
    * Constructs a new {@link TestCaseImpl}.
    * @param name the name of the {@link TestCase}, cannot be null.
    * @param location the package location of the {@link TestCase}, cannot be null. 
    */
   public TestClassImpl( String name, String location ) {
      if ( name == null ) throw new IllegalArgumentException( "Null name provided." );
      if ( name.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid name provided." );
      if ( location == null ) throw new IllegalArgumentException( "Null location provided." );
      if ( location.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid location provided." );
      
      this.name = new SimpleStringProperty( name );
      this.location = new SimpleStringProperty( location );
      this.duration = new SimpleDoubleProperty( DEFAULT_DURATION );
      this.testCases = FXCollections.observableArrayList();
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public StringProperty nameProperty() {
      return name;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public StringProperty locationProperty() {
      return location;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public DoubleProperty durationProperty() {
      return duration;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< TestCase > testCasesList() {
      return testCases;
   }//End Method

}//End Class
