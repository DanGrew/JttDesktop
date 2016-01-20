/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package synchronisation.time;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import api.handling.JenkinsFetcher;

/**
 * {@link TimeKeeper} test.
 */
public class TimeKeeperTest {
   
   private JenkinsFetcher fetcher;
   
   @Before public void initialiseSystemUnderTest(){
      fetcher = Mockito.mock( JenkinsFetcher.class );
   }//End Method
   
   @Test public void shouldRecordSomeInteractionsWhenRunQuickly() throws InterruptedException{
      new TimeKeeper( fetcher, 1l );
      Thread.sleep( 10 );
      Mockito.verify( fetcher, Mockito.atLeast( 5 ) ).fetchJobsAndUpdateDetails();
   }//End Method
   
   @Test public void shouldRunTaskAtLeastOnceBeforeAndOnceOnInterval() throws InterruptedException{
      new TimeKeeper( fetcher, 100l );
      Thread.sleep( 150 );
      Mockito.verify( fetcher, Mockito.times( 2 ) ).fetchJobsAndUpdateDetails();
   }//End Method
   
   @Test public void shouldRecordSomeInteractionsWhenIntervalIsChanged() throws InterruptedException{
      TimeKeeper systemUnderTest = new TimeKeeper( fetcher, 1000l );
      systemUnderTest.setInterval( 2000l );
      systemUnderTest.setInterval( 1l );
      Thread.sleep( 10 );
      Mockito.verify( fetcher, Mockito.atLeast( 5 ) ).fetchJobsAndUpdateDetails();
   }//End Method
   
   @Test public void shouldPollOnRequest(){
      TimeKeeper time = new TimeKeeper( fetcher, null );
      time.poll();
      Mockito.verify( fetcher ).fetchJobsAndUpdateDetails();
   }//End Method
   
   @Test public void shouldNotScheduleIfIntervalNull(){
      new TimeKeeper( fetcher, null );
      Mockito.verifyZeroInteractions( fetcher );
   }//End Method
   
   @Test public void setNullIntervalShouldCancelTimer() throws InterruptedException{
      TimeKeeper time = new TimeKeeper( fetcher, 5l );
      Mockito.verify( fetcher, Mockito.atLeastOnce() ).fetchJobsAndUpdateDetails();
      time.setInterval( null );
      Thread.sleep( 100 );
      Mockito.verify( fetcher, Mockito.atMost( 10 ) ).fetchJobsAndUpdateDetails();
   }//End Method

}//End Class
