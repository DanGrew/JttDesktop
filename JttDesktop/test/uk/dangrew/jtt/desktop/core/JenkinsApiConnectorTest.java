/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.core.JenkinsApiConnector;
import uk.dangrew.jtt.desktop.core.JttApplicationController;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.main.digest.SystemDigestController;
import uk.dangrew.sd.viewer.basic.DigestViewer;

public class JenkinsApiConnectorTest {

   @Mock private JttApplicationController applicationController;
   @Mock private DigestViewer digestViewer;
   private JenkinsApiConnector systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JenkinsApiConnector( applicationController );
   }//End Method

   @Test public void shouldReturnIfCantLogin() {
      systemUnderTest.connect( digestViewer );
      
      when( applicationController.login( Mockito.any() ) ).thenReturn( false );
      assertThat( systemUnderTest.connect( digestViewer ), is( nullValue() ) );
   }// End Method
   
   @Test public void shouldReturnApiIfLoggedIn() {
      systemUnderTest.connect( digestViewer );
      
      when( applicationController.login( Mockito.any() ) ).thenReturn( true );
      assertThat( systemUnderTest.connect( digestViewer ), is( notNullValue() ) );
   }// End Method
   
}//End Class
