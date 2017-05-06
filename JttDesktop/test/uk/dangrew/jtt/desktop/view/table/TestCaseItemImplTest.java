/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.view.table;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import uk.dangrew.jtt.desktop.view.table.TestCaseItemImpl;
import uk.dangrew.jtt.desktop.view.table.TestTableItem;
import uk.dangrew.jtt.model.tests.TestCase;
import uk.dangrew.jtt.model.tests.TestCaseImpl;
import uk.dangrew.jtt.model.tests.TestClass;
import uk.dangrew.jtt.model.tests.TestResultStatus;
import uk.dangrew.jtt.model.utility.TestCommon;

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
      testCase1.statusProperty().set( TestResultStatus.PASSED );
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
   
   @Test public void shouldProvideCommonJenkinsStyleStatusGraphic(){
      Node node = testCase1Item.getStatusGraphic();
      Assert.assertTrue( node instanceof Circle );
      Circle circle = ( Circle )node;
      Assert.assertEquals( TestCaseItemImpl.DEFAULT_STATUS_GRAPHIC_RADIUS, circle.getRadius(), TestCommon.precision() );
      Assert.assertEquals( 0, circle.getCenterX(), TestCommon.precision() );
      Assert.assertEquals( 0, circle.getCenterY(), TestCommon.precision() );
   }//End Method

   @Test public void shouldNotRecreateStatusGraphic(){
      Node firstCall = testCase1Item.getStatusGraphic();
      Node secondCall = testCase1Item.getStatusGraphic();
      Assert.assertEquals( firstCall, secondCall );
   }//End Method
   
   @Test public void statusGraphicShouldReflectPassStatus(){
      assertStatusGraphicColour( testCase1Item, TestResultStatus.PASSED.colour() );
   }//End Method
   
   @Test public void statusGraphicShouldReflectFailStatus(){
      assertStatusGraphicColour( testCase2Item, TestResultStatus.FAILED.colour() );
   }//End Method
   
   @Test public void statusGraphicShouldUpdateWithStatusChange(){
      assertStatusGraphicColour( testCase1Item, TestResultStatus.PASSED.colour() );
      testCase1.statusProperty().set( TestResultStatus.FIXED );
      assertStatusGraphicColour( testCase1Item, TestResultStatus.FIXED.colour() );
      testCase1.statusProperty().set( TestResultStatus.SKIPPED );
      assertStatusGraphicColour( testCase1Item, TestResultStatus.SKIPPED.colour() );
      testCase1.statusProperty().set( TestResultStatus.REGRESSION );
      assertStatusGraphicColour( testCase1Item, TestResultStatus.REGRESSION.colour() );
      testCase1.statusProperty().set( TestResultStatus.FAILED );
      assertStatusGraphicColour( testCase1Item, TestResultStatus.FAILED.colour() );
      testCase1.statusProperty().set( TestResultStatus.UNKNOWN );
      assertStatusGraphicColour( testCase1Item, TestResultStatus.UNKNOWN.colour() );
   }//End Method
   
   /**
    * Method to assert that the {@link Color} is as expected on the status graphic.
    * @param item the {@link TestTableItem} in question.
    * @param colour the expected {@link Color}.
    */
   private void assertStatusGraphicColour( TestTableItem item, Color colour ) {
      Node passNode = item.getStatusGraphic();
      Assert.assertTrue( passNode instanceof Circle );
      Circle passGraphic = ( Circle )passNode;
      Assert.assertEquals( colour, passGraphic.strokeProperty().get() );
      Assert.assertEquals( colour, passGraphic.fillProperty().get() );
   }//End Method
   
}//End Class
