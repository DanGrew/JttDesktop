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
      new TimeKeeper( fetcher, 1 );
      Mockito.verify( fetcher, Mockito.atLeast( 1 ) ).fetchJobs();;
      Thread.sleep( 10 );
      Mockito.verify( fetcher, Mockito.atLeast( 8 ) ).fetchJobs();
   }//End Method
   
   @Test public void shouldRunTaskAtLeastOnceBeforeAndOnceOnInterval() throws InterruptedException{
      new TimeKeeper( fetcher, 1000 );
      Thread.sleep( 1000 );
      Mockito.verify( fetcher, Mockito.times( 2 ) ).fetchJobs();
   }//End Method
   
   @Test public void shouldRecordSomeInteractionsWhenIntervalIsChanged() throws InterruptedException{
      TimeKeeper systemUnderTest = new TimeKeeper( fetcher, 1000 );
      Mockito.verify( fetcher, Mockito.times( 1 ) ).fetchJobs();
      systemUnderTest.setInterval( 2000 );
      Mockito.verify( fetcher, Mockito.times( 1 ) ).fetchJobs();
      systemUnderTest.setInterval( 1 );
      Thread.sleep( 10 );
      Mockito.verify( fetcher, Mockito.atLeast( 10 ) ).fetchJobs();
   }//End Method

}//End Class
