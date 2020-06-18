/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.view;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.dangrew.kode.launch.TestApplication;

/**
 * {@link ManagementConsoleStyle} test.
 */
public class ManagementConsoleStyleTest {

   private static final Image IMAGE = mock( Image.class );
   
   private ManagementConsoleStyle systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      systemUnderTest = new ManagementConsoleStyle();
   }//End Method
   
   @Test public void shouldConfigureButtonWithImage() {
      Button button = new Button();
      systemUnderTest.styleButtonSize( button, IMAGE );
      
      assertThat( button.getGraphic(), is( instanceOf( ImageView.class ) ) );
      ImageView view = ( ImageView ) button.getGraphic();
      assertThat( view.getImage(), is( IMAGE ) );
      
      assertThat( view.getFitWidth(), is( ManagementConsoleStyle.BUTTON_WIDTH ) );
      assertThat( view.getFitHeight(), is( ManagementConsoleStyle.BUTTON_HEIGHT ) );
      assertThat( button.getPrefWidth(), is( ManagementConsoleStyle.BUTTON_WIDTH ) );
      assertThat( button.getPrefHeight(), is( ManagementConsoleStyle.BUTTON_HEIGHT ) );
      assertThat( button.getMaxHeight(), is( ManagementConsoleStyle.BUTTON_HEIGHT ) );
      assertThat( button.getAlignment(), is( Pos.CENTER ) );
   }//End Method

}//End Class
