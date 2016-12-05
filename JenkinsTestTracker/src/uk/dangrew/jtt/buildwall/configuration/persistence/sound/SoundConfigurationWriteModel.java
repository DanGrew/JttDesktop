/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.sound;

import java.util.ArrayList;
import java.util.List;

import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link SoundConfigurationWriteModel} provides the model to write to when converting
 * a {@link SoundConfiguration} to json data.
 */
class SoundConfigurationWriteModel {
   
   private final SoundConfiguration configuration;
   
   private final List< BuildResultStatus > bufferedPreviousState;
   private final List< BuildResultStatus > bufferedCurrentState;
   private final List< String > bufferedFilename;
   
   private final List< JenkinsJob > bufferedJobs;
   
   /**
    * Constructs a new {@link SoundConfigurationWriteModel}.
    * @param configuration the {@link SoundConfiguration} being serialized.
    */
   SoundConfigurationWriteModel( SoundConfiguration configuration ) {
      if ( configuration == null ) {
         throw new NullPointerException( "Arguments must not be null." );
      }
      
      this.configuration = configuration;
      this.bufferedPreviousState = new ArrayList<>();
      this.bufferedCurrentState = new ArrayList<>();
      this.bufferedFilename = new ArrayList<>();
      this.bufferedJobs = new ArrayList<>();
   }//End Constructor
   
   /**
    * Method to get the number of {@link uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange}
    * configurations applied.
    * @param key the parsed key.
    * @return the number being serialized.
    */
   int getNumberOfStatusChanges( String key ){
      return configuration.statusChangeSounds().size();
   }//End Method
   
   /**
    * Method to get the number of {@link JenkinsJob} exclusions.
    * @param key the parsed key.
    * @return the number being serialized.
    */
   int getNumberOfJobExclusions( String key ){
      return configuration.excludedJobs().size();
   }//End Method
   
   /**
    * Method to start writing the {@link uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange} 
    * configurations.
    * @param key the parsed key.
    */
   void startWritingChanges( String key ) {
      bufferedPreviousState.clear();
      bufferedCurrentState.clear();
      bufferedFilename.clear();
      
      configuration.statusChangeSounds().forEach( ( c, f ) -> {
         bufferedPreviousState.add( c.getPreviousStatus() );
         bufferedCurrentState.add( c.getCurrentStatus() );
         bufferedFilename.add( f );
      } );
   }//End Method
   
   /**
    * Getter for the next buffered previous {@link BuildResultStatus}.
    * @param key the parsed key.
    * @return the value to write for the key.
    */
   BuildResultStatus getPreviousState( String key ) {
      if ( bufferedPreviousState.isEmpty() ) {
         return null;
      }
      return bufferedPreviousState.remove( 0 );
   }//End Method
   
   /**
    * Getter for the next buffered current {@link BuildResultStatus}.
    * @param key the parsed key.
    * @return the value to write for the key.
    */
   BuildResultStatus getCurrentState( String key ) {
      if ( bufferedCurrentState.isEmpty() ) {
         return null;
      }
      return bufferedCurrentState.remove( 0 );
   }//End Method
   
   /**
    * Getter for the next buffered filename configured.
    * @param key the parsed key.
    * @return the value to write for the key.
    */
   String getFilename( String key ) {
      if ( bufferedFilename.isEmpty() ) {
         return null;
      }
      return bufferedFilename.remove( 0 );
   }//End Method
   
   /**
    * Method to start writing the exclusions.
    * @param key the parsed key.
    */
   void startWritingExclusions( String key ) {
      bufferedJobs.clear();
      configuration.excludedJobs().forEach( bufferedJobs::add );
   }//End Method
   
   /**
    * Getter for the next buffered {@link JenkinsJob} exclusion.
    * @param key the parsed key.
    * @return the {@link JenkinsJob} name.
    */
   String getExclusion( String key ) {
      if ( bufferedJobs.isEmpty() ) {
         return null;
      }
      return bufferedJobs.remove( 0 ).nameProperty().get();
   }//End Method

}//End Class
