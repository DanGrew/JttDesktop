/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.kode.launch.TestApplication;

import java.util.function.Function;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JttCoreInitializerTest {

   private JenkinsDatabase database;
   @Mock private JttSystemInitialization systemInitialization;
   
   @Mock private Thread thread;
   @Mock private Function< Runnable, Thread > threadSupplier;
   @Captor private ArgumentCaptor< Runnable > threadRunnableCaptor;
   
   private JttCoreInitializer systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      database = new TestJenkinsDatabaseImpl();
      when( threadSupplier.apply( Mockito.any() ) ).thenReturn( thread );
      systemUnderTest = new JttCoreInitializer( threadSupplier, database, systemInitialization );
   }//End Method
   
   @Test public void shouldStartSystemInitialization(){
      verify( systemInitialization ).beginInitializing();
   }//End Method

   @Test public void shouldConstructThreadStartAndInformSystemReady(){
      verify( thread ).start();
      
      verify( threadSupplier ).apply( threadRunnableCaptor.capture() );
      threadRunnableCaptor.getValue().run();
      assertThat( systemUnderTest.buildProgressor(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldStartPollingWhenInitializerComplete(){
      assertThat( systemUnderTest.buildProgressor(), is( nullValue() ) );
      
      systemUnderTest.systemReady();
      assertThat( systemUnderTest.buildProgressor(), is( notNullValue() ) );
      
      assertThat( systemUnderTest.buildProgressor().isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.buildProgressor().getInterval(), is( JttCoreInitializer.PROGRESS_DELAY ) );
   }//End Method
   
   @Test public void shouldReadySystemInitializer(){
      systemUnderTest.systemReady();
      verify( systemInitialization ).systemReady();
   }//End Method
   
}//End Class
