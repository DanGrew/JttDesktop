/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import javafx.scene.image.ImageView;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link BuildResultStatusChange} test.
 */
public class BuildResultStatusChangeTest {

   @Test public void shouldMapEnumNameWithValueOf() {
      TestCommon.assertEnumNameWithValueOf( BuildResultStatusChange.class );
   }//End Method 
   
   @Test public void shouldMapToStringWithValueOf() {
      TestCommon.assertEnumToStringWithValueOf( BuildResultStatusChange.class );
   }//End Method 
   
   @Test public void shouldProvideImageViewWithImage(){
      for ( BuildResultStatusChange change : BuildResultStatusChange.values() ) {
         ImageView view = change.constructImage();
         assertThat( view, is( notNullValue() ) );
         assertThat( view.getImage(), is( notNullValue() ) );
      }
   }//End Method

}//End Class
