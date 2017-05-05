/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.sound;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.TestJenkinsDatabaseImpl;

public class SoundConfigurationParseModelTest {

   private static final String ANYTHING = "anything";
   private static final String FILENAME = "filename";
   
   private JenkinsDatabase database;
   private SoundConfiguration configuration;
   private SoundConfigurationParseModel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      database = new TestJenkinsDatabaseImpl();
      configuration = new SoundConfiguration();
      systemUnderTest = new SoundConfigurationParseModel( configuration, database );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullConfiguration(){
      new SoundConfigurationParseModel( null, database );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      new SoundConfigurationParseModel( configuration, null );
   }//End Method
   
   @Test public void shouldClearExistingWhenStartingParsingChanges(){
      configuration.statusChangeSounds().put( 
               new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.ABORTED ), FILENAME 
      );
      systemUnderTest.startParsingChanges( ANYTHING );
      assertThat( configuration.statusChangeSounds().isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldNotPopulateUntilAllValuesParsed(){
      assertThat( configuration.statusChangeSounds().isEmpty(), is( true ) );
      systemUnderTest.setCurrentState( ANYTHING, BuildResultStatus.ABORTED );
      assertThat( configuration.statusChangeSounds().isEmpty(), is( true ) );
      systemUnderTest.setPreviousState( ANYTHING, BuildResultStatus.FAILURE );
      assertThat( configuration.statusChangeSounds().isEmpty(), is( true ) );
      systemUnderTest.setFilename( ANYTHING, FILENAME );
      assertThat( configuration.statusChangeSounds().get( 
               new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.ABORTED ) 
      ), is( FILENAME ) );
   }//End Method
   
   @Test public void shouldNotPopulateNextUntilAllValuesParsed(){
      shouldNotPopulateUntilAllValuesParsed();
      
      String alternateFilename = "another file";
      systemUnderTest.setFilename( ANYTHING, alternateFilename );
      assertThat( configuration.statusChangeSounds().get( 
               new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.ABORTED ) 
      ), is( FILENAME ) );
      
      systemUnderTest.setPreviousState( ANYTHING, BuildResultStatus.UNKNOWN );
      systemUnderTest.setCurrentState( ANYTHING, BuildResultStatus.SUCCESS );
      assertThat( configuration.statusChangeSounds().get( 
               new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.ABORTED ) 
      ), is( FILENAME ) );
      assertThat( configuration.statusChangeSounds().get( 
               new BuildResultStatusChange( BuildResultStatus.UNKNOWN, BuildResultStatus.SUCCESS ) 
      ), is( alternateFilename ) );
   }//End Method
   
   @Test public void shouldClearBuffersWhenNewObjectStarted(){
      systemUnderTest.setPreviousState( ANYTHING, BuildResultStatus.FAILURE );
      systemUnderTest.setCurrentState( ANYTHING, BuildResultStatus.ABORTED );
      systemUnderTest.startParsingChange( ANYTHING );
      systemUnderTest.setFilename( ANYTHING, FILENAME );
      assertThat( configuration.statusChangeSounds().isEmpty(),is( true ) );
      
      systemUnderTest.setPreviousState( ANYTHING, BuildResultStatus.FAILURE );
      systemUnderTest.setCurrentState( ANYTHING, BuildResultStatus.ABORTED );
      assertThat( configuration.statusChangeSounds().get( 
               new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.ABORTED ) 
      ), is( FILENAME ) );
   }//End Method
   
   @Test public void shouldFetchJobFromDatabaseAndExclude(){
      final String jobName = "some name";
      JenkinsJob job = new JenkinsJobImpl( jobName );
      database.store( job );
      
      systemUnderTest.setExclusion( ANYTHING, jobName );
      assertThat( configuration.excludedJobs(), contains( job ) );
   }//End Method
   
   @Test public void shouldCreateJobForJobNameThatDoesntExist(){
      String jobName = "doesnt exist";
      systemUnderTest.setExclusion( ANYTHING, jobName );
      assertThat( database.hasJenkinsJob( jobName ), is( true ) );
      assertThat( configuration.excludedJobs(), is( contains( database.getJenkinsJob( jobName ) ) ) );
   }//End Method
   
}//End Class
