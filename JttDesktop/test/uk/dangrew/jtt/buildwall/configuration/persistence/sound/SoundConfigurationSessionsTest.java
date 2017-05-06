/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.sound;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.main.JenkinsTestTracker;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;

public class SoundConfigurationSessionsTest {

   @Mock private JenkinsDatabase database;
   @Mock private JarJsonPersistingProtocol protocol;
   @Captor private ArgumentCaptor< JSONObject > objectCaptor;
   
   private SoundConfigurationSessions systemUnderTest;
   private SoundConfiguration configuration;
   
   private CountDownLatch latch;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
      configuration = new SoundConfiguration();
      systemUnderTest = new SoundConfigurationSessions( configuration, database, protocol );
      
      latch = new CountDownLatch( 1 );
      when( protocol.writeToLocation( Mockito.any() ) ).thenAnswer( invocation -> { latch.countDown(); return true; } );
   }//End Method
   
   @After public void tearDown(){
      systemUnderTest.shutdownSessions();
   }//End Method
   
   @Test public void publicConstructorShouldDefineCorrectFileLocations(){
      systemUnderTest = new SoundConfigurationSessions( configuration, database );
      assertThat( 
               systemUnderTest.locationProtocol().getLocation(), 
               is( new JarJsonPersistingProtocol( 
                        SoundConfigurationSessions.FOLDER_NAME, 
                        SoundConfigurationSessions.SOUND_FILE_NAME, 
                        JenkinsTestTracker.class 
               ).getLocation() ) 
      );
   }//End Method
   
   @Test public void shouldReadOnConstruction(){
      verify( protocol ).readFromLocation();
   }//End Method
   
   @Test public void shutdownShouldStopWriting(){
      systemUnderTest.shutdownSessions();
      
      configuration.statusChangeSounds().put( 
               new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE ), "anything" 
      );
      
      verify( protocol, never() ).writeToLocation( Mockito.any() );
   }//End Method
   
   @Test public void shouldTriggerWriteWhenChangeSoundChanged() throws InterruptedException {
      configuration.statusChangeSounds().put( 
               new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE ), "anything" 
      );
      latch.await();
      
      verify( protocol ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"Filename\":\"anything\"" ) );
      
      verifyReadAlwaysHappensBeforeWrite();
   }//End Method
   
   @Test public void shouldTriggerWriteWhenExclusionsChanged() throws InterruptedException {
      configuration.excludedJobs().add( new JenkinsJobImpl( "anything" ) );
      latch.await();
      
      verify( protocol ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "anything" ) );
      
      verifyReadAlwaysHappensBeforeWrite();
   }//End Method
   
   /**
    * Method to verify that read always happens before write on the given {@link JarJsonPersistingProtocol}.
    */
   private void verifyReadAlwaysHappensBeforeWrite(){
      InOrder order = inOrder( protocol );
      order.verify( protocol ).readFromLocation();
      order.verify( protocol ).writeToLocation( Mockito.any() );
   }//End Method
   
   @Test public void shouldUseConfiguration(){
      assertThat( systemUnderTest.uses( configuration ), is( true ) );
      assertThat( systemUnderTest.uses( new SoundConfiguration() ), is( false ) );
   }//End Method
   
}//End Class
