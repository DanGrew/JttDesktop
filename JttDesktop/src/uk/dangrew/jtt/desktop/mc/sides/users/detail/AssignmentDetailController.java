/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users.detail;

import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignmentsTreeItem;

/**
 * The {@link AssignmentDetailController} is responsible for controlling the 
 * {@link AssignmentDetailArea} given changes in selection on the {@link uk.dangrew.jtt.mc.sides.users.UserAssignmentsTree}.
 */
public class AssignmentDetailController {

   private final TreeTableView< UserAssignmentsTreeItem > tree;
   private final AssignmentDetailArea area;
   
   /**
    * Constructs a new {@link AssignmentDetailController}.
    * @param tree the {@link TreeTableView} of {@link UserAssignmentsTreeItem}s 
    * to monitor selection on.
    * @param area the {@link AssignmentDetailArea} to control.
    */
   public AssignmentDetailController( TreeTableView< UserAssignmentsTreeItem > tree, AssignmentDetailArea area ) {
      this.tree = tree;
      this.area = area;
      
      tree.getSelectionModel().getSelectedItems().addListener( this::handleSelection );
   }// End Constructor

   /**
    * Method to handle the change in selection of the {@link uk.dangrew.jtt.javafx.tree.structure.Tree}.
    * @param change the {@link javafx.collections.ListChangeListener.Change} made.
    */
   private void handleSelection( Change< ? extends TreeItem< UserAssignmentsTreeItem > > change ) {
      ObservableList< TreeItem< UserAssignmentsTreeItem > > selection = tree.getSelectionModel().getSelectedItems();
      if ( selection.size() != 1 ) {
         area.setAssignment( null );
         return;
      }
      
      if ( selection.get( 0 ).getValue() == null ) {
         area.setAssignment( null );
         return;
      }
      area.setAssignment( selection.get( 0 ).getValue().getAssignment() );
   }// End Method

}//End Class
