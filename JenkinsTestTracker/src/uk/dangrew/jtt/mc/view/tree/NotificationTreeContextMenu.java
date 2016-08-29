/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import java.time.Instant;

import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.friendly.javafx.FriendlyContextMenu;
import uk.dangrew.jtt.mc.model.Notification;
import uk.dangrew.jtt.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.mc.sides.users.UserAssignmentEvent;
import uk.dangrew.jtt.mc.sides.users.shared.AssignmentMenu;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.time.DefaultInstantProvider;
import uk.dangrew.jtt.utility.time.InstantProvider;

/**
 * The {@link NotificationTreeContextMenu} provides a {@link ContextMenu} that allows
 * the user to interact with {@link Notification}s on the {@link NotificationTree}.
 */
public class NotificationTreeContextMenu extends FriendlyContextMenu {
   
   static final String ENTER_DETAIL = "<enter detail>";
   static final String CANCEL = "Cancel";
   
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
      
      applyControls();
      applyCancel();
      
      setAutoHide( true );
   }//End Constructor

   /**
    * Method to apply the functions available to the menu.
    */
   private void applyControls() {
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
    * Method to apply the cancel function to close the menu.
    */
   private void applyCancel() {
      MenuItem cancel = new MenuItem( CANCEL );
      cancel.setOnAction( event -> hide() );
      
      getItems().addAll( 
               new SeparatorMenuItem(),
               cancel
      );
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
