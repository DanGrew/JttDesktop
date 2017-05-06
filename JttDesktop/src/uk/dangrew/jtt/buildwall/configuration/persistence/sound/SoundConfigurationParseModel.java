/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.sound;

import uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link SoundConfigurationParseModel} provides the model to read from and write to when converting
 * a {@link SoundConfiguration} from json data.
 */
class SoundConfigurationParseModel {
   
   private final JenkinsDatabase database;
   private final SoundConfiguration configuration;
   
   private BuildResultStatus previousStateBuffer;
   private BuildResultStatus currentStateBuffer;
   private String filenameBuffer;
   
   /**
    * Constructs a new {@link SoundConfigurationParseModel}.
    * @param configuration the {@link SoundConfiguration} being serialized.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsJob}s.
    */
   SoundConfigurationParseModel( SoundConfiguration configuration, JenkinsDatabase database ) {
      if ( configuration == null || database == null ) {
         throw new IllegalArgumentException( "Arguments must not be null." );
      }
      
      this.configuration = configuration;
      this.database = database;
   }//End Constructor
   
   /**
    * Method to start parsing changes. This will clear the existing configuration.
    * @param key the parsed key.
    */
   void startParsingChanges( String key ) {
      configuration.statusChangeSounds().clear();
   }//End Method
   
   /**
    * Method to start parsing a single {@link BuildResultStatusChange} configuration.
    * @param key the parsed key.
    */
   void startParsingChange( String key ) {
      clearBuffers();
   }//End Method
   
   /**
    * Method to clear the parsing buffers.
    */
   private void clearBuffers(){
      previousStateBuffer = null;
      currentStateBuffer = null;
      filenameBuffer = null;
   }//End Method
   
   /**
    * Method to set the parsed {@link BuildResultStatus} for the previous state.
    * @param key the parsed key.
    * @param status the parsed value.
    */
   void setPreviousState( String key, BuildResultStatus status ) {
      previousStateBuffer = status;
      tryToPutChangeSound();
   }//End Method
   
   /**
    * Method to set the parsed {@link BuildResultStatus} for the current state.
    * @param key the parsed key.
    * @param status the parsed value.
    */
   void setCurrentState( String key, BuildResultStatus status ) {
      currentStateBuffer = status;
      tryToPutChangeSound();
   }//End Method
   
   /**
    * Method to set the parsed filename for change.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setFilename( String key, String value ) {
      filenameBuffer = value;
      tryToPutChangeSound();
   }//End Method
   
   /**
    * Method to attempt to push the configuration through, only if all values are parsed. When configuration
    * is pushed, the buffers are cleared.
    */
   private void tryToPutChangeSound(){
      if ( previousStateBuffer == null || currentStateBuffer == null || filenameBuffer == null ) {
         return;
      }
      
      configuration.statusChangeSounds().put( 
               new BuildResultStatusChange( previousStateBuffer, currentStateBuffer ), 
               filenameBuffer 
      );
      clearBuffers();
   }//End Method
   
   /**
    * Method to start parsing the excluded {@link JenkinsJob}s.
    * @param key the parsed key.
    */
   void startParsingExclusions( String key ) {
      configuration.excludedJobs().clear();
   }//End Method

   /**
    * Method to set the excluded {@link JenkinsJob} name.
    * @param key the parsed key.
    * @param jobName the {@link JenkinsJob} parsed.
    */
   void setExclusion( String key, String jobName ) {
      JenkinsJob job = database.getJenkinsJob( jobName );
      if ( job == null ) {
         job = new JenkinsJobImpl( jobName );
         database.store( job );
      }
      
      configuration.excludedJobs().add( job );
   }//End Method

}//End Class
