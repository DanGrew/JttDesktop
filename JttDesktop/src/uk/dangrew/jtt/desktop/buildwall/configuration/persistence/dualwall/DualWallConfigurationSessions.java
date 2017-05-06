/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.persistence.dualwall;

import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.desktop.main.JenkinsTestTracker;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;
import uk.dangrew.jupa.json.marshall.ModelMarshaller;
import uk.dangrew.jupa.json.session.SessionManager;
import uk.dangrew.sd.logging.location.FileLocationProtocol;

/**
 * The {@link DualWallConfigurationSessions} provides the {@link DualWallConfiguration}s for the
 * left and right {@link uk.dangrew.jtt.buildwall.layout.GridWallImpl}s along with their {@link SessionManager}s.
 */
public class DualWallConfigurationSessions {

   static final String FOLDER_NAME = "uk.dangrew.jtt.configuration";
   static final String DUAL_WALL_FILE_NAME = "dual-build-wall.json";
   
   private final DualWallConfiguration dualConfiguration;
   private final SessionManager sessions;
   private final JarJsonPersistingProtocol dualConfigurationFileLocation;
   
   /**
    * Constructs a new {@link DualWallConfigurationSessions}.
    * @param dualConfiguration the {@link DualWallConfiguration} to persist.
    */
   public DualWallConfigurationSessions( 
            DualWallConfiguration dualConfiguration 
   ) {
      this( 
               dualConfiguration,
               new JarJsonPersistingProtocol( 
                        FOLDER_NAME, DUAL_WALL_FILE_NAME, JenkinsTestTracker.class 
               ) 
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link DualWallConfigurationSessions}.
    * @param dualConfiguration the {@link DualWallConfiguration} to persist.
    * @param dualProtocol the {@link JarJsonPersistingProtocol} to persist to.
    */
   DualWallConfigurationSessions( 
            DualWallConfiguration dualConfiguration, 
            JarJsonPersistingProtocol dualProtocol 
   ) {
      this.dualConfiguration = dualConfiguration;
      this.dualConfigurationFileLocation = dualProtocol;
      
      ModelMarshaller marshaller = constructMarshaller( dualConfiguration, dualProtocol );
      marshaller.read();
      this.sessions = new SessionManager( marshaller );
      configureSessionManager( dualConfiguration, sessions );
   }//End Constructor
   
   /**
    * Method to construct the {@link ModelMarshaller} using {@link DualWallConfigurationPersistence}.
    * @param configuration the {@link DualWallConfiguration} to persist.
    * @param locationProtocol the {@link JarJsonPersistingProtocol} for the persistence.
    * @return the {@link ModelMarshaller} constructed.
    */
   private ModelMarshaller constructMarshaller( 
            DualWallConfiguration configuration, 
            JarJsonPersistingProtocol locationProtocol 
   ){
      DualWallConfigurationPersistence persistence = new DualWallConfigurationPersistence( configuration );
      return new ModelMarshaller( 
               persistence.structure(), 
               persistence.readHandles(), 
               persistence.writeHandles(), 
               locationProtocol 
      );
   }//End Method
   
   /**
    * Method to configure a {@link SessionManager} for the given {@link DualWallConfiguration}.
    * @param configuration the {@link BuildWallConfiguration} to session.
    * @param session the {@link SessionManager} for controlling sessions.
    */
   private void configureSessionManager( DualWallConfiguration configuration, SessionManager session ){
      session.triggerWriteOnChange( configuration.dividerPositionProperty() );
      session.triggerWriteOnChange( configuration.dividerOrientationProperty() );
   }//End Method
   
   /**
    * Method to shutdown the {@link SessionManager}s associated.
    */
   public void shutdownSessions(){
      sessions.stop();
   }//End Method
   
   FileLocationProtocol dualConfigurationFileLocation(){
      return dualConfigurationFileLocation;
   }//End Method
   
}//End Class
