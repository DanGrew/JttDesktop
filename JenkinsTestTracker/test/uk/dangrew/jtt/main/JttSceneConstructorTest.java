/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.main;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.Scene;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.main.digest.SystemDigestController;

/**
 * {@link JttSceneConstructor} test.
 */
public class JttSceneConstructorTest {

   @Mock JttApplicationController controller;
   @Mock SystemDigestController digestController;
   private JttSceneConstructor systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JttSceneConstructor( controller, digestController );
   }//End Method
   
   @Test public void shouldReturnIfCantLogin() {
      when( controller.login( Mockito.any() ) ).thenReturn( false );
      assertThat( systemUnderTest.makeScene(), is( nullValue() ) );
      verify( controller, times( 1 ) ).login( Mockito.any() );
   }// End Method

   @Test public void shouldProvideEnvironmentWindow() {
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      
      Scene scene = systemUnderTest.makeScene();
      verify( controller, times( 1 ) ).login( Mockito.any() );

      assertThat( scene.getRoot(), instanceOf( EnvironmentWindow.class ) );
      assertThat( scene.getAccelerators().isEmpty(), is( true ) );
   }// End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowRecallOfConstruction(){
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      systemUnderTest.makeScene();
      systemUnderTest.makeScene();
   }//End Method
   
   @Test public void shouldConstructBuildWallSessions(){
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      systemUnderTest.makeScene();
      assertThat( systemUnderTest.buildWallSessions(), is( notNullValue() ) );
      assertThat( systemUnderTest.buildWallSessions().usesConfiguration( 
               systemUnderTest.configuration().getLeftConfiguration(),
               systemUnderTest.configuration().getRightConfiguration() 
      ), is( true ) );
   }//End Method

}//End Class
