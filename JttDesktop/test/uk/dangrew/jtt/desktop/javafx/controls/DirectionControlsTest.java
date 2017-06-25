/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.controls;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class DirectionControlsTest {

   private static final FontAwesomeIconView LEFT_VIEW = new FontAwesomeIconView( FontAwesomeIcon.ANGLE_DOUBLE_LEFT );
   private static final FontAwesomeIconView UP_VIEW = new FontAwesomeIconView( FontAwesomeIcon.ANGLE_DOUBLE_UP );
   private static final FontAwesomeIconView RIGHT_VIEW = new FontAwesomeIconView( FontAwesomeIcon.ANGLE_DOUBLE_RIGHT );
   private static final FontAwesomeIconView DOWN_VIEW = new FontAwesomeIconView( FontAwesomeIcon.ANGLE_DOUBLE_DOWN );
   
   @Spy private JavaFxStyle styling;
   private DirectionControls systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new DirectionControls(
               styling, 
               new HashMap< DirectionControlType, GlyphIcon< ? >>() {{
                  put( DirectionControlType.Left, LEFT_VIEW );
                  put( DirectionControlType.Up, UP_VIEW );
                  put( DirectionControlType.Right, RIGHT_VIEW );
                  put( DirectionControlType.Down, DOWN_VIEW );
               }}
      );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> systemUnderTest );
      
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldProvideButtonsInGrid(){
      assertThat( systemUnderTest.getCenter(), is( systemUnderTest.grid() ) );
      
      for ( DirectionControlType type : DirectionControlType.values() ) {
         assertThat( systemUnderTest.grid().getChildren().contains( systemUnderTest.buttonFor( type ) ), is( true ) );
      }
      
      assertThat( GridPane.getColumnIndex( systemUnderTest.buttonFor( DirectionControlType.Up ) ), is( 1 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.buttonFor( DirectionControlType.Up ) ), is( 0 ) );
      
      assertThat( GridPane.getColumnIndex( systemUnderTest.buttonFor( DirectionControlType.Right ) ), is( 2 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.buttonFor( DirectionControlType.Right ) ), is( 1 ) );
      
      assertThat( GridPane.getColumnIndex( systemUnderTest.buttonFor( DirectionControlType.Down ) ), is( 1 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.buttonFor( DirectionControlType.Down ) ), is( 2 ) );
      
      assertThat( GridPane.getColumnIndex( systemUnderTest.buttonFor( DirectionControlType.Left ) ), is( 0 ) );
      assertThat( GridPane.getRowIndex( systemUnderTest.buttonFor( DirectionControlType.Left ) ), is( 1 ) );
   }//End Method
   
   @Test public void shouldCreateButtonsForGlyphs(){
      assertThat( systemUnderTest.buttonFor( DirectionControlType.Up ).getGraphic(), is( UP_VIEW ) );
      assertThat( systemUnderTest.buttonFor( DirectionControlType.Right ).getGraphic(), is( RIGHT_VIEW ) );
      assertThat( systemUnderTest.buttonFor( DirectionControlType.Down ).getGraphic(), is( DOWN_VIEW ) );
      assertThat( systemUnderTest.buttonFor( DirectionControlType.Left ).getGraphic(), is( LEFT_VIEW ) );
      
      verify( styling ).createGlyphButton( LEFT_VIEW );
      verify( styling ).createGlyphButton( UP_VIEW );
      verify( styling ).createGlyphButton( RIGHT_VIEW );
      verify( styling ).createGlyphButton( DOWN_VIEW );
   }//End Method
   
   @Test public void shouldConfigureGridColumnsAndRows(){
      verify( styling ).configureConstraintsForEvenColumns( systemUnderTest.grid(), 3 );
      verify( styling ).configureConstraintsForEvenRows( systemUnderTest.grid(), 3 );
   }//End Method
   
   @Test public void shouldProvidePadding(){
      assertThat( systemUnderTest.getPadding().getBottom(), is( DirectionControls.PADDING ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( DirectionControls.PADDING ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( DirectionControls.PADDING ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( DirectionControls.PADDING ) );
      
      assertThat( systemUnderTest.grid().getHgap(), is( DirectionControls.PADDING ) );
      assertThat( systemUnderTest.grid().getVgap(), is( DirectionControls.PADDING ) );
   }//End Method
   
   @Test public void shouldSetButtonSizePolicy(){
      for ( DirectionControlType type : DirectionControlType.values() ) {
         assertThat( systemUnderTest.buttonFor( type ).getMaxHeight(), is( Double.MAX_VALUE ) );
         assertThat( systemUnderTest.buttonFor( type ).getMaxWidth(), is( Double.MAX_VALUE ) );
      }
   }//End Method
   
   @Test public void shouldProvideNotificationForActions(){
      DirectionControlListener listener = mock( DirectionControlListener.class );
      systemUnderTest.setActionListener( listener );
      
      for ( DirectionControlType type : DirectionControlType.values() ) {
         systemUnderTest.buttonFor( type ).fire();
         verify( listener ).action( type );
      }
      
      systemUnderTest.setActionListener( null );
      
      for ( DirectionControlType type : DirectionControlType.values() ) {
         systemUnderTest.buttonFor( type ).fire();
      }
      
      verifyNoMoreInteractions( listener );
   }//End Method

}//End Class
