/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package utility;

import org.junit.runner.RunWith;
import org.junit.runners.AllTests;
import org.junit.runners.Suite.SuiteClasses;

import graphics.JavaFxInitializerTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;

/**
 * The {@link SpamRunner} is responsible for running the same test repeatedly.
 */
@RunWith(AllTests.class) 
@SuiteClasses({}) public class SpamRunner {

   /**
    * Method used by JUnit to gather the tests to run.
    * @return the {@link TestSuite} to run.
    */
   public static TestSuite suite(){
      TestSuite suite = new TestSuite();
      for ( int i = 0; i < 1000; i++ ) {
         suite.addTest( new JUnit4TestAdapter( JavaFxInitializerTest.class ) );
      }
      return suite;
   }//End Method
}//End Class


