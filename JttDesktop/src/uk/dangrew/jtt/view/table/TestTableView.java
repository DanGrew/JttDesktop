/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.view.table;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ListChangeListener;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.model.tests.TestCase;
import uk.dangrew.jtt.model.tests.TestClass;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link TestTableView} provides a collapsable tree view of {@link TestClass}es 
 * and {@link TestCase}s.
 */
public class TestTableView extends BorderPane {

   static final int COMMON_COLUMN_WIDTH = 100;
   static final int DESCRIPTION_COLUMN_WIDTH = 300;
   static final int DESCRIPTION_COLUMN_INDEX = 0;
   static final int STATUS_COLUMN_INDEX = 1;
   static final int SKIPPED_COLUMN_INDEX = 2;
   static final int AGE_COLUMN_INDEX = 3;
   static final int DURATION_COLUMN_INDEX = 4;
   static final String DURATION_COLUMN_NAME = "Duration";
   static final String AGE_COLUMN_NAME = "Age";
   static final String SKIPPED_COLUMN_NAME = "Skipped";
   static final String STATUS_COLUMN_NAME = "Status";
   static final String DESCRIPTION_COLUMN_NAME = "Test Cases";
   
   private TreeItem< TestTableItem > root;
   private Map< TestClass, TreeItem< TestTableItem > > testClassTreeItems;
   private Map< TestCase, TreeItem< TestTableItem > > testCaseTreeItems;
   private Map< TestClass, ListChangeListener< TestCase > > testCaseSubscriptions;

   /**
    * Constructs a new {@link TestTableView}.
    * @param database the {@link JenkinsDatabase} providing the {@link TestClass}es.
    */
   public TestTableView( JenkinsDatabase database ) {
      testClassTreeItems = new HashMap<>();
      testCaseTreeItems = new HashMap<>();
      testCaseSubscriptions = new HashMap<>();
      constructLayout( database );
      
      database.testClasses().addListener( new FunctionListChangeListenerImpl< TestClass >( 
               object -> addTestClass( object ), object -> removeTestClass( object ) 
      ) );
   }// End Constructor
   
   /**
    * Method to add a {@link TestClass} to the {@link TreeTableView}.
    * @param added the {@link TestClass} to added. Cannot be already present.
    */
   void addTestClass( TestClass added ){
      if ( testClassTreeItems.containsKey( added ) ) 
         throw new IllegalStateException( added.getDescription() + " is in the table, it cannot be added." );
      createBranch( root, added );
   }//End Method
   
   /**
    * Method to remove a {@link TestClass} from the {@link TreeTableView}.
    * @param removed the {@link TestClass} to remove. Must be already present.
    */
   void removeTestClass( TestClass removed ) {
      if ( !testClassTreeItems.containsKey( removed ) ) 
         throw new IllegalStateException( removed.getDescription() + " is not in the table, it cannot be removed." ); 
      TreeItem< TestTableItem > item = testClassTreeItems.get( removed );
      root.getChildren().remove( item );
      testClassTreeItems.remove( removed );
      removeTestCasesSubscription( removed );
   }//End Method
   
   /**
    * Method to add a {@link TestCase} to the {@link TreeTableView}.
    * @param added the {@link TestCase} to add, must not be present and {@link TestClass} must be present.
    */
   void addTestCase( TestCase added ) {
      TestClass testClass = added.testClassProperty().get();
      TreeItem< TestTableItem > branch = testClassTreeItems.get( testClass );
      if ( branch == null ) 
           throw new IllegalStateException( testClass.getDescription() + " is not in the table, " 
         + added.nameProperty().get() + " cannot be added to it." ); 
      
      createTestCaseItem( branch, added );
   }//End Method
   
   /**
    * Method to remove a {@link TestCase} from the {@link TreeTableView}.
    * @param removed the {@link TestCase} to remove, must be present and {@link TestClass} must be present.
    */
   void removeTestCase( TestCase removed ) {
      TestClass testClass = removed.testClassProperty().get();
      TreeItem< TestTableItem > branch = testClassTreeItems.get( testClass );
      if ( branch == null ) 
         throw new IllegalStateException( testClass.getDescription() + " is not in the table, " 
       + removed.nameProperty().get() + " cannot be removed from it." );
      
      TreeItem< TestTableItem > testCaseItem = testCaseTreeItems.get( removed );
      branch.getChildren().remove( testCaseItem );
      testCaseTreeItems.remove( removed );
   }//End Method
   
   /**
    * Method to add a listener to the {@link TestClass}es {@link TestCase}s.
    * @param testClass the {@link TestClass} to register on.
    */
   private void addTestCasesSubscription( TestClass testClass ) {
      ListChangeListener< TestCase > listener = new FunctionListChangeListenerImpl<>( 
               testCase -> addTestCase( testCase ), testCase -> removeTestCase( testCase ) 
      );
      testClass.testCasesList().addListener( listener );
      testCaseSubscriptions.put( testClass, listener );
   }//End Method
   
   /**
    * Method to remove a listener from the {@link TestClass} {@link TestCase}s.
    * @param testClass the {@link TestClass} to unregister for.
    */
   private void removeTestCasesSubscription( TestClass testClass ) {
      ListChangeListener< TestCase > listener = testCaseSubscriptions.get( testClass );
      testClass.testCasesList().removeListener( listener );
      testCaseSubscriptions.remove( testClass );
   }//End Method
   
   /**
    * Method to create a branch in the tree for the given data.
    * @param parent the parent {@link TreeItem} in the tree.
    * @param testClass the {@link TestClass} to branch for.
    * @return the constructed {@link TreeItem} representing the branch.
    */
   private TreeItem< TestTableItem > createBranch( 
            TreeItem< TestTableItem > parent, 
            TestClass testClass 
   ) {
      TestTableItem branchItem = new TestClassItemImpl( testClass );
      TreeItem< TestTableItem > branch = new TreeItem<>( branchItem );
      branch.setExpanded( true );
      testClass.testCasesList().forEach( object -> {
         createTestCaseItem( branch, object );
      } );
      addTestCasesSubscription( testClass );
      
      DecoupledPlatformImpl.runLater( () -> {
         parent.getChildren().add( branch );
      } );
      testClassTreeItems.put( testClass, branch );
      return branch;
   }// End Method
   
   /**
    * Method to create a {@link TestCaseItemImpl} for the given {@link TestCase}.
    * @param branch the {@link TreeItem} branch to add to.
    * @param testCase the {@link TestCase} to add.
    */
   private void createTestCaseItem( TreeItem< TestTableItem > branch, TestCase testCase ){
      TestTableItem caseDescriber = new TestCaseItemImpl( testCase );
      TreeItem< TestTableItem > caseItem = new TreeItem<>( caseDescriber, caseDescriber.getStatusGraphic() );
      DecoupledPlatformImpl.runLater( () -> {
         branch.getChildren().add( caseItem );
      } );
      testCaseTreeItems.put( testCase, caseItem );
   }//End Method
   
   /**
    * Method to create a column in the table.
    * @param parent the {@link TreeTableView} parent to add the column to.
    * @param columnName the name of the column.
    * @param columnReference the index of the column in the table.
    * @param columnWidth the width of the column.
    */
   private void createDescriptionColumn( TreeTableView< TestTableItem > parent, String columnName, int columnReference, int columnWidth ) {
      TreeTableColumn< TestTableItem, String > descriptionColumn = new TreeTableColumn<>( columnName );
      descriptionColumn.setPrefWidth( columnWidth );
      descriptionColumn.setEditable( false );
      descriptionColumn.setCellValueFactory( object -> object.getValue().getValue().getColumnProperty( columnReference ) );
      parent.getColumns().add( descriptionColumn );
   }// End Method
   
   /**
    * Method to construct the table.
    * @param database the {@link JenkinsDatabase} providing the {@link TestClass}es.
    */
   public void constructLayout( JenkinsDatabase database ) {
      root = new TreeItem<>();
      root.setExpanded( true );
      
      database.testClasses().forEach( test -> {
         createBranch( root, test );
      } );

      TreeTableView< TestTableItem > treeTableView = new TreeTableView<>( root );
      treeTableView.setEditable( false );
      treeTableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

      createDescriptionColumn( treeTableView, DESCRIPTION_COLUMN_NAME, DESCRIPTION_COLUMN_INDEX, DESCRIPTION_COLUMN_WIDTH );
      createDescriptionColumn( treeTableView, STATUS_COLUMN_NAME, STATUS_COLUMN_INDEX, COMMON_COLUMN_WIDTH );
      createDescriptionColumn( treeTableView, SKIPPED_COLUMN_NAME, SKIPPED_COLUMN_INDEX, COMMON_COLUMN_WIDTH );
      createDescriptionColumn( treeTableView, AGE_COLUMN_NAME, AGE_COLUMN_INDEX, COMMON_COLUMN_WIDTH );
      createDescriptionColumn( treeTableView, DURATION_COLUMN_NAME, DURATION_COLUMN_INDEX, COMMON_COLUMN_WIDTH );
      
      treeTableView.setShowRoot( false );
      treeTableView.setTableMenuButtonVisible( true );
      setCenter( treeTableView );
   }// End Method

}// End Class
