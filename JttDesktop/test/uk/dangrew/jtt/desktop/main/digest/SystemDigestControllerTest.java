/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.main.digest;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.utility.time.TimestampProvider;
import uk.dangrew.kode.launch.TestApplication;
import uk.dangrew.sd.logging.location.LoggingLocationProtocol;
import uk.dangrew.sd.logging.logger.DigestFileLogger;
import uk.dangrew.sd.table.presentation.DigestTableRowLimit;
import uk.dangrew.sd.utility.threading.ThreadedWrapper;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * {@link SystemDigestController} test.
 */
public class SystemDigestControllerTest {

   @Captor private ArgumentCaptor< LoggingLocationProtocol > protocolCaptor;
   @Mock private ThreadedWrapper wrapper;
   @Mock private DigestFileLogger logger;
   @Mock private DigestViewer digestViewer;
   private TimestampProvider timestampProvider;
   private SystemDigestController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      TestApplication.startPlatform();
      timestampProvider = () -> LocalDateTime.MIN;
      systemUnderTest = new SystemDigestController( timestampProvider, wrapper, logger, digestViewer );
   }//End Method
   
   @Test public void shouldProvideDigestViewer() {
      systemUnderTest = new SystemDigestController( timestampProvider, wrapper, logger, digestViewer );
      assertThat( systemUnderTest.getDigestViewer(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldWrapLoggerWithIndefinteRun() {
      verify( wrapper ).wrap( logger, -1 );
   }//End Method
   
   @Test public void shouldAssociateFileLocation(){
      verify( logger ).setFileLocation( protocolCaptor.capture() );
      
      assertThat( systemUnderTest.getLoggingLocation(), is( protocolCaptor.getValue().getLocation() ) );
      assertThat( systemUnderTest.getLoggingLocation(), containsString( 
               SystemDigestController.FOLDER_NAME + "/" + systemUnderTest.makeFilePrefix( timestampProvider.get() ) + SystemDigestController.LOG_FILE_SUFFIX 
      ) );
      assertThat( protocolCaptor.getValue().getFileSizeLimit(), is( SystemDigestController.FILE_SIZE_LIMIT ) );
   }//End Method
   
   @Test public void shouldMakeFilePrefixCompatibleWithAllOperatingSystems(){
      assertThat( systemUnderTest.makeFilePrefix( timestampProvider.get() ), is( "-999999999-01-01at00-00" ) );
      String current = systemUnderTest.makeFilePrefix( LocalDateTime.now() );
      assertThat( current, containsString( "at" ) );
      assertThat( current, not( containsString( ":" ) ) );
   }//End Method
   
   @Test public void shouldConfigureLimitOnDigestViewer(){
      verify( digestViewer ).setTableRowLimit( DigestTableRowLimit.OneThousand );
   }//End Method

}//End Class
