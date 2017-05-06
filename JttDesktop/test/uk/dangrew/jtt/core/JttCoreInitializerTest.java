/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.api.handling.live.LiveStateFetcher;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

public class JttCoreInitializerTest {

   @Mock private LiveStateFetcher fetcher;
   private JenkinsDatabase database;
   @Mock private JttSystemInitialization systemInitialization;
   
   @Mock private Thread thread;
   @Mock private Function< Runnable, Thread > threadSupplier;
   @Captor private ArgumentCaptor< Runnable > threadRunnableCaptor;
   
   private JttCoreInitializer systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      database = new TestJenkinsDatabaseImpl();
      when( threadSupplier.apply( Mockito.any() ) ).thenReturn( thread );
      systemUnderTest = new JttCoreInitializer( threadSupplier, fetcher, database, systemInitialization );
   }//End Method
   
   @Test public void shouldStartSystemInitialization(){
      verify( systemInitialization ).beginInitializing();
   }//End Method

   @Test public void shouldConstructThreadStartAndInformSystemReady(){
      verify( thread ).start();
      
      verify( threadSupplier ).apply( threadRunnableCaptor.capture() );
      threadRunnableCaptor.getValue().run();
      verify( fetcher ).loadLastCompletedBuild();
      assertThat( systemUnderTest.jobUpdater(), is( notNullValue() ) );
      assertThat( systemUnderTest.buildProgressor(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldStartPollingWhenInitializerComplete(){
      assertThat( systemUnderTest.jobUpdater(), is( nullValue() ) );
      assertThat( systemUnderTest.buildProgressor(), is( nullValue() ) );
      
      systemUnderTest.systemReady();
      assertThat( systemUnderTest.jobUpdater(), is( notNullValue() ) );
      assertThat( systemUnderTest.buildProgressor(), is( notNullValue() ) );
      
      assertThat( systemUnderTest.jobUpdater().isAssociatedWith( fetcher ), is( true ) );
      assertThat( systemUnderTest.jobUpdater().getInterval(), is( JttCoreInitializer.UPDATE_DELAY ) );
      
      assertThat( systemUnderTest.buildProgressor().isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.buildProgressor().getInterval(), is( JttCoreInitializer.PROGRESS_DELAY ) );
   }//End Method
   
   @Test public void shouldReadySystemInitializer(){
      systemUnderTest.systemReady();
      verify( systemInitialization ).systemReady();
   }//End Method
   
}//End Class
