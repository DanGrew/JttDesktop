/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

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

import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.main.digest.SystemDigestController;

public class JenkinsApiConnectorTest {

   @Mock private JttApplicationController applicationController;
   @Mock private SystemDigestController digestController;
   private JenkinsApiConnector systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JenkinsApiConnector( applicationController );
   }//End Method

   @Test public void shouldReturnIfCantLogin() {
      systemUnderTest.connect( digestController );
      
      when( applicationController.login( Mockito.any() ) ).thenReturn( false );
      assertThat( systemUnderTest.connect( digestController ), is( nullValue() ) );
   }// End Method
   
   @Test public void shouldReturnApiIfLoggedIn() {
      systemUnderTest.connect( digestController );
      
      when( applicationController.login( Mockito.any() ) ).thenReturn( true );
      assertThat( systemUnderTest.connect( digestController ), is( notNullValue() ) );
   }// End Method
   
}//End Class
