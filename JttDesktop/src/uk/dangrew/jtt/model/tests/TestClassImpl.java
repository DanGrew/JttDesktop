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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import uk.dangrew.jtt.storage.structure.MappedObservableStoreManagerImpl;
import uk.dangrew.jtt.storage.structure.ObjectStoreManager;

/**
 * Basic implementation of the {@link TestClass}.
 */
public class TestClassImpl implements TestClass {

   private StringProperty name;
   private StringProperty location;
   private DoubleProperty duration;
   private ObjectStoreManager< String, TestCase > testCases;

   /**
    * Constructs a new {@link TestCaseImpl}.
    * @param name the name of the {@link TestCase}, cannot be null.
    * @param location the package location of the {@link TestCase}, cannot be null. 
    */
   public TestClassImpl( String name, String location ) {
      if ( name == null ) throw new IllegalArgumentException( "Null name provided." );
      if ( name.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid name provided." );
      if ( location == null ) throw new IllegalArgumentException( "Null location provided." );
      if ( location.length() > 0 && location.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid location specified as space." );
      
      this.name = new SimpleStringProperty( name );
      this.location = new SimpleStringProperty( location );
      this.duration = new SimpleDoubleProperty( DEFAULT_DURATION );
      this.testCases = new MappedObservableStoreManagerImpl<>();
   }//End Constructor

   /**
    * Constructs a new {@link TestClassImpl} with the given full name of the {@link TestClass}.
    * @param fullName the name including location, such as package.subpackage.ClassName
    */
   public TestClassImpl( String fullName ) {
      this( identifyName( fullName ), identifyLocation( fullName ) );
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
   @Override public String getDescription() {
      return location.get() + "." + name.get();
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
      return testCases.objectList();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void addTestCase( TestCase testCase ) {
      testCases.store( testCase.nameProperty().get(), testCase );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasTestCase( String name ) {
      return testCases.has( name );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public TestCase getTestCase( String name ) {
      return testCases.get( name );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void removeTestCase( TestCase testCase ) {
      testCases.remove( testCase.nameProperty().get() );
   }//End Method
   
   /**
    * Method to identify the name of the {@link TestClass} from the full name.
    * @param fullName the name including location, such as package.subpackage.ClassName
    * @return the {@link TestClass} name.
    */
   public static final String identifyName( String fullName ) {
      if ( fullName == null ) throw new IllegalArgumentException( "Null name provided." );
      if ( fullName.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid name provided." );
      
      String[] elements = fullName.split( "\\." );
      return elements[ elements.length - 1 ];
   }//End Method
   
   /**
    * Method to identify the location of the {@link TestClass} from the full name.
    * @param fullName the name including location, such as package.subpackage.ClassName
    * @return the {@link TestClass} location.
    */
   public static final String identifyLocation( String fullName ) {
      if ( fullName == null ) throw new IllegalArgumentException( "Null name provided." );
      if ( fullName.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid name provided." );
      
      String[] elements = fullName.split( "\\." );
      String[] locationElements = new String[ elements.length - 1 ];
      System.arraycopy( elements, 0, locationElements, 0, locationElements.length );
      String location = String.join( ".", locationElements );
      return location;
   }//End Method
   
}//End Class
