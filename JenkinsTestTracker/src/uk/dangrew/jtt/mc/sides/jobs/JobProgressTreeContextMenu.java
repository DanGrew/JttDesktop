/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.friendly.javafx.FriendlyContextMenu;
import uk.dangrew.jtt.javafx.contextmenu.ContextMenuWithCancel;
import uk.dangrew.jtt.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.mc.sides.users.UserAssignmentEvent;
import uk.dangrew.jtt.mc.sides.users.shared.AssignmentMenu;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.time.DefaultInstantProvider;
import uk.dangrew.jtt.utility.time.InstantProvider;

/**
 * The {@link JobProgressTreeContextMenu} provides a {@link ContextMenu} that allows
 * the user to interact with {@link Notification}s on the {@link JobProgressTree}.
 */
public class JobProgressTreeContextMenu extends ContextMenuWithCancel {
   
   static final String ENTER_DETAIL = "<enter detail>";
   
   private final JenkinsDatabase database;
   private final InstantProvider instantProvider;
   private final JobProgressTree display;
   private final UserAssignmentEvent events;
   private Menu assignControl;
   
   /**
    * Constructs a new {@link JobProgressTreeContextMenu}.
    * @param display the {@link JobProgressTree} to control.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsUser}s. 
    */
   JobProgressTreeContextMenu( JobProgressTree display, JenkinsDatabase database ) {
      this( display, database, new DefaultInstantProvider() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobProgressTreeContextMenu}.
    * @param display the {@link JobProgressTree} to control.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsUser}s. 
    */
   JobProgressTreeContextMenu( JobProgressTree display, JenkinsDatabase database, InstantProvider instantProvider ) {
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
    * selection in the {@link JobProgressTree}.
    * @param user the {@link JenkinsUser} to assign to.
    */
   private void notifyAssignment( JenkinsUser user ) {
      ObservableList< TreeItem< JobProgressTreeItem > > selection = display.getSelectionModel().getSelectedItems();
      if ( selection.isEmpty() ) {
         return;
      }
      
      for ( TreeItem< JobProgressTreeItem > item : selection ) {
         if ( item.getValue() == null || item.getValue().getJenkinsJob() == null ) {
            continue;
         }
         JenkinsJob job = item.getValue().getJenkinsJob();
         String description = constructDescriptionFor( job );
         UserAssignment assignment = new UserAssignment( 
                  user, 
                  instantProvider.get(), 
                  description, 
                  ENTER_DETAIL
         );
         events.fire( new Event<>( assignment ) );         
      }
   }//End Method
   
   /**
    * Method to determine whether the {@link JenkinsDatabase} given is used by this {@link JobProgressTreeContextMenu}.
    * @param database the {@link JenkinsDatabase} in question.
    * @return true if associated with this.
    */
   public boolean isConnectedTo( JenkinsDatabase database ) {
      return this.database == database;
   }//End Method
   
   /**
    * Method to construct a description of the assignment from the {@link JenkinsJob}.
    * @param job the {@link JenkinsJob} in question.
    * @return a {@link String} description of the assignment.
    */
   static String constructDescriptionFor( JenkinsJob job ) {
      return job.nameProperty().get() + " with state " + job.lastBuildProperty().get().getValue().name();
   }//End Method
   
   Menu assignMenu(){
      return assignControl;
   }//End Method
   
}//End Class
