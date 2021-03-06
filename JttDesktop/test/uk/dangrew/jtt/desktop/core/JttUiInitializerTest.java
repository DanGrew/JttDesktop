/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.desktop.environment.launch.LaunchOptions;
import uk.dangrew.jtt.desktop.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.sd.viewer.basic.DigestViewer;

public class JttUiInitializerTest {

   @Spy private JavaFxStyle styling;
   private JenkinsDatabase database;
   @Mock private EnvironmentWindow window;
   @Mock private SystemConfiguration configuration;
   @Mock private DigestViewer digestViewer;
   
   @Captor private ArgumentCaptor< Node > contentCaptor;
   
   private JttUiInitializer systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      TestApplication.startPlatform();
      database = new TestJenkinsDatabaseImpl();
      systemUnderTest = new JttUiInitializer( styling, database, window, digestViewer, configuration );;
   }//End Method

   @Test public void shouldSetProgressAsWindowContent() {
      systemUnderTest.beginInitializing();
      verify( window ).setContent( contentCaptor.capture() );
      
      Node captured = contentCaptor.getValue();
      assertThat( captured, is( instanceOf( BorderPane.class ) ) );
      BorderPane pane = ( BorderPane ) captured;
      ProgressIndicator progress = ( ProgressIndicator ) pane.getCenter();
      assertThat( progress.getProgress(), is( -1.0 ) );
      
      Label label = ( Label ) pane.getBottom();
      assertThat( label.getText(), is( JttUiInitializer.LOADING_JENKINS_JOBS ) );
      verify( styling ).createBoldLabel( JttUiInitializer.LOADING_JENKINS_JOBS );
   }//End Method
   
   @Test public void shouldSetLaunchOptionsOnWindowWhenReady(){
      systemUnderTest.systemReady();
      verify( window ).setContent( contentCaptor.capture() );
      
      assertThat( contentCaptor.getAllValues(), hasSize( 1 ) );
      assertThat( contentCaptor.getAllValues().get( 0 ), is( instanceOf( LaunchOptions.class ) )  );
      LaunchOptions launchOptions = ( LaunchOptions ) contentCaptor.getValue();
      assertThat( launchOptions.isAssociatedWith( digestViewer ), is( true ) );
      assertThat( launchOptions.isAssociatedWith( database ), is( true ) );
      assertThat( launchOptions.isAssociatedWith( configuration ), is( true ) );
      assertThat( launchOptions.isAssociatedWith( window ), is( true ) );
   }//End Method

}//End Class
