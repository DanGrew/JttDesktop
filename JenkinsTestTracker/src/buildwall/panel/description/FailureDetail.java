/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel.description;

import buildwall.configuration.BuildWallConfiguration;
import graphics.DecoupledPlatformImpl;
import javafx.registrations.ChangeListenerRegistrationImpl;
import javafx.registrations.ListChangeListenerRegistrationImpl;
import javafx.registrations.RegisteredComponent;
import javafx.registrations.RegistrationManager;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import model.jobs.JenkinsJob;
import utility.observable.FunctionListChangeListenerImpl;

/**
 * {@link FailureDetail} provides a sub component for showing more detail about
 * the failure of a build.
 */
public class FailureDetail extends GridPane implements RegisteredComponent {
   
   static final String CULPRITS_PREFIX = "Culprits: ";
   private final BuildWallConfiguration configuration;
   private final JenkinsJob jenkinsJob;
   
   private RegistrationManager registrations;
   private Label culpritsLabel;
   
   /**
    * Constructs a new {@link FailureDetail}.
    * @param jenkinsJob the {@link JenkinsJob} associated.
    * @param configuration the {@link BuildWallConfiguration} for configuring the components.
    */
   public FailureDetail( JenkinsJob jenkinsJob, BuildWallConfiguration configuration ) {
      this.jenkinsJob = jenkinsJob;
      this.configuration = configuration;
      
      provideCulprits();
      applyRegistrations();
      applyRowConstraints();
   }//End Constructor
   
   /**
    * Method to provide the culprits on the component as a {@link Label}.
    */
   private void provideCulprits(){  
      culpritsLabel = new Label();
      updateCulpritText();
      updateCulpritFont();
      updateCulpritColour();
      culpritsLabel.setWrapText( true );
      add( culpritsLabel, 0, 0 );
   }//End Method

   /**
    * Method to construct the list of culprits as a {@link StringBuilder}.
    * @return the {@link StringBuilder} for further changes if needed.
    */
   private StringBuilder constructCulpritsList() {
      StringBuilder culprits = new StringBuilder();
      jenkinsJob.culprits().forEach( culprit -> {
         culprits.append( culprit.nameProperty().get() );
         culprits.append( ", " );
      } );
      if ( culprits.length() > 0 ) {
         culprits.setLength( culprits.length() - 1 );
         culprits.setCharAt( culprits.length() - 1, '.' );
      }
      return culprits;
   }//End Method
   
   /**
    * Method to apply the {@link RowConstraints} needed.
    */
   private void applyRowConstraints() {
      RowConstraints culpritsRow = new RowConstraints();
      culpritsRow.setPercentHeight( 100 );
      culpritsRow.setMaxHeight( Double.MAX_VALUE );
      getRowConstraints().add( culpritsRow );
   }//End Method
   
   /**
    * Method to apply the {@link RegistrationImpl}s needed to keep the component up to date.
    */
   private void applyRegistrations(){
      registrations = new RegistrationManager();
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.culpritsFont(), 
               ( source, old, updated ) -> updateCulpritFont() 
      ) );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.culpritsColour(), 
               ( source, old, updated ) -> updateCulpritColour() 
      ) );
      
      registrations.apply( new ListChangeListenerRegistrationImpl<>(
               jenkinsJob.culprits(), 
               new FunctionListChangeListenerImpl<>( 
                        added -> updateCulpritText(), removed -> updateCulpritText()
               )
      ) );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void detachFromSystem(){
      registrations.shutdown();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isDetached() {
      return registrations.isEmpty();
   }//End Method
   
   /**
    * Method to update the culprits {@link Font} in line with the {@link BuildWallConfiguration}.
    */
   private void updateCulpritFont(){
      culpritsLabel.fontProperty().set( configuration.culpritsFont().get() );
   }//End Method
   
   /**
    * Method to update the culprits {@link Color} in line with the {@link BuildWallConfiguration}.
    */
   private void updateCulpritColour(){
      culpritsLabel.textFillProperty().set( configuration.culpritsColour().get() );
   }//End Method
   
   /**
    * Method to update the culprit text displayed in the {@link Label}.
    */
   private void updateCulpritText(){
      StringBuilder culprits = constructCulpritsList();
      DecoupledPlatformImpl.runLater( () -> {
         culpritsLabel.setText( CULPRITS_PREFIX + culprits.toString() );
      } );
   }//End Method
   
   Label culpritsLabel() {
      return culpritsLabel;
   }//End Method
   
}//End Class
