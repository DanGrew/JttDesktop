/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users.detail;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.mc.sides.users.AssignedTreeItem;
import uk.dangrew.jtt.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.mc.sides.users.UserAssignmentsTreeItem;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link AssignmentDetailController} test.
 */
public class AssignmentDetailControllerTest {

   private UserAssignmentsTreeItem itemA;
   private UserAssignmentsTreeItem itemB;
   
   private UserAssignment assignmentA;
   private UserAssignment assignmentB;
   
   private TreeTableView< UserAssignmentsTreeItem > tree;
   @Mock private AssignmentDetailArea area;
   private AssignmentDetailController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      assignmentA = new UserAssignment( new JenkinsUserImpl( "A" ), 0, "", "" );
      assignmentB = new UserAssignment( new JenkinsUserImpl( "B" ), 0, "", "" );
      
      itemA = new AssignedTreeItem( assignmentA );
      itemB = new AssignedTreeItem( assignmentB );
      tree = new TreeTableView<>();
      tree.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
      tree.setRoot( new TreeItem<>() );
      tree.getRoot().setExpanded( true );
      tree.getRoot().getChildren().add( new TreeItem<>( itemA ) );
      tree.getRoot().getChildren().add( new TreeItem<>( itemB ) );
      
      systemUnderTest = new AssignmentDetailController( tree, area );
   }//End Method
   
   @Test public void shouldSetAssignmentSelectedOnArea() {
      tree.getSelectionModel().select( 1 );
      verify( area ).setAssignment( assignmentA );
      
      tree.getSelectionModel().clearSelection();
      verify( area ).setAssignment( null );
      
      tree.getSelectionModel().select( 2 );
      verify( area ).setAssignment( assignmentB );
   }//End Method
   
   @Test public void shouldClearSelectionForMultipleSelection() {
      tree.getSelectionModel().selectIndices( 1, 2 );
      verify( area ).setAssignment( null );
   }//End Method
   
   @Test public void shouldClearSelectionForBranch() {
      tree.getSelectionModel().select( 0 );
      verify( area ).setAssignment( null );
   }//End Method
   
   @Test public void shouldClearSelectionWhenCleared() {
      tree.getSelectionModel().select( 1 );
      verify( area ).setAssignment( assignmentA );
      
      tree.getSelectionModel().clearSelection();
      verify( area ).setAssignment( null );
   }//End Method

}//End Class
