/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.main;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



import javafx.scene.Scene;
import uk.dangrew.jtt.connection.api.JenkinsApiConnectionPrompt;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.desktop.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.main.digest.SystemDigestController;
import uk.dangrew.kode.javafx.platform.JavaFxThreading;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * {@link JttSceneConstructor} test.
 */
public class JttSceneConstructorTest {

   @Mock private ExternalApi api;
   @Mock private JenkinsApiConnectionPrompt connector;
   @Mock private SystemDigestController digestController;
   @Mock private DigestViewer digest;
   private JttSceneConstructor systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      when( digestController.getDigestViewer() ).thenReturn( digest );
      systemUnderTest = new JttSceneConstructor( a -> Mockito.mock( Scene.class ), digestController, connector );
   }//End Method
   
   @Test public void shouldReturnIfCantLogin() {
      when( connector.connect( digest ) ).thenReturn( false );
      assertThat( systemUnderTest.makeScene(), is( nullValue() ) );
   }// End Method

   @Test public void shouldProvideEnvironmentWindow() {
      when( connector.connect( digest ) ).thenReturn( true );
      
      systemUnderTest = new JttSceneConstructor( Scene::new, digestController, connector );
       JavaFxThreading.runAndWait( () -> {
         Scene scene = systemUnderTest.makeScene();
         assertThat( scene.getRoot(), instanceOf( EnvironmentWindow.class ) );
         assertThat( scene.getAccelerators().isEmpty(), is( true ) );
      } );
      assertThat( systemUnderTest.initializer(), is( notNullValue() ) );
   }// End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowRecallOfConstruction(){
      when( connector.connect( digest ) ).thenReturn( true );
      systemUnderTest.makeScene();
      systemUnderTest.makeScene();
   }//End Method
   
   @Test public void shouldConstructBuildWallSessions(){
      when( connector.connect( digest ) ).thenReturn( true );
      systemUnderTest.makeScene();
      assertThat( systemUnderTest.buildWallSessions(), is( notNullValue() ) );
      assertThat( systemUnderTest.buildWallSessions().uses( 
               systemUnderTest.configuration().getLeftConfiguration(),
               systemUnderTest.configuration().getRightConfiguration() 
      ), is( true ) );
      assertThat( systemUnderTest.buildWallSessions().uses( systemUnderTest.database() ), is( true ) );
   }//End Method
   
   @Test public void shouldConstructSoundSessions(){
      when( connector.connect( digest ) ).thenReturn( true );
      systemUnderTest.makeScene();
      assertThat( systemUnderTest.soundSessions(), is( notNullValue() ) );
      assertThat( systemUnderTest.soundSessions().uses( systemUnderTest.configuration().getSoundConfiguration() ), is( true ) );
      assertThat( systemUnderTest.soundSessions().uses( systemUnderTest.database() ), is( true ) );
   }//End Method
   
   @Test public void shouldConstructStatisticsSessions(){
      when( connector.connect( digest ) ).thenReturn( true );
      systemUnderTest.makeScene();
      assertThat( systemUnderTest.statisticsSessions(), is( notNullValue() ) );
      assertThat( systemUnderTest.statisticsSessions().uses( systemUnderTest.configuration().getStatisticsConfiguration() ), is( true ) );
   }//End Method
}//End Class
