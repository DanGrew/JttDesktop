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
import org.mockito.Spy;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.api.handling.live.LiveStateFetcher;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.launch.LaunchOptions;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.sd.viewer.basic.DigestViewer;

public class JttInitializerTest {

   @Spy private JavaFxStyle styling;
   @Mock private LiveStateFetcher fetcher;
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
      systemUnderTest = new JttInitializer( styling, threadSupplier, fetcher, database, window, configuration, digestViewer );
   }//End Method

   @Test public void shouldSetProgressAsWindowContent() {
      verify( window ).setContent( contentCaptor.capture() );
      
      Node captured = contentCaptor.getValue();
      assertThat( captured, is( instanceOf( BorderPane.class ) ) );
      BorderPane pane = ( BorderPane ) captured;
      ProgressIndicator progress = ( ProgressIndicator ) pane.getCenter();
      assertThat( progress.getProgress(), is( -1.0 ) );
      
      Label label = ( Label ) pane.getBottom();
      assertThat( label.getText(), is( JttInitializer.LOADING_JENKINS_JOBS ) );
      verify( styling ).createBoldLabel( JttInitializer.LOADING_JENKINS_JOBS );
   }//End Method
   
   @Test public void shouldConstructThreadStartAndInformSystemReady(){
      verify( thread ).start();
      
      verify( threadSupplier ).apply( threadRunnableCaptor.capture() );
      threadRunnableCaptor.getValue().run();
      verify( fetcher ).loadLastCompletedBuild();
      assertThat( systemUnderTest.jobUpdater(), is( notNullValue() ) );
      assertThat( systemUnderTest.buildProgressor(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldStartPollingWhenInitializerComplete(){
      assertThat( systemUnderTest.jobUpdater(), is( nullValue() ) );
      assertThat( systemUnderTest.buildProgressor(), is( nullValue() ) );
      
      systemUnderTest.systemReady();
      assertThat( systemUnderTest.jobUpdater(), is( notNullValue() ) );
      assertThat( systemUnderTest.buildProgressor(), is( notNullValue() ) );
      
      assertThat( systemUnderTest.jobUpdater().isAssociatedWith( fetcher ), is( true ) );
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
   
}//End Class
