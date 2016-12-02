/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.friendly.javafx.FriendlyMediaPlayer;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

public class SoundPlayerTest {

   private SoundTriggerEvent events;
   private SoundConfiguration configuration;
   @Mock private StringMediaConverter converter;
   @Mock private FriendlyMediaPlayer player;
   private SoundPlayer systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      when( converter.convert( Mockito.anyString() ) ).thenReturn( player );
      
      events = new SoundTriggerEvent();
      events.clearAllSubscriptions();
      configuration = new SoundConfiguration();
      systemUnderTest = new SoundPlayer( converter, configuration );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      Thread.sleep( 1000 );
      new StringMediaConverter().convert( "/Users/Amelia/Desktop/Sad_Trombone.wav" ).friendly_play();
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullConfiguration(){
      new SoundPlayer( null );
   }//End Method
   
   @Test public void shouldPlayMediaPlayerWhenEventIsReceived(){
      BuildResultStatusChange change = new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE );
      String fileName = "anything";
      when( converter.convert( Mockito.anyString() ) ).thenReturn( null );
      when( converter.convert( fileName ) ).thenReturn( player );
      
      configuration.statusChangeSounds().put( change, fileName );
      events.fire( new Event<>( change ) );
      
      verify( player ).friendly_play();
   }//End Method
   
   @Test public void shouldNotPlaySoundWhenNoMedia(){
      BuildResultStatusChange change = new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE );
      String fileName = "anything";
      when( converter.convert( Mockito.anyString() ) ).thenReturn( null );
      
      configuration.statusChangeSounds().put( change, fileName );
      events.fire( new Event<>( change ) );
      
      verify( player, never() ).friendly_play();
   }//End Method
   
}//End Class
