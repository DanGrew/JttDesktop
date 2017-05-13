/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.BiFunction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.login.JenkinsLoginPrompt;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.sd.graphics.launch.TestApplication;
import uk.dangrew.sd.viewer.basic.DigestViewer;

public class JenkinsApiConnectorTest {

   @Mock private ExternalApi api;
   @Mock private DigestViewer digestViewer;
   @Mock private JenkinsLoginPrompt prompt;
   @Mock private BiFunction< ExternalApi, DigestViewer, JenkinsLoginPrompt > promptSupplier;
   private JenkinsApiConnector systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      when( promptSupplier.apply( api, digestViewer ) ).thenReturn( prompt );
      systemUnderTest = new JenkinsApiConnector( 
               promptSupplier,
               api
      );
   }//End Method

   @Test public void shouldReturnIfCantLogin() {
      when( prompt.friendly_showAndWait() ).thenReturn( Optional.of( false ) );
      assertThat( systemUnderTest.connect( digestViewer ), is( nullValue() ) );
   }// End Method
   
   @Test public void shouldReturnApiIfLoggedIn() {
      when( prompt.friendly_showAndWait() ).thenReturn( Optional.of( true ) );
      assertThat( systemUnderTest.connect( digestViewer ), is( api ) );
   }// End Method
   
}//End Class
