/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package view.table;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import model.tests.TestCase;
import model.tests.TestClass;
import storage.database.JenkinsDatabase;

/**
 * The {@link TestTableViewTest} provides a collapsable tree view of {@link TestClass}es 
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

   /**
    * Constructs a new {@link TestTableViewTest}.
    * @param database the {@link JenkinsDatabase} providing the {@link TestClass}es.
    */
   public TestTableView( JenkinsDatabase database ) {
      constructLayout( database );
   }// End Constructor
   
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
         TestTableItem caseDescriber = new TestCaseItemImpl( object );
         branch.getChildren().add( new TreeItem<>( caseDescriber, caseDescriber.getStatusGraphic() ) );
      } );
      parent.getChildren().add( branch );
      return branch;
   }// End Method
   
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
      TreeItem< TestTableItem > root = new TreeItem<>();
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
