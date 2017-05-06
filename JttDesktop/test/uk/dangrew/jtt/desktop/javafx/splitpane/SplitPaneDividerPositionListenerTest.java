/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.splitpane;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.javafx.splitpane.DividerPositionsChangedCallBack;
import uk.dangrew.jtt.desktop.javafx.splitpane.SplitPaneDividerPositionListener;

/**
 * {@link SplitPaneDividerPositionListener} test.
 */
public class SplitPaneDividerPositionListenerTest {

   private Node firstNode;
   private Node secondNode;
   private Node thirdNode;
   private SplitPane subject;
   @Mock private DividerPositionsChangedCallBack callBack;
   private SplitPaneDividerPositionListener systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      
      MockitoAnnotations.initMocks( this );
      firstNode = new Label();
      secondNode = new Label();
      thirdNode = new Label();
      subject = new SplitPane( firstNode, secondNode, thirdNode );
      systemUnderTest = new SplitPaneDividerPositionListener( subject, callBack );
   }//End Method
   
   @Test public void shouldCallBackWhenFirstDividerChanges() {
      subject.getDividers().get( 0 ).setPosition( 0.8 );
      verify( callBack ).dividerPositionsChanged();
   }//End Method
   
   @Test public void shouldCallBackWhenSecondDividerChanges() {
      subject.getDividers().get( 1 ).setPosition( 0.8 );
      verify( callBack ).dividerPositionsChanged();
   }//End Method

   @Test public void shouldCallBackWhenFirstNodeRemoved() {
      subject.getItems().remove( firstNode );
      verify( callBack, never() ).dividerPositionsChanged();
   }//End Method
   
   @Test public void shouldCallBackWhenThirdNodeAdded() {
      subject.getItems().add( thirdNode );
      verify( callBack, never() ).dividerPositionsChanged();
   }//End Method
   
   @Test public void shouldNotCallBackWhenRemovedDividerPositionIsChanged() {
      Divider divider = subject.getDividers().get( 0 );
      subject.getItems().clear();
      
      divider.setPosition( 0.1 );
      verify( callBack, never() ).dividerPositionsChanged();
   }//End Method
}//End Class
