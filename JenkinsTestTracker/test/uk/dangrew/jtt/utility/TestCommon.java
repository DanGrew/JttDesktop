/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.Assert;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Common test values and properties used.
 */
public final class TestCommon {
   
   private static final double PRECISION = 0.001;
   
   /**
    * Gets the precision to use for double comparisons.
    * @return the error to compare to.
    */
   public static final double precision(){
      return PRECISION;
   }//End Method
   
   /**
    * Method to read a text file into a {@link String}.
    * @param locationClass the {@link Class} in the package to load the {@link File} from.
    * @param filename the name of the {@link File}.
    * @return the {@link String} containing all text from the {@link File}.
    */
   public static final String readFileIntoString( Class< ? > locationClass, String filename ) {
      InputStream stream = locationClass.getResourceAsStream( filename );
      Assert.assertNotNull( stream );
      
      Scanner scanner = new Scanner( stream );
      String content = scanner.useDelimiter( "//Z" ).next();
      scanner.close();
      return content;
   }//End Method
   
   /**
    * Method to assert that all values of the enum map using {@link Enum#valueOf(Class, String)} to {@link Enum#name()}.
    * @param enumClass the {@link Enum} {@link Class} to prove.
    * @param <E> the enum type. 
    */
   public static < E extends Enum< E > > void assertEnumNameWithValueOf( Class< E > enumClass ) {
      for ( Enum< E > value : enumClass.getEnumConstants() ) {
         Assert.assertEquals( value, Enum.valueOf( enumClass, value.name() ) );
      }
   }//End Method
   
   /**
    * Method to assert that all values of the enum map using {@link Enum#valueOf(Class, String)} to {@link Enum#toString()}.
    * @param enumClass the {@link Enum} {@link Class} to prove.
    * @param <E> the enum type.
    */
   public static < E extends Enum< E > > void assertEnumToStringWithValueOf( Class< E > enumClass ) {
      for ( Enum< E > value : enumClass.getEnumConstants() ) {
         Assert.assertEquals( value, Enum.valueOf( enumClass, value.toString() ) );
      }
   }//End Method
   
   /**
    * Method to assert that the given {@link Font} is bold.
    * @param font the {@link Font} in question.
    */
   public static void assertThatFontIsBold( Font font ) {
      assertThat( FontWeight.findByName( font.getStyle() ), is( FontWeight.BOLD ) );
   }//End Method
   
   /**
    * Method to assert that two instructions do not concurrently interfere with each other.
    * @param threadOneInstruction the {@link Consumer} to accept the loop count and execute the first instruction.
    * @param threadTwoInstruction the {@link Consumer} to accept the loop count and execute the second instruction.
    * @param loopCounts the number of loops of each instruction to run.
    */
   public static final void assertConcurrencyIsNotAnIssue( 
            Consumer< Integer > threadOneInstruction, 
            Consumer< Integer > threadTwoInstruction,
            final int loopCounts
   ){
      CountDownLatch latch = new CountDownLatch( 2 );
      
      IntegerProperty firstCount = new SimpleIntegerProperty( 0 );
      IntegerProperty secondCount = new SimpleIntegerProperty( 0 );
      
      new Thread( () -> {
         for ( int i = 0; i < loopCounts; i++ ) {
            threadOneInstruction.accept( i );
            firstCount.set( firstCount.get() + 1 );
         }
         latch.countDown();
      } ).start();
      
      new Thread( () -> {
         for ( int i = 0; i < loopCounts; i++ ) {
            threadTwoInstruction.accept( i );
            secondCount.set( secondCount.get() + 1 );
         }
         latch.countDown();
      } ).start();
      
      try {
         latch.await( 10000, TimeUnit.MILLISECONDS );
      } catch ( InterruptedException e ) {
         fail( "Waiting on latch has failed." );
      }
      assertThat( firstCount.get(), is( loopCounts ) );
      assertThat( secondCount.get(), is( loopCounts ) );
   }//End Method

}//End Class
