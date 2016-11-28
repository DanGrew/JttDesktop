/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.sd.core.message.Message;
import uk.dangrew.sd.core.progress.Progress;
import uk.dangrew.sd.core.source.Source;
import uk.dangrew.sd.progressbar.model.DigestProgressBar;

public class InitialSynchronizationReceiverTest {

   @Mock private Source source;
   @Mock private Message message;
   @Mock private Progress progress;
   
   @Mock private DigestProgressBar progressBar;
   @Mock private JttInitializer initializer;
   private InitialSynchronizationReceiver systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new InitialSynchronizationReceiver( initializer, progressBar );
   }//End Method

   @Test public void shouldForwardProgressInAllCases() {
      systemUnderTest.progress( source, progress, message );
      verify( progressBar ).handleProgress( source, progress, message );
      
      systemUnderTest.progress( source, progress, message );
      verify( progressBar, times( 2 ) ).handleProgress( source, progress, message );
   }//End Method
   
   @Test public void shouldReturnControlToInitializerWhenComplete() {
      when( progress.isComplete() ).thenReturn( true );
      systemUnderTest.progress( source, progress, message );
      verify( progressBar ).handleProgress( source, progress, message );
      verify( initializer ).systemReady();
   }//End Method

}//End Class
