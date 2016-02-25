/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package utility.comparator;

import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import utility.comparator.Comparators;

/**
 * {@link Comparators} test.
 */
public class ComparatorsTest {

   @Test public void shouldStringCompare(){
      Assert.assertEquals( 0, Comparators.compare( "me", "me" ) );
      Assert.assertEquals( 0, Comparators.compare( "something really long", "something really long" ) );
      Assert.assertEquals( 0, Comparators.compare( ( String )null, ( String )null ) );
      
      Assert.assertTrue( Comparators.compare( "me", "you" ) < 0 );
      Assert.assertTrue( Comparators.compare( null, "you" ) < 0 );
      
      Assert.assertTrue( Comparators.compare( "you", "me" ) > 0 );
      Assert.assertTrue( Comparators.compare( "you", null ) > 0 );
   }// End Method
   
   @Test public void shouldNumberCompare(){
      Assert.assertEquals( 0, Comparators.compare( "2345", "2345" ) );
      Assert.assertEquals( 0, Comparators.compare( "0.84953847", "0.84953847" ) );
      Assert.assertEquals( 0, Comparators.compare( ( Double )null, ( Double )null ) );
      
      Assert.assertTrue( Comparators.compare( "5", "8" ) < 0 );
      Assert.assertTrue( Comparators.compare( null, "8" ) < 0 );
      
      Assert.assertTrue( Comparators.compare( "56", "-34" ) > 0 );
      Assert.assertTrue( Comparators.compare( "56", null ) > 0 );
   }// End Method
   
   @Test public void shouldConstructComparatorForStringExtraction(){
      Comparator< JenkinsJob > comparator = Comparators.stringExtractionComparater( job -> { return job.nameProperty().get(); } );
      
      JenkinsJob first = new JenkinsJobImpl( "first" );
      JenkinsJob second = new JenkinsJobImpl( "second" );
      JenkinsJob last = new JenkinsJobImpl( "xlast" );
      
      Assert.assertTrue( comparator.compare( first, second ) < 0 );
      Assert.assertTrue( comparator.compare( second, last ) < 0 );
      Assert.assertTrue( comparator.compare( last, second ) > 0 );
      Assert.assertTrue( comparator.compare( second, second ) == 0 );
   }//End Method
   
}// End Class
