/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package storage.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.tests.TestClass;
import model.tests.TestClassImpl;

/**
 * {@link JenkinsDatabaseImpl} test.
 */
public class JenkinsDatabaseImplTest {

   private static final String NAME = "anything";
   private static final String LOCATION = "anywhere";
   private JenkinsDatabase systemUnderTest;
   private TestClass testClass;
   
   /**
    * Method to initialise the {@link JenkinsDatabase} system under test.
    */
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JenkinsDatabaseImpl();
      Assert.assertTrue( systemUnderTest.isEmpty() );
      
      testClass = new TestClassImpl( NAME, LOCATION );
      Assert.assertFalse( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that a {@link TestClass} can be stored.
    */
   @Test public void shouldStoreTestClass() {
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that a {@link TestClass} can be retrieved.
    */
   @Test public void shouldRetrieveTestClass() {
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
      Assert.assertEquals( testClass, systemUnderTest.get( new TestClassKeyImpl( NAME, LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that only matching keys are retrieved.
    */
   @Test public void shouldRetrieveOnlyMatchingTestClass() {
      systemUnderTest.store( new TestClassImpl( "something", "elsewhere" ) );
      Assert.assertFalse( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
      Assert.assertNull( systemUnderTest.get( new TestClassKeyImpl( NAME, LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that storing {@link TestClass}es which match the same key overwrite.
    */
   @Test public void shouldOverwriteDuplicateTestClass() {
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
      TestClass alternate = new TestClassImpl( NAME, LOCATION );
      systemUnderTest.store( alternate );
      Assert.assertEquals( alternate, systemUnderTest.get( new TestClassKeyImpl( NAME, LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that store does not accept null.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldNotStoreNull(){
      systemUnderTest.store( null );
   }//End Method
   
   /**
    * Prove that has does not accept null.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldNotHasNull(){
      systemUnderTest.has( null );
   }//End Method

   /**
    * Prove that get does not accept null.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldNotGetNull(){
      systemUnderTest.get( null );
   }//End Method
   
   /**
    * Prove that a {@link TestClass} can be removed.
    */
   @Test public void shouldRemoveTestClass(){
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
      Assert.assertEquals( testClass, systemUnderTest.remove( new TestClassKeyImpl( NAME, LOCATION ) ) );
      Assert.assertFalse( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that nothing happens when there is nothing to remove.
    */
   @Test public void shouldNotRemoveTestClassIfNonePresent(){
      Assert.assertFalse( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
      Assert.assertNull( systemUnderTest.remove( new TestClassKeyImpl( NAME, LOCATION ) ) );
      Assert.assertFalse( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that nothing is removed when no mathcing {@link TestClass} is found.
    */
   @Test public void shouldNotRemoveTestClassIfNotPresent(){
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
      Assert.assertNull( systemUnderTest.remove( new TestClassKeyImpl( "something", "else" ) ) );
      Assert.assertTrue( systemUnderTest.has( new TestClassKeyImpl( NAME, LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that a null {@link TestClassKey} cannot be removed.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldNotRemoveNull(){
      systemUnderTest.remove( null );
   }//End Method
}//End Class
