/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package view.table;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import model.tests.TestCase;
import model.tests.TestCaseImpl;
import model.tests.TestClass;
import model.tests.TestResultStatus;
import utility.TestCommon;

/**
 * {@link TestCaseItemImpl} test.
 */
public class TestCaseItemImplTest {

   private TestCase testCase1;
   private TestTableItem testCase1Item;
   private TestCase testCase2;
   private TestTableItem testCase2Item;
   
   /**
    * Method to initialise the system under test.
    */
   @Before public void initialiseSystemUnderTest(){
      testCase1 = new TestCaseImpl( "first", Mockito.mock( TestClass.class ) );
      testCase1Item = new TestCaseItemImpl( testCase1 );
      testCase2 = new TestCaseImpl( "second", Mockito.mock( TestClass.class ) );
      testCase2Item = new TestCaseItemImpl( testCase2 );
   }//End Method
   
   @Test public void shouldProvideName() {
      Assert.assertEquals( testCase1.nameProperty().get(), testCase1Item.getColumnProperty( 0 ).get() );
      Assert.assertEquals( testCase2.nameProperty().get(), testCase2Item.getColumnProperty( 0 ).get() );
   }//End Method
   
   @Test public void shouldUpdateName() {
      shouldProvideName();
      
      final String newName = "a new name";
      testCase1.nameProperty().set( newName );
      Assert.assertEquals( newName, testCase1.nameProperty().get() );
      
      shouldProvideName();
   }//End Method
   
   @Test public void shouldProvideStatus() {
      Assert.assertEquals( testCase1.statusProperty().get().toString(), testCase1Item.getColumnProperty( 1 ).get() );
      Assert.assertEquals( testCase2.statusProperty().get().toString(), testCase2Item.getColumnProperty( 1 ).get() );
   }//End Method
   
   @Test public void shouldUpdateStatus() {
      shouldProvideStatus();
      
      final TestResultStatus newStatus = TestResultStatus.PASSED;
      testCase1.statusProperty().set( newStatus );
      Assert.assertEquals( newStatus, testCase1.statusProperty().get() );
      
      shouldProvideStatus();
   }//End Method
   
   @Test public void shouldProvideSkipped() {
      Assert.assertEquals( "" + testCase1.skippedProperty().get(), testCase1Item.getColumnProperty( 2 ).get() );
      Assert.assertEquals( "" + testCase2.skippedProperty().get(), testCase2Item.getColumnProperty( 2 ).get() );
   }//End Method
   
   @Test public void shouldUpdateSkipped() {
      shouldProvideSkipped();
      
      final boolean newSkipped = true;
      testCase1.skippedProperty().set( newSkipped );
      Assert.assertEquals( newSkipped, testCase1.skippedProperty().get() );
      
      shouldProvideSkipped();
   }//End Method
   
   @Test public void shouldProvideAge() {
      Assert.assertEquals( "" + testCase1.ageProperty().get(), testCase1Item.getColumnProperty( 3 ).get() );
      Assert.assertEquals( "" + testCase2.ageProperty().get(), testCase2Item.getColumnProperty( 3 ).get() );
   }//End Method
   
   @Test public void shouldUpdateAge() {
      shouldProvideAge();
      
      final int newAge = 998;
      testCase1.ageProperty().set( newAge );
      Assert.assertEquals( newAge, testCase1.ageProperty().get() );
      
      shouldProvideAge();
   }//End Method
   
   @Test public void shouldProvideDuration() {
      Assert.assertEquals( "" + testCase1.durationProperty().get(), testCase1Item.getColumnProperty( 4 ).get() );
      Assert.assertEquals( "" + testCase2.durationProperty().get(), testCase2Item.getColumnProperty( 4 ).get() );
   }//End Method
   
   @Test public void shouldUpdateDuration() {
      shouldProvideDuration();
      
      final double newDuration = 1000.23;
      testCase1.durationProperty().set( newDuration );
      Assert.assertEquals( newDuration, testCase1.durationProperty().get(), TestCommon.precision() );
      
      shouldProvideDuration();
   }//End Method
   
   @Test public void shouldProvideSubject(){
      Assert.assertEquals( testCase1, testCase1Item.getSubject() );
   }//End Method

}//End Class
