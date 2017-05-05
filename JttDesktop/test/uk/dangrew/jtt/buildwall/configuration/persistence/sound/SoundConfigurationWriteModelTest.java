/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.sound;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

public class SoundConfigurationWriteModelTest {

   private static final String ANYTHING = "anything";
   private static final String FILENAME = "filename";
   private static final String FILENAME2 = "filename2";
   
   private SoundConfiguration configuration;
   private SoundConfigurationWriteModel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new SoundConfiguration();
      systemUnderTest = new SoundConfigurationWriteModel( configuration );
   }//End Method
   
   @Test( expected = NullPointerException.class ) public void shouldNotAcceptNullConfiguration(){
      new SoundConfigurationWriteModel( null );
   }//End Method
   
   @Test public void shouldProvideNumberOfConfiguredChanges(){
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE ), FILENAME );
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE ), FILENAME );
      assertThat( systemUnderTest.getNumberOfStatusChanges( ANYTHING ), is( 2 ) );
      
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.SUCCESS ), FILENAME );
      assertThat( systemUnderTest.getNumberOfStatusChanges( ANYTHING ), is( 3 ) );
   }//End Method
   
   @Test public void shouldProvideNumberOfExcludedJobs(){
      configuration.excludedJobs().add( new JenkinsJobImpl( "job1" ) );
      configuration.excludedJobs().add( new JenkinsJobImpl( "job2" ) );
      configuration.excludedJobs().add( new JenkinsJobImpl( "job3" ) );
      assertThat( systemUnderTest.getNumberOfJobExclusions( ANYTHING ), is( 3 ) );
      
      configuration.excludedJobs().add( new JenkinsJobImpl( "job4" ) );
      assertThat( systemUnderTest.getNumberOfJobExclusions( ANYTHING ), is( 4 ) );
   }//End Method
   
   @Test public void shouldBufferChanges(){
      systemUnderTest.startWritingChanges( ANYTHING );
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( nullValue() ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( nullValue() ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( nullValue() ) );
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.SUCCESS ), FILENAME );
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( nullValue() ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( nullValue() ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldWriteChange(){
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE ), FILENAME );
      systemUnderTest.startWritingChanges( ANYTHING );
      
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( BuildResultStatus.SUCCESS ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( BuildResultStatus.FAILURE ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( FILENAME ) );
   }//End Method
   
   @Test public void shouldWriteConsecutiveChanges(){
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE ), FILENAME );
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.NOT_BUILT ), FILENAME2 );
      systemUnderTest.startWritingChanges( ANYTHING );
      
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( BuildResultStatus.SUCCESS ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( BuildResultStatus.FAILURE ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( FILENAME ) );
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( BuildResultStatus.ABORTED ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( BuildResultStatus.NOT_BUILT ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( FILENAME2 ) );
   }//End Method
   
   @Test public void shouldWriteNullFile(){
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE ), null );
      systemUnderTest.startWritingChanges( ANYTHING );
      
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( BuildResultStatus.SUCCESS ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( BuildResultStatus.FAILURE ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldReturnNullIfRunOutOfDataToWrite(){
      systemUnderTest.startWritingChanges( ANYTHING );
      
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( nullValue() ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( nullValue() ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldWriteExclusion(){
      configuration.excludedJobs().add( new JenkinsJobImpl( "Job1" ) );
      systemUnderTest.startWritingExclusions( ANYTHING );
      assertThat( systemUnderTest.getExclusion( ANYTHING ), is( "Job1" ) );
   }//End Method
   
   @Test public void shouldWriteultipleExclusions(){
      configuration.excludedJobs().add( new JenkinsJobImpl( "Job1" ) );
      configuration.excludedJobs().add( new JenkinsJobImpl( "Job2" ) );
      systemUnderTest.startWritingExclusions( ANYTHING );
      assertThat( systemUnderTest.getExclusion( ANYTHING ), is( "Job1" ) );
      assertThat( systemUnderTest.getExclusion( ANYTHING ), is( "Job2" ) );
      assertThat( systemUnderTest.getExclusion( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldBufferExclusions(){
      systemUnderTest.startWritingExclusions( ANYTHING );
      assertThat( systemUnderTest.getExclusion( ANYTHING ), is( nullValue() ) );
      configuration.excludedJobs().add( new JenkinsJobImpl( "job1" ) );
      assertThat( systemUnderTest.getExclusion( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldClearChangesBetweenWrites(){
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE ), null );
      systemUnderTest.startWritingChanges( ANYTHING );
      configuration.statusChangeSounds().clear();
      configuration.statusChangeSounds().put( new BuildResultStatusChange( BuildResultStatus.UNKNOWN, BuildResultStatus.UNSTABLE ), FILENAME );
      systemUnderTest.startWritingChanges( ANYTHING );
      
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( BuildResultStatus.UNKNOWN ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( BuildResultStatus.UNSTABLE ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( FILENAME ) );
      
      assertThat( systemUnderTest.getPreviousState( ANYTHING ), is( nullValue() ) );
      assertThat( systemUnderTest.getCurrentState( ANYTHING ), is( nullValue() ) );
      assertThat( systemUnderTest.getFilename( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldClearExclusionsBetweenWrites(){
      configuration.excludedJobs().add( new JenkinsJobImpl( "Job1" ) );
      systemUnderTest.startWritingExclusions( ANYTHING );
      configuration.excludedJobs().clear();
      configuration.excludedJobs().add( new JenkinsJobImpl( "Job2" ) );
      systemUnderTest.startWritingExclusions( ANYTHING );
      assertThat( systemUnderTest.getExclusion( ANYTHING ), is( "Job2" ) );
      assertThat( systemUnderTest.getExclusion( ANYTHING ), is( nullValue() ) );
   }//End Method
   
}//End Class
