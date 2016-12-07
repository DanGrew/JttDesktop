/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.build;

/**
 * The {@link TestResults} provides a read only report of the tests associated 
 * with a {@link JenkinsBuild}.
 */
public class TestResults {
   
   private final int failures;
   private final int skipped;
   private final int total;
   
   /**
    * Constructs a new {@link TestResults}.
    * @param failures the number of failed test cases.
    * @param skipped the number of skipped test cases.
    * @param total the total number of test cases run.
    */
   public TestResults( int failures, int skipped, int total ) {
      this.failures = failures;
      this.skipped = skipped;
      this.total = total;
   }//End Constructor

   /**
    * Access to the number of failed test cases.
    * @return the value.
    */
   public int failures(){
      return failures;
   }//End Method
   
   /**
    * Access to the number of skipped test cases.
    * @return the value.
    */
   public int skipped(){
      return skipped;
   }//End Method
   
   /**
    * Access to the total number of test cases.
    * @return the value.
    */
   public int total(){
      return total;
   }//End Method

}//End Class
