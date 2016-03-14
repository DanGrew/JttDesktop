/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.input.ContextMenuEvent;

/**
 * {@link DualBuildWallContextMenuOpener} test.
 */
public class DualBuildWallContextMenuOpenerTest {

   @Mock private DualBuildWallDisplayImpl display;
   @Mock private DualBuildWallContextMenu menu;
   private DualBuildWallContextMenuOpener systemUnderTest;

   @BeforeClass public static void initialisePlatform(){
      PlatformImpl.startup( () -> {} );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      when( menu.friendly_isShowing() ).thenReturn( false );
      systemUnderTest = new DualBuildWallContextMenuOpener( display, menu );
   }//End Method
   
   @Test public void shouldPositionMenuAtMouseLocation() {
      final double x = 394857.234;
      final double y = 2387.938;
      ContextMenuEvent event = new ContextMenuEvent( null, -1, -1, x, y, false, null );
      systemUnderTest.handle( event );
      
      verify( menu ).friendly_show( display, x, y );
   }//End Method
   
   @Test public void shouldHideAndShowSingleInstance() {
      when( menu.friendly_isShowing() ).thenReturn( false, true, false, true );
      
      ContextMenuEvent event = new ContextMenuEvent( null, -1, -1, 0, 0, false, null );
      systemUnderTest.handle( event );
      
      verify( menu, times( 1 ) ).friendly_show( Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble() );
      
      systemUnderTest.handle( event );
      
      systemUnderTest.handle( event );
      verify( menu, times( 2 ) ).friendly_show( Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble() );
      
      systemUnderTest.handle( event );
      verify( menu, times( 2 ) ).friendly_show( Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble() );
   }//End Method
   
}//End Class
