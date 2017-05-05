/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel;

import javafx.scene.layout.StackPane;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.buildwall.panel.description.JobPanelDescriptionBaseImpl;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.javafx.registrations.ChangeListenerRegistrationImpl;
import uk.dangrew.jtt.javafx.registrations.RegistrationManager;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JobPanelImpl} provides a single {@link Pane} for a individual {@link JenkinsJob}
 * that can be displayed on a build wall.
 */
public class JobPanelImpl extends StackPane {

   private final JobProgressImpl progress;
   private JobPanelDescriptionBaseImpl description;
   private final JenkinsJob job;
   private final BuildWallTheme theme;
   private final BuildWallConfiguration configuration;
   private final RegistrationManager registrations;
   
   /**
    * Constructs a new {@link JobPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param theme the {@link BuildWallTheme} associated.
    * @param job the {@link JenkinsJob}.
    */
   public JobPanelImpl( BuildWallConfiguration configuration, BuildWallTheme theme, JenkinsJob job ) {
      this.configuration = configuration;
      this.theme = theme;
      this.job = job;
      this.registrations = new RegistrationManager();
      
      this.progress = new JobProgressImpl( job, theme );
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
    * @param provider the {@link JobPanelDescriptionProviders} providing the {@link JobPanelDescriptionBaseImpl}.
    */
   private void updateJobPanelDescriptionType( JobPanelDescriptionProviders provider ) {
      if ( description != null ) {
         getChildren().remove( description );
         description.detachFromSystem();
      }
      
      this.description = provider.constructJobDescriptionPanel( configuration, theme, job );
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
   
   /**
    * Method to determine whether the {@link JobPanelImpl} is associated with the give {@link BuildWallTheme}.
    * @param theme the {@link BuildWallTheme} in question.
    * @return true if associated with it.
    */
   public boolean isAssociatedWith( BuildWallTheme theme ) {
      return this.theme == theme;
   }//End Method

   JobProgressImpl progress() {
      return progress;
   }//End Method

   JobPanelDescriptionBaseImpl description() {
      return description;
   }//End Method

}//End Class
