/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence;

import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.main.JenkinsTestTracker;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jupa.file.protocol.FileLocationProtocol;
import uk.dangrew.jupa.file.protocol.JarLocationProtocol;
import uk.dangrew.jupa.json.marshall.ModelMarshaller;
import uk.dangrew.jupa.json.session.SessionManager;

/**
 * The {@link BuildWallConfigurationSessions} provides the {@link BuildWallConfiguration}s for the
 * left and right {@link uk.dangrew.jtt.buildwall.layout.GridWallImpl}s along with their {@link SessionManager}s.
 */
public class BuildWallConfigurationSessions {

   static final String FOLDER_NAME = "uk.dangrew.jtt.configuration";
   static final String LEFT_BUILD_WALL_FILE_NAME = "left-build-wall.json";
   static final String RIGHT_BUILD_WALL_FILE_NAME = "right-build-wall.json";
   
   private final BuildWallConfiguration leftConfiguration;
   private final SessionManager leftSessions;
   private final FileLocationProtocol leftConfigurationFileLocation;
   private final BuildWallConfiguration rightConfiguration;
   private final SessionManager rightSessions;
   private final FileLocationProtocol rightConfigurationFileLocation;
   
   /**
    * Constructs a new {@link BuildWallConfigurationSessions}.
    * @param database the {@link JenkinsDatabase} for accessing {@link uk.dangrew.jtt.model.jobs.JenkinsJob} information.
    */
   public BuildWallConfigurationSessions( JenkinsDatabase database ) {
      this( 
               database, 
               new JarLocationProtocol( 
                        FOLDER_NAME, LEFT_BUILD_WALL_FILE_NAME, JenkinsTestTracker.class 
               ),
               new JarLocationProtocol( 
                        FOLDER_NAME, RIGHT_BUILD_WALL_FILE_NAME, JenkinsTestTracker.class 
               ) 
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link BuildWallConfigurationSessions}.
    * @param database the {@link JenkinsDatabase} for accessing {@link uk.dangrew.jtt.model.jobs.JenkinsJob} information.
    * @param leftProtocol the {@link FileLocationProtocol} for the left {@link BuildWallConfiguration}.
    * @param rightProtocol the {@link FileLocationProtocol} for the right {@link BuildWallConfiguration}.
    */
   BuildWallConfigurationSessions( JenkinsDatabase database, FileLocationProtocol leftProtocol, FileLocationProtocol rightProtocol ) {
      this.leftConfiguration = new BuildWallConfigurationImpl();
      this.leftConfigurationFileLocation = leftProtocol;
      
      this.rightConfiguration = new BuildWallConfigurationImpl();
      this.rightConfigurationFileLocation = rightProtocol;
      
      applyDefaultConfigurations();

      ModelMarshaller leftMarshaller = constructMarshaller( leftConfiguration, database, leftProtocol );
      this.leftSessions = new SessionManager( leftMarshaller );
      configureSessionManager( leftConfiguration, leftSessions );
      leftMarshaller.read();
      
      ModelMarshaller rightMarshaller = constructMarshaller( rightConfiguration, database, rightProtocol );
      this.rightSessions = new SessionManager( rightMarshaller );
      configureSessionManager( rightConfiguration, rightSessions );
      rightMarshaller.read();
   }//End Constructor
   
   /**
    * Method to apply the default configurations to the {@link BuildWallConfiguration}s.
    */
   private void applyDefaultConfigurations(){
      rightConfiguration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
      leftConfiguration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      leftConfiguration.numberOfColumns().set( 1 );
   }//End Method
   
   /**
    * Method to construct the {@link ModelMarshaller} using {@link BuildWallConfigurationPersistence}.
    * @param configuration the {@link BuildWallConfiguration} to persist.
    * @param database the {@link JenkinsDatabase} for persisting {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s.
    * @param locationProtocol the {@link FileLocationProtocol} for the persistence.
    * @return the {@link ModelMarshaller} constructed.
    */
   private ModelMarshaller constructMarshaller( 
            BuildWallConfiguration configuration, 
            JenkinsDatabase database, 
            FileLocationProtocol locationProtocol 
   ){
      BuildWallConfigurationPersistence persistence = new BuildWallConfigurationPersistence( configuration, database );
      return new ModelMarshaller( 
               persistence.structure(), 
               persistence.readHandles(), 
               persistence.writeHandles(), 
               locationProtocol 
      );
   }//End Method
   
   /**
    * Method to configure a {@link SessionManager} for the given {@link BuildWallConfiguration}.
    * @param configuration the {@link BuildWallConfiguration} to session.
    * @param session the {@link SessionManager} for controlling sessions.
    */
   private void configureSessionManager( BuildWallConfiguration configuration, SessionManager session ){
      session.triggerWriteOnChange( configuration.numberOfColumns() );
      session.triggerWriteOnChange( configuration.jobPanelDescriptionProvider() );
      
      session.triggerWriteOnChange( configuration.jobPolicies() );
      
      session.triggerWriteOnChange( configuration.jobNameFont() );
      session.triggerWriteOnChange( configuration.buildNumberFont() );
      session.triggerWriteOnChange( configuration.completionEstimateFont() );
      session.triggerWriteOnChange( configuration.detailFont() );
      
      session.triggerWriteOnChange( configuration.jobNameColour() );
      session.triggerWriteOnChange( configuration.buildNumberColour() );
      session.triggerWriteOnChange( configuration.completionEstimateColour() );
      session.triggerWriteOnChange( configuration.detailColour() );
   }//End Method
   
   /**
    * Getter for the right {@link BuildWallConfiguration}.
    * @return the {@link BuildWallConfiguration}.
    */
   public BuildWallConfiguration getRightConfiguration(){
      return rightConfiguration;
   }//End Method
   
   /**
    * Getter for the left {@link BuildWallConfiguration}.
    * @return the {@link BuildWallConfiguration}.
    */
   public BuildWallConfiguration getLeftConfiguration(){
      return leftConfiguration;
   }//End Method
   
   /**
    * Method to shutdown the {@link SessionManager}s associated.
    */
   public void shutdownSessions(){
      leftSessions.stop();
      rightSessions.stop();
   }//End Method
   
   FileLocationProtocol rightConfigurationFileLocation(){
      return rightConfigurationFileLocation;
   }//End Method
   
   FileLocationProtocol leftConfigurationFileLocation(){
      return leftConfigurationFileLocation;
   }//End Method
}//End Class
