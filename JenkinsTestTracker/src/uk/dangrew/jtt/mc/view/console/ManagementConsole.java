/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.console;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.mc.notifiers.jobs.BuildResultStatusNotifier;
import uk.dangrew.jtt.mc.sides.jobs.JobProgressTree;
import uk.dangrew.jtt.mc.sides.users.UserAssignmentsTree;
import uk.dangrew.jtt.mc.view.tree.NotificationTree;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link ManagementConsole} provides an area of notifications and information for managing 
 * Jenkins and builds.
 */
public class ManagementConsole extends BorderPane {

   static final double PROGRESS_DIVIDER_POSITION = 0.35;
   static final double ASSIGNMENTS_DIVIDER_POSITION = 0.7;
   
   private final SplitPane jobsNotificationsSplit;
   private final SplitPane notificationsAssignmentsSplit;
   
   /**
    * Constructs a new {@link ManagementConsole}.
    * @param database the {@link JenkinsDatabase}.
    */
   public ManagementConsole( JenkinsDatabase database ) {
      NotificationTree notifications = new NotificationTree( database );
      
      //not tested - to be wrapped with others
      new BuildResultStatusNotifier( database );
      
      this.jobsNotificationsSplit = new SplitPane( new JobProgressTree( database ), notifications );
      this.jobsNotificationsSplit.setDividerPositions( PROGRESS_DIVIDER_POSITION );
      
      this.notificationsAssignmentsSplit = new SplitPane( jobsNotificationsSplit, new UserAssignmentsTree( database ) );
      this.notificationsAssignmentsSplit.setDividerPositions( ASSIGNMENTS_DIVIDER_POSITION );
      this.notificationsAssignmentsSplit.setOrientation( Orientation.VERTICAL );
      
      setCenter( notificationsAssignmentsSplit );
   }//End Constructor
   
   SplitPane jobsNotificationsSplit(){
      return jobsNotificationsSplit;
   }//End Method
   
   SplitPane notificationsAssignmentsSplit(){
      return notificationsAssignmentsSplit;
   }//End Method
   
}//End Class
