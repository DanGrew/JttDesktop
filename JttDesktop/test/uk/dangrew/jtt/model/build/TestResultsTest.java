/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.build;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class TestResultsTest {

   private static final int FAILURES = 234;
   private static final int SKIPPED = 20;
   private static final int TOTAL = 30987;
   
   private TestResults systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new TestResults( FAILURES, SKIPPED, TOTAL );
   }//End Method

   @Test public void shouldProvideFailures() {
      assertThat( systemUnderTest.failures(), is( FAILURES ) );
      assertThat( systemUnderTest.failures(), is( FAILURES ) );
   }//End Method
   
   @Test public void shouldProvideSkipped() {
      assertThat( systemUnderTest.skipped(), is( SKIPPED ) );
      assertThat( systemUnderTest.skipped(), is( SKIPPED ) );
   }//End Method
   
   @Test public void shouldProvideTotal() {
      assertThat( systemUnderTest.total(), is( TOTAL ) );
      assertThat( systemUnderTest.total(), is( TOTAL ) );
   }//End Method

}//End Class
