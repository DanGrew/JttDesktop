/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import uk.dangrew.jtt.api.handling.JenkinsProcessing;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.launch.LaunchOptions;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.sd.core.lockdown.DigestProgressReceiverImpl;
import uk.dangrew.sd.core.message.Message;
import uk.dangrew.sd.core.message.Messages;
import uk.dangrew.sd.core.progress.Progress;
import uk.dangrew.sd.core.progress.Progresses;
import uk.dangrew.sd.core.source.Source;
import uk.dangrew.sd.core.source.SourceImpl;
import uk.dangrew.sd.progressbar.model.DigestProgressBar;
import uk.dangrew.sd.viewer.basic.DigestViewer;

public class JttInitializerTest {

   @Mock private JenkinsProcessing processing;
   private JenkinsDatabase database;
   @Mock private EnvironmentWindow window;
   @Mock private SystemConfiguration configuration;
   @Mock private DigestViewer digestViewer;
   
   @Mock private Thread thread;
   @Mock private Function< Runnable, Thread > threadSupplier;
   @Captor private ArgumentCaptor< Node > contentCaptor;
   @Captor private ArgumentCaptor< Runnable > threadRunnableCaptor;
   
   private JttInitializer systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      database = new JenkinsDatabaseImpl();
      when( threadSupplier.apply( Mockito.any() ) ).thenReturn( thread );
      systemUnderTest = new JttInitializer( threadSupplier, processing, database, window, configuration, digestViewer );
   }//End Method

   @Test public void shouldSetDigestProgressBarAsWindowContent() {
      verify( window ).setContent( contentCaptor.capture() );
      
      Node captured = contentCaptor.getValue();
      assertThat( captured, is( instanceOf( DigestProgressBar.class ) ) );
      DigestProgressBar bar = ( DigestProgressBar ) captured;
      assertThat( bar.isAssociatedWith( new SourceImpl( processing ) ), is( true ) );
      assertThat( bar.getMinWidth(), is( JttInitializer.BAR_WIDTH ) );
   }//End Method
   
   @Test public void shouldBeConnectedToDigest(){
      assertThat( systemUnderTest.digestConnection(), is( notNullValue() ) );
      assertThat( systemUnderTest.digestConnection().isConnected(), is( true ) );
   }//End Method
   
   @Test public void shouldConstructThreadAndStrart(){
      verify( thread ).start();
      
      verify( threadSupplier ).apply( threadRunnableCaptor.capture() );
      threadRunnableCaptor.getValue().run();
      verify( processing ).fetchJobsAndUpdateDetails();
   }//End Method
   
   @Test public void shouldStartPollingWhenInitializerComplete(){
      assertThat( systemUnderTest.jobUpdater(), is( nullValue() ) );
      assertThat( systemUnderTest.buildProgressor(), is( nullValue() ) );
      
      systemUnderTest.systemReady();
      assertThat( systemUnderTest.jobUpdater(), is( notNullValue() ) );
      assertThat( systemUnderTest.buildProgressor(), is( notNullValue() ) );
      
      assertThat( systemUnderTest.jobUpdater().isAssociatedWith( processing ), is( true ) );
      assertThat( systemUnderTest.jobUpdater().getInterval(), is( JttInitializer.UPDATE_DELAY ) );
      
      assertThat( systemUnderTest.buildProgressor().isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.buildProgressor().getInterval(), is( JttInitializer.PROGRESS_DELAY ) );
   }//End Method
   
   @Test public void shouldSetLaunchOptionsOnWindowWhenReady(){
      systemUnderTest.systemReady();
      verify( window, times( 2 ) ).setContent( contentCaptor.capture() );
      
      assertThat( contentCaptor.getAllValues(), hasSize( 2 ) );
      assertThat( contentCaptor.getAllValues().get( 1 ), is( instanceOf( LaunchOptions.class ) )  );
      LaunchOptions launchOptions = ( LaunchOptions ) contentCaptor.getValue();
      assertThat( launchOptions.isAssociatedWith( digestViewer ), is( true ) );
      assertThat( launchOptions.isAssociatedWith( database ), is( true ) );
      assertThat( launchOptions.isAssociatedWith( configuration ), is( true ) );
      assertThat( launchOptions.isAssociatedWith( window ), is( true ) );
   }//End Method
   
   @Test public void digestConnectionShouldUseCorrectSource(){
      verify( window ).setContent( contentCaptor.capture() );
      DigestProgressBar bar = ( DigestProgressBar ) contentCaptor.getValue();
      
      DigestProgressReceiverImpl receiver = ( DigestProgressReceiverImpl ) systemUnderTest.digestConnection();
      
      receiver.progress( new SourceImpl( this ), Progresses.simpleProgress( 50 ), Messages.simpleMessage( "" ) );
      assertThat( bar.getProgress(), is( -1.0 ) );
      
      receiver.progress( new SourceImpl( processing ), Progresses.simpleProgress( 50 ), Messages.simpleMessage( "" ) );
      assertThat( bar.getProgress(), is( 0.5 ) );
   }//End Method
   
   @Test public void shouldInstructSystemReadyWhenProgressComplete(){
      Source source = mock( Source.class );
      Progress progress = mock( Progress.class );
      Message message = mock( Message.class );
      
      when( progress.isComplete() ).thenReturn( true );
      DigestProgressReceiverImpl receiver = ( DigestProgressReceiverImpl ) systemUnderTest.digestConnection();
      receiver.progress( source, progress, message );
      
      assertThat( receiver.isConnected(), is( false ) );
   }//End Method
   
   @Test public void shouldDisconnectWhenSystemReady(){
      assertThat( systemUnderTest.digestConnection().isConnected(), is( true ) );
      systemUnderTest.systemReady();
      assertThat( systemUnderTest.digestConnection().isConnected(), is( false ) );
   }//End Method
   
}//End Class
