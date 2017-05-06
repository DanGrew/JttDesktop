/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import org.controlsfx.control.Notifications;

import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.jtt.desktop.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationTreeController;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * {@link BuildResultStatusNotification} provides a {@link Notification} for the change in
 * {@link BuildResultStatus}.
 */
public class BuildResultStatusNotification implements Notification {

   static final String STILL_THE_SAME = "Build has remained at %s";
   static final String MAY_REQUIRE_ACTION = "Build has only achieved %s when it was %s and may require action";
   static final String PASSING = "Build has achieved %s from %s";
   static final int NOTIFICATION_DELAY = 30000;
   
   private final ChangeIdentifier changeIdentifier;
   private final BuildResultStatusDesktopNotification desktopNotification;
   private final JenkinsJob job;
   private final BuildResultStatus previousStatus;
   private final BuildResultStatus newStatus;
   
   /**
    * Constructs a new {@link BuildResultStatusNotification}.
    * @param job the {@link JenkinsJob} changed.
    * @param previousStatus the previous {@link BuildResultStatus}.
    * @param newStatus the new {@link BuildResultStatus}.
    */
   public BuildResultStatusNotification( JenkinsJob job, BuildResultStatus previousStatus, BuildResultStatus newStatus ) {
      this( new ChangeIdentifier(), new BuildResultStatusDesktopNotification(), job, previousStatus, newStatus );
   }//End Constructor
   
   /**
    * Constructs a new {@link BuildResultStatusNotification}.
    * @param changeIdentifier the {@link ChangeIdentifier} for providing a change description.
    * @param desktopNotification the {@link BuildResultStatusDesktopNotification} for notifications on the desktop.
    * @param job the {@link JenkinsJob} changed.
    * @param previousStatus the previous {@link BuildResultStatus}.
    * @param newStatus the new {@link BuildResultStatus}.
    */
   BuildResultStatusNotification( 
            ChangeIdentifier changeIdentifier, 
            BuildResultStatusDesktopNotification desktopNotification, 
            JenkinsJob job, 
            BuildResultStatus previousStatus, 
            BuildResultStatus newStatus 
   ) {
      this.job = job;
      this.previousStatus = previousStatus;
      this.newStatus = newStatus;
      this.changeIdentifier = changeIdentifier;
      this.desktopNotification = desktopNotification;
   }//End Constructor
   
   /**
    * Method to format the given change in {@link BuildResultStatus}.
    * @param previous the previous {@link BuildResultStatus}.
    * @param current the new {@link BuildResultStatus}.
    * @return the {@link String} description of the change.
    */
   String formatBuildResultStatusChange( BuildResultStatus previous, BuildResultStatus current ) {
      switch ( identifyChange() ) {
         case ActionRequired:
            return String.format( MAY_REQUIRE_ACTION, current.name(), previous.name() );
         case Passed:
            return String.format( PASSING, current.name(), previous.name() );
         case Unchanged:
            return String.format( STILL_THE_SAME, previous.name() );
         default:
            return "Unkown";
      }
   }//End Method 
   
   /**
    * Method to identify the {@link BuildResultStatusChange} for this {@link Notification}.
    * @return the {@link BuildResultStatusChange} identified.
    */
   public BuildResultStatusHighLevelChange identifyChange(){
      return changeIdentifier.identifyChangeType( getPreviousBuildResultStatus(), getNewBuildResultStatus() );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String getDescription() {
      return formatBuildResultStatusChange( 
               getPreviousBuildResultStatus(), getNewBuildResultStatus() 
      );
   }//End Method

   /**
    * Getter for the associated {@link JenkinsJob}.
    * @return the {@link JenkinsJob}.
    */
   public JenkinsJob getJenkinsJob() {
      return job;
   }//End Method

   /**
    * Getter for the previous {@link BuildResultStatus}.
    * @return the {@link BuildResultStatus}.
    */
   public BuildResultStatus getPreviousBuildResultStatus() {
      return previousStatus;
   }//End Method

   /**
    * Getter for the new {@link BuildResultStatus}.
    * @return the {@link BuildResultStatus}.
    */
   public BuildResultStatus getNewBuildResultStatus() {
      return newStatus;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public NotificationTreeItem constructTreeItem( NotificationTreeController controller ) {
      return new BuildResultStatusNotificationTreeItem( this, controller );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void showDesktopNotification() {
      desktopNotification.showNotification( this, Notifications.create(), NOTIFICATION_DELAY );
   }//End Method
}//End Class
