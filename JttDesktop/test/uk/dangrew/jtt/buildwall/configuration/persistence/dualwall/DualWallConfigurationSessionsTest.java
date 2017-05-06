/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.dualwall;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationSessions.DUAL_WALL_FILE_NAME;
import static uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationSessions.FOLDER_NAME;

import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.geometry.Orientation;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfigurationImpl;
import uk.dangrew.jtt.main.JenkinsTestTracker;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;

/**
 * {@link DualWallConfigurationSessions} test.
 */
public class DualWallConfigurationSessionsTest {

   @Mock private JenkinsDatabase database;
   @Mock private JarJsonPersistingProtocol dualProtocol;
   @Captor private ArgumentCaptor< JSONObject > objectCaptor;
   
   private DualWallConfigurationSessions systemUnderTest;
   private DualWallConfiguration dualConfiguration;
   
   private CountDownLatch latch;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
      dualConfiguration = new DualWallConfigurationImpl();
      
      systemUnderTest = new DualWallConfigurationSessions( dualConfiguration, dualProtocol );
      
      latch = new CountDownLatch( 1 );
      when( dualProtocol.writeToLocation( Mockito.any() ) ).thenAnswer( invocation -> { latch.countDown(); return true; } );
   }//End Method
   
   @After public void tearDown(){
      systemUnderTest.shutdownSessions();
   }//End Method
   
   @Test public void publicConstructorShouldDefineCorrectFileLocations(){
      systemUnderTest = new DualWallConfigurationSessions( dualConfiguration );
      assertThat( 
               systemUnderTest.dualConfigurationFileLocation().getLocation(), 
               is( new JarJsonPersistingProtocol( FOLDER_NAME, DUAL_WALL_FILE_NAME, JenkinsTestTracker.class ).getLocation() ) 
      );
   }//End Method
   
   @Test public void shouldReadRightAndLeftOnConstruction(){
      verify( dualProtocol ).readFromLocation();
   }//End Method
   
   @Test public void shutdownShouldStopWriting(){
      systemUnderTest.shutdownSessions();
      
      dualConfiguration.dividerOrientationProperty().set( Orientation.VERTICAL );
      
      verify( dualProtocol, never() ).writeToLocation( Mockito.any() );
   }//End Method
   
   @Test public void dualShouldTriggerWriteWhenOrientationChanged() throws InterruptedException {
      dualConfiguration.dividerOrientationProperty().set( Orientation.VERTICAL );
      latch.await();
      
      verify( dualProtocol ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"Orientation\":\"VERTICAL\"" ) );
      
      verifyReadAlwaysHappensBeforeWrite();
   }
   
   @Test public void dualShouldTriggerWriteWhenPositionChanged() throws InterruptedException {
      dualConfiguration.dividerPositionProperty().set( 0.67 );
      latch.await();
      
      verify( dualProtocol ).writeToLocation( objectCaptor.capture() );
      assertThat( objectCaptor.getValue().toString(), containsString( "\"Positon\":0.67" ) );
      
      verifyReadAlwaysHappensBeforeWrite();
   }//End Method
   
   /**
    * Method to verify that read always happens before write on the given {@link JarJsonPersistingProtocol}.
    */
   private void verifyReadAlwaysHappensBeforeWrite(){
      InOrder order = inOrder( dualProtocol );
      order.verify( dualProtocol ).readFromLocation();
      order.verify( dualProtocol ).writeToLocation( Mockito.any() );
   }//End Method
   
}//End Class
