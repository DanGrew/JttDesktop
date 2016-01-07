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

import model.tests.TestClass;

/**
 * Basic implementation of the {@link JenkinsDatabase}.
 */
public class JenkinsDatabaseImpl implements JenkinsDatabase {

   private Map< TestClassKey, TestClass > testClasses;

   /**
    * Constructs a new {@link JenkinsDatabaseImpl}.
    */
   public JenkinsDatabaseImpl() {
      testClasses = new HashMap<>();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isEmpty() {
      return testClasses.isEmpty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean has( TestClassKey testClassKey ) {
      if ( testClassKey == null ) throw new IllegalArgumentException( "Null key provided." );
      
      return testClasses.containsKey( testClassKey );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void store( TestClass testClass ) {
      if ( testClass == null ) throw new IllegalArgumentException( "Null TestClass provided." );
      
      testClasses.put( 
         new TestClassKeyImpl( 
            testClass.nameProperty().get(), 
            testClass.locationProperty().get() 
         ), 
         testClass 
      );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public TestClass get( TestClassKey testClassKey ) {
      if ( testClassKey == null ) throw new IllegalArgumentException( "Null key provided." );
      
      return testClasses.get( testClassKey );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public TestClass remove( TestClassKey testClassKey ) {
      if ( testClassKey == null ) throw new IllegalArgumentException( "Null key provided." );
      
      return testClasses.remove( testClassKey );
   }//End Method

}//End Class
