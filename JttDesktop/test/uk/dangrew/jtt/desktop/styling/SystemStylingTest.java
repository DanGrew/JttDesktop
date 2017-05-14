/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.styling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.scene.Parent;

/**
 * {@link SystemStyling} test.
 */
public class SystemStylingTest {
   
   @Test public void shouldGetAndSetInstance() {
      SystemStyles styles = Mockito.mock( SystemStyles.class );
      Assert.assertNotEquals( styles, SystemStyling.get() );
      SystemStyling.set( styles );
      Assert.assertEquals( styles, SystemStyling.get() );
   }//End Method
   
   @Test public void shouldInteractWithInstance() {
      SystemStyles styles = Mockito.mock( SystemStyles.class );
      SystemStyling.set( styles );
      
      Parent parent = Mockito.mock( Parent.class );
      SystemStyling.applyStyle( BuildWallStyles.ProgressBarFailed, BuildWallThemes.Standard, parent );
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarFailed, BuildWallThemes.Standard, parent );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldShowIllegalStateIfNoInstance(){
      SystemStyling.set( null );
      SystemStyling.applyStyle( BuildWallStyles.ProgressBarFailed, BuildWallThemes.Standard, Mockito.mock( Parent.class ) );
   }//End Method

}//End Class
