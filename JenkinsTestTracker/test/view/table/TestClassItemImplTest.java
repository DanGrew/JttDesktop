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

import model.tests.TestCase;
import model.tests.TestCaseImpl;
import model.tests.TestClass;
import model.tests.TestClassImpl;
import model.tests.TestResultStatus;
import utility.TestCommon;

/**
 * {@link TestClassItemImpl} test.
 */
public class TestClassItemImplTest {

   private TestClass testClass1;
   private TestTableItem testClass1Item;
   private TestClass testClass2;
   private TestTableItem testClass2Item;
   
   private TestCase pass;
   private TestCase fail;
   
   @Before public void initialiseSystemUnderTest(){
      testClass1 = new TestClassImpl( "somewhere.something.ClassName" );
      pass = new TestCaseImpl( "shouldBePassing", testClass1 );
      pass.statusProperty().set( TestResultStatus.PASSED );
      testClass1.testCasesList().add( pass );
      testClass1Item = new TestClassItemImpl( testClass1 );
      
      testClass2 = new TestClassImpl( "another.place.SomethingElse" );
      fail = new TestCaseImpl( "shouldBeFailing", testClass2 );
      fail.statusProperty().set( TestResultStatus.FAILED );
      testClass2.testCasesList().add( pass );
      testClass2.testCasesList().add( fail );
      testClass2Item = new TestClassItemImpl( testClass2 );
   }//End Method
   
   @Test public void shouldProvideDescription() {
      Assert.assertEquals( testClass1.getDescription(), testClass1Item.getColumnProperty( 0 ).get() );
      Assert.assertEquals( testClass2.getDescription(), testClass2Item.getColumnProperty( 0 ).get() );
   }//End Method
   
   @Test public void shouldUpdateDescription() {
      shouldProvideDescription();
      
      final String newName = "another name";
      testClass1.nameProperty().set( newName );
      Assert.assertEquals( newName, testClass1.nameProperty().get() );
      
      shouldProvideDescription();
      
      final String newLocation = "somewhere else";
      testClass1.locationProperty().set( newLocation );
      Assert.assertEquals( newLocation, testClass1.locationProperty().get() );
      
      shouldProvideDescription();
   }//End Method
   
   @Test public void shouldProvideStatus() {
      Assert.assertNull( testClass1Item.getColumnProperty( 1 ) );
      Assert.assertNull( testClass2Item.getColumnProperty( 1 ) );
   }//End Method
   
   @Test public void shouldProvideSkipped() {
      Assert.assertNull( testClass1Item.getColumnProperty( 2 ) );
      Assert.assertNull( testClass2Item.getColumnProperty( 2 ) );
   }//End Method
   
   @Test public void shouldProvideAge() {
      Assert.assertNull( testClass1Item.getColumnProperty( 3 ) );
      Assert.assertNull( testClass2Item.getColumnProperty( 3 ) );
   }//End Method
   
   @Test public void shouldProvideDuration() {
      Assert.assertEquals( "" + testClass1.durationProperty().get(), testClass1Item.getColumnProperty( 4 ).get() );
      Assert.assertEquals( "" + testClass2.durationProperty().get(), testClass2Item.getColumnProperty( 4 ).get() );
   }//End Method
   
   @Test public void shouldUpdateDuration() {
      shouldProvideDuration();
      
      final double newDuration = 738.2;
      testClass1.durationProperty().set( newDuration );
      Assert.assertEquals( newDuration, testClass1.durationProperty().get(), TestCommon.precision() );
      
      shouldProvideDuration();
   }//End Method
   
   @Test public void shouldProvideSubject(){
      Assert.assertEquals( testClass1, testClass1Item.getSubject() );
   }//End Method
}//End Class
