/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.panel.description.DefaultJobPanelDescriptionImpl;
import javafx.registrations.RegistrationImpl;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.jobs.JenkinsJob;

/**
 * The {@link JobPanelImpl} provides a single {@link Pane} for a individual {@link JenkinsJob}
 * that can be displayed on a build wall.
 */
public class JobPanelImpl extends StackPane {

   private final JobProgressImpl progress;
   private final DefaultJobPanelDescriptionImpl description;
   private final JenkinsJob job;
   
   /**
    * Constructs a new {@link JobPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob}.
    */
   public JobPanelImpl( BuildWallConfiguration configuration, JenkinsJob job ) {
      this.job = job;
      
      this.progress = new JobProgressImpl( job );
      getChildren().add( progress );
      this.description = new DefaultJobPanelDescriptionImpl( configuration, job );
      getChildren().add( description );
   }//End Method

   /**
    * Method to get the {@link JenkinsJob} being displayed.
    * @return the {@link JenkinsJob}.
    */
   public JenkinsJob getJenkinsJob() {
      return job;
   }//End Method
   
   /**
    * Method to detach this object, its {@link RegistrationImpl}s and its childrens
    * from the system.
    */
   public void detachFromSystem() {
      getChildren().clear();
      progress.detachFromSystem();
      description.detachFromSystem();
   }//End Method
   
   /**
    * Method to determine whether the {@link DefaultJobPanelDescriptionImpl} is detached and
    * not registered with anything in the system.
    * @return true if no registrations held.
    */
   public boolean isDetached() {
      return progress.isDetached() && description.isDetached();
   }//End Method

   JobProgressImpl progress() {
      return progress;
   }//End Method

   DefaultJobPanelDescriptionImpl description() {
      return description;
   }//End Method

}//End Class
