/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.resources;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link ManagementConsoleImages} test.
 */
public class ManagementConsoleImagesTest {

   private ManagementConsoleImages systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new ManagementConsoleImages();
   }//End Method
   
   @Test public void shouldFindImages() {
      assertThat( systemUnderTest.constuctActionImage(), is( notNullValue() ) );
      assertThat( systemUnderTest.constuctCloseImage(), is( notNullValue() ) );
      assertThat( systemUnderTest.constuctEqualImage(), is( notNullValue() ) );
      assertThat( systemUnderTest.constuctPassedImage(), is( notNullValue() ) );
      assertThat( systemUnderTest.constuctPeopleImage(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldCahceImages() {
      assertThat( systemUnderTest.constuctActionImage(), is( systemUnderTest.constuctActionImage() ) );
      assertThat( systemUnderTest.constuctCloseImage(), is( systemUnderTest.constuctCloseImage() ) );
      assertThat( systemUnderTest.constuctEqualImage(), is( systemUnderTest.constuctEqualImage() ) );
      assertThat( systemUnderTest.constuctPassedImage(), is( systemUnderTest.constuctPassedImage() ) );
      assertThat( systemUnderTest.constuctPeopleImage(), is( systemUnderTest.constuctPeopleImage() ) );
   }//End Method

}//End Class
