/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

import uk.dangrew.jtt.mc.model.Notification;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * {@link BuildResultStatusNotification} provides a {@link Notification} for the change in
 * {@link BuildResultStatus}.
 */
public class BuildResultStatusNotification implements Notification {

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
      this.job = job;
      this.previousStatus = previousStatus;
      this.newStatus = newStatus;
   }//End Constructor

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
   @Override public NotificationTreeItem constructTreeItem() {
      return new BuildResultStatusNotificationTreeItem( this );
   }//End Method
}//End Class
