/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package storage.database;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.tests.TestClass;

/**
 * Basic implementation of the {@link JenkinsDatabase}.
 */
public class JenkinsDatabaseImpl implements JenkinsDatabase {

   private Map< TestClassKey, TestClass > testClassesMap;
   private ObservableList< TestClass > testClasses;

   /**
    * Constructs a new {@link JenkinsDatabaseImpl}.
    */
   public JenkinsDatabaseImpl() {
      testClassesMap = new HashMap<>();
      testClasses = FXCollections.observableArrayList();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isEmpty() {
      return testClassesMap.isEmpty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean has( TestClassKey testClassKey ) {
      if ( testClassKey == null ) throw new IllegalArgumentException( "Null key provided." );
      
      return testClassesMap.containsKey( testClassKey );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void store( TestClass testClass ) {
      if ( testClass == null ) throw new IllegalArgumentException( "Null TestClass provided." );
 
      TestClassKey representativeKey = new TestClassKeyImpl( 
            testClass.nameProperty().get(), 
            testClass.locationProperty().get() 
      );
      if ( testClassesMap.containsKey( representativeKey ) ) {
         TestClass currentTestClass = testClassesMap.remove( representativeKey );
         testClasses.remove( currentTestClass );
      }
      
      testClassesMap.put( representativeKey, testClass );
      testClasses.add( testClass );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public TestClass get( TestClassKey testClassKey ) {
      if ( testClassKey == null ) throw new IllegalArgumentException( "Null key provided." );
      
      return testClassesMap.get( testClassKey );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public TestClass remove( TestClassKey testClassKey ) {
      if ( testClassKey == null ) throw new IllegalArgumentException( "Null key provided." );
      
      TestClass testClass = testClassesMap.remove( testClassKey );
      testClasses.remove( testClass );
      return testClass;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< TestClass > testClasses() {
      return testClasses;
   }//End Method

}//End Class
