/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.friendly.javafx;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.awt.Desktop;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link FriendlyDesktop} test.
 */
public class FriendlyDesktopTest {

   private FriendlyDesktop systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new FriendlyDesktop();
   }//End Method
   
   @Test public void shouldProvideDesktop() {
      assertThat( systemUnderTest.getDesktop(), is( Desktop.getDesktop() ) );
   }//End Method

}//End Class
