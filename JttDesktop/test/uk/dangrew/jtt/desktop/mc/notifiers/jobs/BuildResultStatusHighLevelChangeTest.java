/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import javafx.scene.image.ImageView;
import uk.dangrew.kode.TestCommon;

/**
 * {@link BuildResultStatusHighLevelChange} test.
 */
public class BuildResultStatusHighLevelChangeTest {

   @Test public void shouldMapEnumNameWithValueOf() {
      TestCommon.assertEnumNameWithValueOf( BuildResultStatusHighLevelChange.class );
   }//End Method 
   
   @Test public void shouldMapToStringWithValueOf() {
      TestCommon.assertEnumToStringWithValueOf( BuildResultStatusHighLevelChange.class );
   }//End Method 
   
   @Test public void shouldProvideImageViewWithImage(){
      for ( BuildResultStatusHighLevelChange change : BuildResultStatusHighLevelChange.values() ) {
         ImageView view = change.constructImage();
         assertThat( view, is( notNullValue() ) );
         assertThat( view.getImage(), is( notNullValue() ) );
      }
   }//End Method

}//End Class
