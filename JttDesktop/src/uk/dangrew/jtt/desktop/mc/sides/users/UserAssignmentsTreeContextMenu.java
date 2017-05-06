/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.desktop.javafx.contextmenu.ContextMenuWithCancel;

/**
 * The {@link UserAssignmentsTreeContextMenu} provides a {@link ContextMenu} that allows
 * the user to interact with {@link Notification}s on the {@link UserAssignmentsTree}.
 */
public class UserAssignmentsTreeContextMenu extends ContextMenuWithCancel {
   
   static final String COMPLETE_TEXT = "Complete";
   private final UserAssignmentsTree display;
   private MenuItem completeControl;
   
   /**
    * Constructs a new {@link UserAssignmentsTreeContextMenu}.
    * @param display the {@link UserAssignmentsTree} to control.
    */
   UserAssignmentsTreeContextMenu( UserAssignmentsTree display ) {
      this.display = display;
      
      super.initialise();
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override protected void applyControls() {
      completeControl = new MenuItem( COMPLETE_TEXT );
      completeControl.setOnAction( event -> completeSelection() );
      getItems().addAll( 
            completeControl
      );
   }//End Method
   
   /**
    * Method to complete the current selection of {@link UserAssignment}s.
    */
   private void completeSelection() {
      ObservableList< TreeItem< UserAssignmentsTreeItem > > selection = display.getSelectionModel().getSelectedItems();
      if ( selection.isEmpty() ) {
         return;
      }
      
      UserAssignmentsTreeController controller = display.getController();
      new ArrayList<>( selection ).forEach( item -> controller.remove( item.getValue().getAssignment() ) );
   }//End Method
   
   MenuItem completeMenu(){
      return completeControl;
   }//End Method
   
}//End Class
