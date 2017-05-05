/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.shortcuts.keyboard;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import uk.dangrew.jtt.shortcuts.keyboard.KeyBoardShortcuts;

/**
 * {@link KeyBoardShortcuts} test.
 */
public class KeyBoardShortcutsTest {

   private ObservableMap< KeyCombination, Runnable > accelerators;
   @Mock private Runnable runnable;
   @Mock private Scene scene;
   
   @Before public void initialiseSystemUnderTest(){
      accelerators = FXCollections.observableHashMap();
      MockitoAnnotations.initMocks( this );
      when( scene.getAccelerators() ).thenReturn( accelerators );
   }//End Method
   
   @Test public void shouldAssociateRunnableWithShortcut() {
      assertThat( accelerators.isEmpty(), is( true ) );
      KeyBoardShortcuts.cmdShiftO( scene, runnable );
      assertThat( accelerators.get( KeyBoardShortcuts.cmdShiftO ), is( runnable ) );
   }//End Method
   
   @Test public void shouldReplaceShortcutWithGiven() {
      assertThat( accelerators.isEmpty(), is( true ) );
      KeyBoardShortcuts.cmdShiftO( scene, runnable );
      KeyBoardShortcuts.cmdShiftO( scene, runnable );
      assertThat( accelerators.get( KeyBoardShortcuts.cmdShiftO ), is( runnable ) );
      
      Runnable alternate = mock( Runnable.class );
      KeyBoardShortcuts.cmdShiftO( scene, alternate );
      assertThat( accelerators.get( KeyBoardShortcuts.cmdShiftO ), is( alternate ) );
   }//End Method
   
   @Test public void shouldProvideRunnablesForShortcuts(){
      assertThat( KeyBoardShortcuts.getCmdShiftO( scene ), nullValue() );
      KeyBoardShortcuts.cmdShiftO( scene, runnable );
      assertThat( KeyBoardShortcuts.getCmdShiftO( scene ), is( runnable ) );
   }//End Method

}//End Class
