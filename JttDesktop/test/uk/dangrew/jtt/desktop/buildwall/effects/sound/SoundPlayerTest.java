/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.sound;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.dangrew.jtt.connection.api.handling.live.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.friendly.javafx.FriendlyMediaPlayer;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.kode.event.structure.Event;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SoundPlayerTest {

   private SoundTriggerEvent events;
   private SoundConfiguration configuration;
   @Mock private StringMediaConverter converter;
   @Mock private FriendlyMediaPlayer player;
   private SoundPlayer systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
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
   
   @Test public void shouldBeAssociatedWith(){
      assertThat( systemUnderTest.isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new SoundConfiguration() ), is( false ) );
   }//End Method
   
   @Test public void shouldNotGarbageCollectSinglePlayerUntilTheyAreComplete(){
      BuildResultStatusChange change = new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE );
      String fileName = "anything";
      when( converter.convert( Mockito.anyString() ) ).thenReturn( null );
      when( converter.convert( fileName ) ).thenReturn( player );
      
      configuration.statusChangeSounds().put( change, fileName );
      events.fire( new Event<>( change ) );
      
      ArgumentCaptor< Runnable > runnableCaptor = ArgumentCaptor.forClass( Runnable.class );
      verify( player ).friendly_setOnEndOfMedia( runnableCaptor.capture() );
      
      assertThat( systemUnderTest.isWaitingFor( player ), is( true ) );
      runnableCaptor.getValue().run();
      assertThat( systemUnderTest.isWaitingFor( player ), is( false ) );
   }//End Method
   
   @Test public void shouldNotGarbageCollectAnyPlayersUntilTheyAreComplete(){
      FriendlyMediaPlayer player2 = mock( FriendlyMediaPlayer.class );
      FriendlyMediaPlayer player3 = mock( FriendlyMediaPlayer.class );
      FriendlyMediaPlayer player4 = mock( FriendlyMediaPlayer.class );
      
      BuildResultStatusChange change = new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE );
      String fileName = "anything";
      when( converter.convert( Mockito.anyString() ) ).thenReturn( null );
      when( converter.convert( fileName ) ).thenReturn( player ).thenReturn( player2 ).thenReturn( player3 ).thenReturn( player4 );
      
      configuration.statusChangeSounds().put( change, fileName );
      events.fire( new Event<>( change ) );
      events.fire( new Event<>( change ) );
      events.fire( new Event<>( change ) );
      events.fire( new Event<>( change ) );
      
      ArgumentCaptor< Runnable > runnableCaptor = ArgumentCaptor.forClass( Runnable.class );
      verify( player ).friendly_setOnEndOfMedia( runnableCaptor.capture() );
      verify( player2 ).friendly_setOnEndOfMedia( runnableCaptor.capture() );
      verify( player3 ).friendly_setOnEndOfMedia( runnableCaptor.capture() );
      verify( player4 ).friendly_setOnEndOfMedia( runnableCaptor.capture() );
      
      assertThat( systemUnderTest.isWaitingFor( player ), is( true ) );
      assertThat( systemUnderTest.isWaitingFor( player2 ), is( true ) );
      assertThat( systemUnderTest.isWaitingFor( player3 ), is( true ) );
      assertThat( systemUnderTest.isWaitingFor( player4 ), is( true ) );
      runnableCaptor.getAllValues().get( 0 ).run();
      assertThat( systemUnderTest.isWaitingFor( player ), is( false ) );
      assertThat( systemUnderTest.isWaitingFor( player2 ), is( true ) );
      assertThat( systemUnderTest.isWaitingFor( player3 ), is( true ) );
      assertThat( systemUnderTest.isWaitingFor( player4 ), is( true ) );
      runnableCaptor.getAllValues().get( 2 ).run();
      assertThat( systemUnderTest.isWaitingFor( player ), is( false ) );
      assertThat( systemUnderTest.isWaitingFor( player2 ), is( true ) );
      assertThat( systemUnderTest.isWaitingFor( player3 ), is( false ) );
      assertThat( systemUnderTest.isWaitingFor( player4 ), is( true ) );
      runnableCaptor.getAllValues().get( 3 ).run();
      assertThat( systemUnderTest.isWaitingFor( player ), is( false ) );
      assertThat( systemUnderTest.isWaitingFor( player2 ), is( true ) );
      assertThat( systemUnderTest.isWaitingFor( player3 ), is( false ) );
      assertThat( systemUnderTest.isWaitingFor( player4 ), is( false ) );
      runnableCaptor.getAllValues().get( 1 ).run();
      assertThat( systemUnderTest.isWaitingFor( player ), is( false ) );
      assertThat( systemUnderTest.isWaitingFor( player2 ), is( false ) );
      assertThat( systemUnderTest.isWaitingFor( player3 ), is( false ) );
      assertThat( systemUnderTest.isWaitingFor( player4 ), is( false ) );
   }//End Method
   
}//End Class
