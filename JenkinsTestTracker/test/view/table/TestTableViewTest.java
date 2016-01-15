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
import org.junit.Ignore;
import org.junit.Test;

import data.JsonTestResultsImporter;
import data.json.tests.JsonTestResultsImporterImpl;
import graphics.JavaFxInitializer;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import model.tests.TestCase;
import model.tests.TestCaseImpl;
import model.tests.TestClass;
import model.tests.TestClassImpl;
import model.tests.TestResultStatus;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import utility.TestCommon;

/**
 * {@link TestTableView} test.
 */
public class TestTableViewTest {

   private JenkinsDatabase database;
   private TestTableView systemUnderTest;
   private TreeTableView< TestTableItem > treeTableView;
   
   @Before public void initialiseSystemUnderTest(){
      String input = TestCommon.readFileIntoString( getClass(), "multiple-test-case-multiple-test-class.json" );
      database = new JenkinsDatabaseImpl();
      JsonTestResultsImporter importer = new JsonTestResultsImporterImpl( database );
      importer.updateTestResults( input );
      JavaFxInitializer.startPlatform();
      systemUnderTest = new TestTableView( database );
      
      @SuppressWarnings("unchecked") //By design, fail quickly with assertions below. 
      TreeTableView< TestTableItem > temp = ( TreeTableView< TestTableItem > )systemUnderTest.getCenter();
      treeTableView = temp;
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.threadedLaunch( () -> { return new TestTableView( database ); } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldHaveCorrectColumnCount(){
      Assert.assertEquals( 5, treeTableView.getColumns().size() );
   }//End Method
   
   @Test public void shouldHaveDescriptionColumn(){
      assertColumnsConfigured( 
               TestTableView.DESCRIPTION_COLUMN_INDEX, 
               TestTableView.DESCRIPTION_COLUMN_NAME, 
               TestTableView.DESCRIPTION_COLUMN_WIDTH 
      );
   }//End Method
   
   @Test public void shouldHaveStatusColumn(){
      assertColumnsConfigured( 
               TestTableView.STATUS_COLUMN_INDEX, 
               TestTableView.STATUS_COLUMN_NAME, 
               TestTableView.COMMON_COLUMN_WIDTH 
      );
   }//End Method
   
   @Test public void shouldHaveSkippedColumn(){
      assertColumnsConfigured( 
               TestTableView.SKIPPED_COLUMN_INDEX, 
               TestTableView.SKIPPED_COLUMN_NAME, 
               TestTableView.COMMON_COLUMN_WIDTH 
      );
   }//End Method
   
   @Test public void shouldHaveAgeColumn(){
      assertColumnsConfigured( 
               TestTableView.AGE_COLUMN_INDEX, 
               TestTableView.AGE_COLUMN_NAME, 
               TestTableView.COMMON_COLUMN_WIDTH 
      );
   }//End Method
   
   @Test public void shouldHaveDurationColumn(){
      assertColumnsConfigured( 
               TestTableView.DURATION_COLUMN_INDEX, 
               TestTableView.DURATION_COLUMN_NAME, 
               TestTableView.COMMON_COLUMN_WIDTH 
      );
   }//End Method
   
   /**
    * Method to assert that the column is configured correctly.
    * @param index the index of the column in the table.
    * @param name the name of the column.
    * @param width the width of the column.
    */
   private void assertColumnsConfigured( int index, String name, double width ) {
      @SuppressWarnings("unchecked") //By design, fail fast. 
      TreeTableColumn< TestTableItem, String > column = ( TreeTableColumn< TestTableItem, String > )treeTableView.getColumns().get( index );
      Assert.assertEquals( name, column.textProperty().get() );
      Assert.assertEquals( width, column.widthProperty().get(), TestCommon.precision() );
      Assert.assertFalse( column.isEditable() );
   }//End Method
   
   @Test public void tableShouldNotBeEditable(){
      Assert.assertFalse( treeTableView.isEditable() );
   }//End Method
   
   @Test public void tableMenuButtonShouldBeVisible(){
      Assert.assertTrue( treeTableView.isTableMenuButtonVisible() );
   }//End Method
   
   @Test public void tableShouldNotShowRoot(){
      Assert.assertFalse( treeTableView.isShowRoot() );
   }//End Method
   
   @Test public void tableShouldHaveExpandedRoot(){
      Assert.assertTrue( treeTableView.getRoot().isExpanded() );
   }//End Method
   
   @Test public void tableShouldSupportMultipleSelection(){
      Assert.assertEquals( SelectionMode.MULTIPLE, treeTableView.getSelectionModel().getSelectionMode() );
   }//End Method
   
   @Test public void shouldHaveBranchPerTestClass(){
      Assert.assertEquals( database.testClasses().size(), treeTableView.getRoot().getChildren().size() );
      for ( int i = 0; i < database.testClasses().size(); i++ ) {
         TestClass testClass = database.testClasses().get( i );
         TreeItem< TestTableItem > testItem = treeTableView.getRoot().getChildren().get( i );
         Assert.assertEquals( testClass, testItem.getValue().getSubject() );
      }
   }//End Method
   
   @Test public void shouldHaveExpandedBranches(){
      for ( int i = 0; i < database.testClasses().size(); i++ ) {
         TreeItem< TestTableItem > testItem = treeTableView.getRoot().getChildren().get( i );
         Assert.assertTrue( testItem.isExpanded() );
      }
   }//End Method
   
   @Test public void shouldHaveItemPerTestCase(){
      for ( int testClassIndex = 0; testClassIndex < database.testClasses().size(); testClassIndex++ ) {
         TestClass testClass = database.testClasses().get( testClassIndex );
         TreeItem< TestTableItem > testClassItem = treeTableView.getRoot().getChildren().get( testClassIndex );
         
         Assert.assertEquals( testClass.testCasesList().size(), testClassItem.getChildren().size() );
         for ( int testCaseIndex = 0; testCaseIndex < testClass.testCasesList().size(); testCaseIndex++ ) {
            TestCase testCase = testClass.testCasesList().get( testCaseIndex );
            TreeItem< TestTableItem > testCaseItem = testClassItem.getChildren().get( testCaseIndex );
            Assert.assertEquals( testCase, testCaseItem.getValue().getSubject() );
         }
      }
   }//End Method
   
   @Test public void classNameShouldUpdateDescriptionCell(){
      TestClass testClass = database.testClasses().get( 0 );
      TreeItem< TestTableItem > testClassItem = treeTableView.getRoot().getChildren().get( 0 );
      
      Assert.assertEquals( testClass.getDescription(), testClassItem.getValue().getColumnProperty( 0 ).get() );
      final String newValue = "anything";
      testClass.nameProperty().set( newValue );
      Assert.assertEquals( newValue, testClass.nameProperty().get() );
      Assert.assertEquals( testClass.getDescription(), testClassItem.getValue().getColumnProperty( 0 ).get() );
   }//End Method
   
   @Test public void classLocationShouldUpdateDescriptionCell(){
      TestClass testClass = database.testClasses().get( 0 );
      TreeItem< TestTableItem > testClassItem = treeTableView.getRoot().getChildren().get( 0 );
      
      Assert.assertEquals( testClass.getDescription(), testClassItem.getValue().getColumnProperty( 0 ).get() );
      final String newValue = "anything";
      testClass.locationProperty().set( newValue );
      Assert.assertEquals( newValue, testClass.locationProperty().get() );
      Assert.assertEquals( testClass.getDescription(), testClassItem.getValue().getColumnProperty( 0 ).get() );
   }//End Method

   @Test public void classStatusShouldBeNull(){
      TreeItem< TestTableItem > testClassItem = treeTableView.getRoot().getChildren().get( 0 );
      Assert.assertNull( testClassItem.getValue().getColumnProperty( 1 ) );
   }//End Method
   
   @Test public void classSkippedShouldBeNull(){
      TreeItem< TestTableItem > testClassItem = treeTableView.getRoot().getChildren().get( 0 );
      Assert.assertNull( testClassItem.getValue().getColumnProperty( 2 ) );
   }//End Method
   
   @Test public void classAgeShouldBeNull(){
      TreeItem< TestTableItem > testClassItem = treeTableView.getRoot().getChildren().get( 0 );
      Assert.assertNull( testClassItem.getValue().getColumnProperty( 3 ) );
   }//End Method
   
   @Test public void classDurationShouldUpdateCell(){
      TestClass testClass = database.testClasses().get( 0 );
      TreeItem< TestTableItem > testClassItem = treeTableView.getRoot().getChildren().get( 0 );
      
      Assert.assertEquals( testClass.getDescription(), testClassItem.getValue().getColumnProperty( 0 ).get() );
      final double newValue = 239487.23;
      testClass.durationProperty().set( newValue );
      Assert.assertEquals( newValue, testClass.durationProperty().get(), TestCommon.precision() );
      Assert.assertEquals( "" + testClass.durationProperty().get(), testClassItem.getValue().getColumnProperty( 4 ).get() );
   }//End Method
   
   @Test public void caseNameShouldUpdateCell(){
      TestCase testCase = database.testClasses().get( 0 ).testCasesList().get( 0 );
      TreeItem< TestTableItem > testCaseItem = treeTableView.getRoot().getChildren().get( 0 ).getChildren().get( 0 );
      
      Assert.assertEquals( testCase.nameProperty().get(), testCaseItem.getValue().getColumnProperty( 0 ).get() );
      final String newValue = "newName";
      testCase.nameProperty().set( newValue );
      Assert.assertEquals( newValue, testCase.nameProperty().get() );
      Assert.assertEquals( testCase.nameProperty().get(), testCaseItem.getValue().getColumnProperty( 0 ).get() );
   }//End Method
   
   @Test public void caseStatusShouldUpdateCell(){
      TestCase testCase = database.testClasses().get( 0 ).testCasesList().get( 0 );
      TreeItem< TestTableItem > testCaseItem = treeTableView.getRoot().getChildren().get( 0 ).getChildren().get( 0 );
      
      Assert.assertEquals( testCase.statusProperty().get().toString(), testCaseItem.getValue().getColumnProperty( 1 ).get() );
      final TestResultStatus newValue = TestResultStatus.FAILED;
      testCase.statusProperty().set( newValue );
      Assert.assertEquals( newValue, testCase.statusProperty().get() );
      Assert.assertEquals( testCase.statusProperty().get().toString(), testCaseItem.getValue().getColumnProperty( 1 ).get() );
   }//End Method
   
   @Test public void caseSkippedShouldUpdateCell(){
      TestCase testCase = database.testClasses().get( 0 ).testCasesList().get( 0 );
      TreeItem< TestTableItem > testCaseItem = treeTableView.getRoot().getChildren().get( 0 ).getChildren().get( 0 );
      
      Assert.assertEquals( "" + testCase.skippedProperty().get(), testCaseItem.getValue().getColumnProperty( 2 ).get() );
      final boolean newValue = true;
      testCase.skippedProperty().set( newValue );
      Assert.assertEquals( newValue, testCase.skippedProperty().get() );
      Assert.assertEquals( "" + testCase.skippedProperty().get(), testCaseItem.getValue().getColumnProperty( 2 ).get() );
   }//End Method
   
   @Test public void caseAgeShouldUpdateCell(){
      TestCase testCase = database.testClasses().get( 0 ).testCasesList().get( 0 );
      TreeItem< TestTableItem > testCaseItem = treeTableView.getRoot().getChildren().get( 0 ).getChildren().get( 0 );
      
      Assert.assertEquals( "" + testCase.ageProperty().get(), testCaseItem.getValue().getColumnProperty( 3 ).get() );
      final int newValue = 34;
      testCase.ageProperty().set( newValue );
      Assert.assertEquals( newValue, testCase.ageProperty().get() );
      Assert.assertEquals( "" + testCase.ageProperty().get(), testCaseItem.getValue().getColumnProperty( 3 ).get() );
   }//End Method

   @Test public void caseDurationShouldUpdateCell(){
      TestCase testCase = database.testClasses().get( 0 ).testCasesList().get( 0 );
      TreeItem< TestTableItem > testCaseItem = treeTableView.getRoot().getChildren().get( 0 ).getChildren().get( 0 );
      
      Assert.assertEquals( "" + testCase.durationProperty().get(), testCaseItem.getValue().getColumnProperty( 4 ).get() );
      final double newValue = 382.23;
      testCase.durationProperty().set( newValue );
      Assert.assertEquals( newValue, testCase.durationProperty().get(), TestCommon.precision() );
      Assert.assertEquals( "" + testCase.durationProperty().get(), testCaseItem.getValue().getColumnProperty( 4 ).get() );
   }//End Method
   
   @Test public void testCasesShouldDisplayStatusGraphic(){
      TreeItem< TestTableItem > testCaseItem = treeTableView.getRoot().getChildren().get( 0 ).getChildren().get( 0 );
      Assert.assertEquals( testCaseItem.getValue().getStatusGraphic(), testCaseItem.getGraphic() );
   }//End Method
   
   @Test public void shouldRemoveTestClassesWhenDatabaseChanges(){
      TestClass first = database.testClasses().get( 0 );
      TreeItem< TestTableItem > firstItem = treeTableView.getRoot().getChildren().get( 0 );
      Assert.assertEquals( first, firstItem.getValue().getSubject() );
      
      database.testClasses().remove( first );
      Assert.assertFalse( database.testClasses().contains( first ) );
      
      TreeItem< TestTableItem > firstItemFollowingChange = treeTableView.getRoot().getChildren().get( 0 );
      Assert.assertNotEquals( first, firstItemFollowingChange.getValue().getSubject() );
   }//End Method
   
   @Test public void shouldAddTestClassesWhenDatabaseChanges(){
      ObservableList< TreeItem< TestTableItem > > rootChildren = treeTableView.getRoot().getChildren();
      
      TestClass newClass = new TestClassImpl( "newClass" );
      Assert.assertFalse( database.testClasses().contains( newClass ) );
      database.testClasses().add( newClass );
      Assert.assertTrue( database.testClasses().contains( newClass ) );
      
      TreeItem< TestTableItem > lastItemFollowingChange = rootChildren.get( rootChildren.size() - 1 );
      Assert.assertEquals( newClass, lastItemFollowingChange.getValue().getSubject() );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAddTestClassIfAlreadyPresent(){
      systemUnderTest.addTestClass( database.testClasses().get( 0 ) );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotRemoveTestClassIfNotPresent(){
      systemUnderTest.removeTestClass( new TestClassImpl( "anything" ) );
   }//End Method
   
   @Test public void shouldRemoveTestCasesWhenDatabaseChanges(){
      TestClass testClass = database.testClasses().get( 0 );
      TestCase first = testClass.testCasesList().get( 0 );
      TreeItem< TestTableItem > testCaseItem = treeTableView.getRoot().getChildren().get( 0 );
      TreeItem< TestTableItem > firstItem = testCaseItem.getChildren().get( 0 );
      Assert.assertEquals( first, firstItem.getValue().getSubject() );
      
      testClass.testCasesList().remove( first );
      Assert.assertFalse( testClass.testCasesList().contains( first ) );
      
      TreeItem< TestTableItem > firstItemFollowingChange = testCaseItem.getChildren().get( 0 );
      Assert.assertNotEquals( first, firstItemFollowingChange.getValue().getSubject() );
   }//End Method
   
   @Test public void shouldAddTestCasesWhenDatabaseChanges(){
      TestClass testClass = database.testClasses().get( 0 );
      TestCase newCase = new TestCaseImpl( "newCase", testClass );
      
      TreeItem< TestTableItem > testCaseItem = treeTableView.getRoot().getChildren().get( 0 );
      
      testClass.testCasesList().add( newCase );
      Assert.assertTrue( testClass.testCasesList().contains( newCase ) );
      
      TreeItem< TestTableItem > firstItemFollowingChange = testCaseItem.getChildren().get( testCaseItem.getChildren().size() - 1 );
      Assert.assertEquals( newCase, firstItemFollowingChange.getValue().getSubject() );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAddTestCaseItemIfTestClassNotPresent(){
      TestClass unassociatedClass = new TestClassImpl( "anyClass" );
      TestCase newCase = new TestCaseImpl( "anything", unassociatedClass );
      systemUnderTest.addTestCase( newCase );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotRemoveTestCaseItemIfTestClassNotPresent(){
      TestClass unassociatedClass = new TestClassImpl( "anyClass" );
      TestCase newCase = new TestCaseImpl( "anything", unassociatedClass );
      systemUnderTest.removeTestCase( newCase );
   }//End Method

}//End Class
