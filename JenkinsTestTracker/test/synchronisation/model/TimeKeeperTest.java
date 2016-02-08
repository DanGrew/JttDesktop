/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package synchronisation.model;

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

import synchronisation.model.TimeKeeper;

/**
 * {@link TimeKeeper} test.
 */
public class TimeKeeperTest {
   
   @Captor private ArgumentCaptor< TimerTask > timerTaskCaptor;
   @Captor private ArgumentCaptor< Long > delayCaptor;
   @Captor private ArgumentCaptor< Long > intervalCaptor;
   
   @Mock private Timer timer;
   @Mock private Runnable runnable;
   private Long interval;
   private TimeKeeper systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      interval = 1000l;
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new TimeKeeper( timer, runnable, interval );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void constructorShouldRejectNullTimer(){
      new TimeKeeper( null, runnable, interval );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void constructorShouldRejectNullRunnable(){
      new TimeKeeper( timer, null, interval );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void constructorShouldRejectNullRunnableIndependently(){
      new TimeKeeper( null );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void constructorShouldRejectNullInterval(){
      new TimeKeeper( timer, runnable, null );
   }//End Method
   
   @Test public void shouldScheduleTimerTaskOnTimer(){
      Mockito.verify( timer ).schedule( 
               timerTaskCaptor.capture(), 
               delayCaptor.capture(),
               intervalCaptor.capture() 
      );
      
      Assert.assertEquals( 1, delayCaptor.getAllValues().size() );
      Assert.assertEquals( TimeKeeper.TASK_DELAY, delayCaptor.getValue().longValue() );
      
      Assert.assertEquals( 1, intervalCaptor.getAllValues().size() );
      Assert.assertEquals( interval, intervalCaptor.getValue() );
      
      Assert.assertEquals( 1, timerTaskCaptor.getAllValues().size() );
      Assert.assertNotNull( timerTaskCaptor.getValue() );
      Mockito.verifyZeroInteractions( runnable );
      
      timerTaskCaptor.getValue().run();
      Mockito.verify( runnable ).run();
   }//End Method
   
   @Test public void pollShouldRunRunnable(){
      Mockito.verifyZeroInteractions( runnable );
      systemUnderTest.poll();
      Mockito.verify( runnable ).run();
   }//End Method
   
}//End Class
