/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.comparator;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.utility.comparator.Comparators;

/**
 * {@link Comparators} test.
 */
public class ComparatorsTest {

   @Test public void shouldStringCompare(){
      Assert.assertEquals( 0, Comparators.compare( "me", "me" ) );
      Assert.assertEquals( 0, Comparators.compare( "something really long", "something really long" ) );
      Assert.assertEquals( 0, Comparators.compare( ( String )null, ( String )null ) );
      
      assertThat( Comparators.compare( "me", "you" ), lessThan( 0 ) );
      assertThat( Comparators.compare( null, "you" ), lessThan( 0 ) );
      
      assertThat( Comparators.compare( "you", "me" ), greaterThan( 0 ) );
      assertThat( Comparators.compare( "you", null ), greaterThan( 0 )  );
   }// End Method
   
   @Test public void shouldNumberCompare(){
      Assert.assertEquals( 0, Comparators.compare( "2345", "2345" ) );
      Assert.assertEquals( 0, Comparators.compare( "0.84953847", "0.84953847" ) );
      Assert.assertEquals( 0, Comparators.compare( ( Double )null, ( Double )null ) );
      
      assertThat( Comparators.compare( "5", "8" ), lessThan( 0 ) );
      assertThat( Comparators.compare( null, "8" ), lessThan( 0 ) );
      
      assertThat( Comparators.compare( "56", "-34" ), greaterThan( 0 ) );
      assertThat( Comparators.compare( "56", null ), greaterThan( 0 ) );
      
      assertThat( Comparators.compare( 20d, 20d ), is( 0 ) );
      assertThat( Comparators.compare( 100d, 20d ), is( 1 ) );
      assertThat( Comparators.compare( 20d, 100d ), is( -1 ) );
   }// End Method
   
   @Test public void shouldConstructComparatorForStringExtraction(){
      Comparator< JenkinsJob > comparator = Comparators.stringExtractionComparater( job -> { return job.nameProperty().get(); } );
      
      JenkinsJob first = new JenkinsJobImpl( "first" );
      JenkinsJob second = new JenkinsJobImpl( "second" );
      JenkinsJob last = new JenkinsJobImpl( "xlast" );
      
      assertThat( comparator.compare( first, second ), lessThan( 0 ) );
      assertThat( comparator.compare( second, last ), lessThan( 0 ) );
      assertThat( comparator.compare( last, second ), greaterThan( 0 ) );
      assertThat( comparator.compare( second, second ), is( 0 ) );
   }//End Method
   
   @Test public void shouldCompareNullValues(){
      assertThat( Comparators.compareForNullValues( null, null, true ), is( 0 ) );
      assertThat( Comparators.compareForNullValues( null, null, false ), is( 0 ) );
      
      assertThat( Comparators.compareForNullValues( 20, null, false ), is( -1 ) );
      assertThat( Comparators.compareForNullValues( null, 20, true ), is( -1 ) );
      
      assertThat( Comparators.compareForNullValues( null, 20, false ), is( 1 ) );
      assertThat( Comparators.compareForNullValues( 20, null, true ), is( 1 ) );
      
      assertThat( Comparators.compareForNullValues( 20, 100, false ), nullValue() );
   }//End Method
   
   @Test public void shouldReverseCompare(){
      @SuppressWarnings("unchecked") //simply mocking genericized objects. 
      Comparator< String > comparator = mock( Comparator.class );
      Comparator< String > reverse = Comparators.reverseComparator( comparator );

      final String A = "a";
      final String B = "b";
      
      reverse.compare( A, B );
      verify( comparator ).compare( B, A );
      
      reverse.compare( B, A );
      verify( comparator ).compare( A, B );
      
      reverse.compare( A, A );
      verify( comparator ).compare( A, A );
   }//End Method
   
}// End Class
