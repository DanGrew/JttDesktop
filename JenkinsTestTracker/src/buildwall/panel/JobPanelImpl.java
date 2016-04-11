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
import buildwall.panel.description.JobPanelDescriptionBaseImpl;
import buildwall.panel.type.JobPanelDescriptionProvider;
import javafx.registrations.ChangeListenerRegistrationImpl;
import javafx.registrations.RegistrationManager;
import javafx.scene.layout.StackPane;
import model.jobs.JenkinsJob;

/**
 * The {@link JobPanelImpl} provides a single {@link Pane} for a individual {@link JenkinsJob}
 * that can be displayed on a build wall.
 */
public class JobPanelImpl extends StackPane {

   private final JobProgressImpl progress;
   private JobPanelDescriptionBaseImpl description;
   private final JenkinsJob job;
   private final BuildWallConfiguration configuration;
   private final RegistrationManager registrations;
   
   /**
    * Constructs a new {@link JobPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob}.
    */
   public JobPanelImpl( BuildWallConfiguration configuration, JenkinsJob job ) {
      this.configuration = configuration;
      this.job = job;
      this.registrations = new RegistrationManager();
      
      this.progress = new JobProgressImpl( job );
      getChildren().add( progress );

      updateJobPanelDescriptionType( configuration.jobPanelDescriptionProvider().get() );
      applyRegistrations();
   }//End Method
   
   /**
    * Method to apply the registrations for this {@link JobPanelImpl}.
    */
   private void applyRegistrations(){
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.jobPanelDescriptionProvider(), 
               ( source, old, updated ) -> updateJobPanelDescriptionType( updated )
      ) );
   }//End Method
   
   /**
    * Method to update the {@link JobPanelDescriptionBaseImpl} used.
    * @param provider the {@link JobPanelDescriptionProvider} providing the {@link JobPanelDescriptionBaseImpl}.
    */
   private void updateJobPanelDescriptionType( JobPanelDescriptionProvider provider ) {
      if ( description != null ) {
         getChildren().remove( description );
         description.detachFromSystem();
      }
      
      this.description = provider.constructJobDescriptionPanel( configuration, job );
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
      registrations.shutdown();
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

   JobPanelDescriptionBaseImpl description() {
      return description;
   }//End Method

}//End Class
