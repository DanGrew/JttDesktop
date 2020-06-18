/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.view.tree;

import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.desktop.javafx.contextmenu.ContextMenuWithCancel;
import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignmentEvent;
import uk.dangrew.jtt.desktop.mc.sides.users.shared.AssignmentMenu;
import uk.dangrew.jtt.desktop.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.desktop.utility.time.DefaultInstantProvider;
import uk.dangrew.jtt.desktop.utility.time.InstantProvider;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.kode.event.structure.Event;

/**
 * The {@link NotificationTreeContextMenu} provides a {@link ContextMenu} that allows
 * the user to interact with {@link Notification}s on the {@link NotificationTree}.
 */
public class NotificationTreeContextMenu extends ContextMenuWithCancel {
   
   static final String ENTER_DETAIL = "<enter detail>";
   
   private final JenkinsDatabase database;
   private final InstantProvider instantProvider;
   private final NotificationTree display;
   private final UserAssignmentEvent events;
   private Menu assignControl;
   
   /**
    * Constructs a new {@link NotificationTreeContextMenu}.
    * @param display the {@link NotificationTree} to control.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsUser}s. 
    */
   NotificationTreeContextMenu( NotificationTree display, JenkinsDatabase database ) {
      this( display, database, new DefaultInstantProvider() );
   }//End Constructor
   
   /**
    * Constructs a new {@link NotificationTreeContextMenu}.
    * @param display the {@link NotificationTree} to control.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsUser}s. 
    */
   NotificationTreeContextMenu( NotificationTree display, JenkinsDatabase database, InstantProvider instantProvider ) {
      this.display = display;
      this.database = database;
      this.instantProvider = instantProvider;
      this.events = new UserAssignmentEvent();
      
      super.initialise();
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override protected void applyControls() {
      assignControl = new AssignmentMenu( database, this::notifyAssignment );
      getItems().addAll( 
            assignControl
      );
   }//End Method
   
   /**
    * Method to notify the assignments for the given {@link JenkinsUser} based on the current
    * selection in the {@link NotificationTree}.
    * @param user the {@link JenkinsUser} to assign to.
    */
   private void notifyAssignment( JenkinsUser user ) {
      ObservableList< TreeItem< NotificationTreeItem > > selection = display.getSelectionModel().getSelectedItems();
      if ( selection.isEmpty() ) {
         return;
      }
      
      for ( TreeItem< NotificationTreeItem > item : selection ) {
         if ( item.getValue() == null ) {
            continue;
         }
         Notification notification = item.getValue().getNotification();
         UserAssignment assignment = new UserAssignment( 
                  user, 
                  instantProvider.get(), 
                  notification.getDescription(), 
                  ENTER_DETAIL
         );
         events.fire( new Event<>( assignment ) );
      }
   }//End Method
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} given is used by this {@link NotificationTreeContextMenu}.
    * @param database the {@link JenkinsDatabase} in question.
    * @return true if associated with this.
    */
   public boolean isConnectedTo( JenkinsDatabase database ) {
      return this.database == database;
   }//End Method
   
   Menu assignMenu(){
      return assignControl;
   }//End Method
   
}//End Class
