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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.mc.view.tree.NotificationTreeContextMenu;

/**
 * {@link FriendlyMenuOpener} test.
 */
public class FriendlyMenuOpenerTest {
   
   @Mock private Node display;
   @Mock private FriendlyContextMenu menu;
   private FriendlyMenuOpener systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new FriendlyMenuOpener( display, menu );
   }//End Method

   @Test public void shouldAttachOpenerToDisplay() {
      assertThat( display.getOnContextMenuRequested(), is( systemUnderTest ) );
   }//End Method
   
   @Test public void shouldNotShowIfAlreadyShowing(){
      when( menu.friendly_isShowing() ).thenReturn( true );
      
      final double x = 394857.234;
      final double y = 2387.938;
      ContextMenuEvent event = new ContextMenuEvent( null, -1, -1, x, y, false, null );
      systemUnderTest.handle( event );
      
      verify( menu, never() ).friendly_show( Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble() );
   }//End Method
   
   @Test public void shouldShowIfNotShowing(){
      when( menu.friendly_isShowing() ).thenReturn( false );
      
      final double x = 394857.234;
      final double y = 2387.938;
      ContextMenuEvent event = new ContextMenuEvent( null, -1, -1, x, y, false, null );
      systemUnderTest.handle( event );
      
      verify( menu ).friendly_show( display, x, y );
   }//End Method
   
   @Test public void shouldBeAnchoredTo(){
      assertThat( systemUnderTest.isAnchoredTo( display ), is( true ) );
      assertThat( systemUnderTest.isAnchoredTo( mock( Node.class ) ), is( false ) );
   }//End Method
   
   @Test public void shouldBeControllingMenuType(){
      systemUnderTest = new FriendlyMenuOpener( new BorderPane(), new FriendlyContextMenu() );
      assertThat( systemUnderTest.isControllingA( FriendlyContextMenu.class ), is( true ) );
      assertThat( systemUnderTest.isControllingA( NotificationTreeContextMenu.class ), is( false ) );
   }//End Method

}//End Class
