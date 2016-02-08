/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package synchronisation.time;

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

import api.handling.JenkinsFetcher;

/**
 * {@link JobUpdater} test.
 */
public class JobUpdaterTest {

   @Captor private ArgumentCaptor< TimerTask > timerTaskCaptor;
   @Captor private ArgumentCaptor< Long > delayCaptor;
   @Captor private ArgumentCaptor< Long > intervalCaptor;
   
   @Mock private Timer timer;
   @Mock private JenkinsFetcher fetcher;
   private Long interval;
   private JobUpdater systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      interval = 1000l;
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JobUpdater( timer, fetcher, interval );
   }//End Method
   
   @Test public void shouldScheduleTimerTaskOnTimer(){
      Mockito.verify( timer ).schedule( 
               timerTaskCaptor.capture(), 
               delayCaptor.capture(),
               intervalCaptor.capture() 
      );
      
      Assert.assertEquals( 1, delayCaptor.getAllValues().size() );
      Assert.assertEquals( JobUpdater.TASK_DELAY, delayCaptor.getValue().longValue() );
      
      Assert.assertEquals( 1, intervalCaptor.getAllValues().size() );
      Assert.assertEquals( interval, intervalCaptor.getValue() );
      
      Assert.assertEquals( 1, timerTaskCaptor.getAllValues().size() );
      Assert.assertNotNull( timerTaskCaptor.getValue() );
      Mockito.verifyZeroInteractions( fetcher );
      
      timerTaskCaptor.getValue().run();
      Mockito.verify( fetcher ).fetchJobsAndUpdateDetails();
   }//End Method
   
   @Test public void pollShouldRunRunnable(){
      Mockito.verifyZeroInteractions( fetcher );
      systemUnderTest.poll();
      Mockito.verify( fetcher ).fetchJobsAndUpdateDetails();
   }//End Method

   @Test public void manualConstructorPollShouldRunRunnable(){
      Mockito.verifyZeroInteractions( fetcher );
      systemUnderTest = new JobUpdater( fetcher );
      systemUnderTest.poll();
      Mockito.verify( fetcher ).fetchJobsAndUpdateDetails();
   }//End Method

}//End Class
