/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.synchronisation.time;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.api.handling.live.LiveStateFetcher;

/**
 * {@link JobUpdater} test.
 */
public class JobUpdaterTest {

   @Captor private ArgumentCaptor< TimerTask > timerTaskCaptor;
   @Captor private ArgumentCaptor< Long > delayCaptor;
   @Captor private ArgumentCaptor< Long > intervalCaptor;
   
   @Mock private Timer timer;
   @Mock private LiveStateFetcher fetcher;
   private Long interval;
   private JobUpdater systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      interval = 1000l;
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JobUpdater( fetcher, timer, interval );
   }//End Method
   
   @Test public void shouldScheduleTimerTaskOnTimer(){
      Mockito.verify( timer ).schedule( 
               timerTaskCaptor.capture(), 
               delayCaptor.capture(),
               intervalCaptor.capture() 
      );
      
      Assert.assertEquals( 1, delayCaptor.getAllValues().size() );
      Assert.assertEquals( JobUpdater.UPDATE_DELAY, delayCaptor.getValue().longValue() );
      
      Assert.assertEquals( 1, intervalCaptor.getAllValues().size() );
      Assert.assertEquals( interval, intervalCaptor.getValue() );
      
      Assert.assertEquals( 1, timerTaskCaptor.getAllValues().size() );
      Assert.assertNotNull( timerTaskCaptor.getValue() );
      Mockito.verifyZeroInteractions( fetcher );
      
      timerTaskCaptor.getValue().run();
      Mockito.verify( fetcher ).updateBuildState();
   }//End Method
   
   @Test public void pollShouldRunRunnable(){
      Mockito.verifyZeroInteractions( fetcher );
      systemUnderTest.poll();
      Mockito.verify( fetcher ).updateBuildState();
   }//End Method

   @Test public void manualConstructorPollShouldRunRunnable(){
      Mockito.verifyZeroInteractions( fetcher );
      systemUnderTest = new JobUpdater( fetcher, null, null );
      systemUnderTest.poll();
      Mockito.verify( fetcher ).updateBuildState();
   }//End Method
   
   @Test public void shouldBeAssociatedWith(){
      assertThat( systemUnderTest.isAssociatedWith( fetcher ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( mock( LiveStateFetcher.class ) ), is( false ) );
   }//End Method

}//End Class
