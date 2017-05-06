/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.sound;

import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.main.JenkinsTestTracker;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;
import uk.dangrew.jupa.json.marshall.ModelMarshaller;
import uk.dangrew.jupa.json.session.SessionManager;
import uk.dangrew.sd.logging.location.FileLocationProtocol;

/**
 * The {@link SoundConfigurationSessions} provides the {@link SoundConfiguration} along with their {@link SessionManager}s.
 */
public class SoundConfigurationSessions {

   static final String FOLDER_NAME = "uk.dangrew.jtt.configuration";
   static final String SOUND_FILE_NAME = "sound.json";
   
   private final JenkinsDatabase database;
   private final SoundConfiguration configuration;
   private final SessionManager sessions;
   private final JarJsonPersistingProtocol locationProtocol;
   
   /**
    * Constructs a new {@link SoundConfigurationSessions}.
    * @param configuration the {@link SoundConfiguration} to persist.
    * @param database the {@link JenkinsDatabase} for accessing {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s.
    */
   public SoundConfigurationSessions( 
            SoundConfiguration configuration,
            JenkinsDatabase database
           
   ) {
      this( 
               configuration,
               database,
               new JarJsonPersistingProtocol( 
                        FOLDER_NAME, SOUND_FILE_NAME, JenkinsTestTracker.class 
               ) 
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link SoundConfigurationSessions}.
    * @param configuration the {@link SoundConfiguration} to persist.
    * @param database the {@link JenkinsDatabase} for accessing {@link uk.dangrew.jtt.model.jobs.JenkinsJob}.
    * @param locationProtocol the {@link JarJsonPersistingProtocol} to persist to.
    */
   SoundConfigurationSessions( 
            SoundConfiguration configuration,
            JenkinsDatabase database, 
            JarJsonPersistingProtocol locationProtocol 
   ) {
      this.configuration = configuration;
      this.database = database;
      this.locationProtocol = locationProtocol;
      
      ModelMarshaller marshaller = constructMarshaller();
      marshaller.read();
      this.sessions = new SessionManager( marshaller );
      configureSessionManager();
   }//End Constructor
   
   /**
    * Method to construct the {@link ModelMarshaller} using {@link SoundConfigurationPersistence}.
    * @return the {@link ModelMarshaller} constructed.
    */
   private ModelMarshaller constructMarshaller(){
      SoundConfigurationPersistence persistence = new SoundConfigurationPersistence( configuration, database );
      return new ModelMarshaller( 
               persistence.structure(), 
               persistence.readHandles(), 
               persistence.writeHandles(), 
               locationProtocol 
      );
   }//End Method
   
   /**
    * Method to configure a {@link SessionManager} for the given {@link SoundConfiguration}.
    */
   private void configureSessionManager(){
      sessions.triggerWriteOnChange( configuration.statusChangeSounds() );
      sessions.triggerWriteOnChange( configuration.excludedJobs() );
   }//End Method
   
   /**
    * Method to shutdown the {@link SessionManager}s associated.
    */
   public void shutdownSessions(){
      sessions.stop();
   }//End Method
   
   /**
    * Method to determine whether the given is associated.
    * @param soundConfiguration the {@link SoundConfiguration} in question.
    * @return true if identical.
    */
   public boolean uses( SoundConfiguration soundConfiguration ) {
      return this.configuration == soundConfiguration;
   }//End Method
   
   /**
    * Method to determine whether the given is associated.
    * @param database the {@link JenkinsDatabase} in question.
    * @return true if identical.
    */
   public boolean uses( JenkinsDatabase database ) {
      return this.database == database;
   }//End Method
   
   FileLocationProtocol locationProtocol(){
      return locationProtocol;
   }//End Method
   
}//End Class
