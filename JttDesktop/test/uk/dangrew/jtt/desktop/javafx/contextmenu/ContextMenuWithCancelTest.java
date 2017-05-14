/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.contextmenu;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.layout.BorderPane;
import uk.dangrew.sd.graphics.launch.TestApplication;

/**
 * {@link ContextMenuWithCancel} test.
 */
public class ContextMenuWithCancelTest {
   
   private static final Object APPLY_CONTROLS = new Object();
   private static final Object APPLY_CANCEL = new Object();
   
   private TestableContextMenu systemUnderTest;
   private List< Object > callLog;
   
   /** Testable extension of sut.**/
   private class TestableContextMenu extends ContextMenuWithCancel {
      
      /** Constructor to initialise as child impls should.**/
      public TestableContextMenu() {
         super.initialise();
      }//End Constructor
      
      /**
       * {@inheritDoc}
       */
      @Override protected void applyControls() {
         callLog.add( APPLY_CONTROLS );
      }//End Method
      
      /**
       * {@inheritDoc}
       */
      @Override void applyCancel() {
         super.applyCancel();
         callLog.add( APPLY_CANCEL );
      }//End Method
      
   }//End Class
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      callLog = new ArrayList<>();
      systemUnderTest = new TestableContextMenu();
   }//End Method
   
   @Test public void shouldHaveExpectedMenus() {
      assertThat( systemUnderTest.getItems().contains( systemUnderTest.separator() ), is( true ) );
      assertThat( systemUnderTest.getItems().contains( systemUnderTest.cancel() ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideCancelOption() {
      assertThat( systemUnderTest.getItems().get( systemUnderTest.getItems().size() - 1 ).getText(), is( ContextMenuWithCancel.CANCEL_TEXT ) );
   }//End Method
   
   @Test public void shouldHideWhenCancelled() throws InterruptedException {
      BorderPane pane = new BorderPane();
      TestApplication.launch( () -> pane );

      PlatformImpl.runAndWait( () -> systemUnderTest.show( pane, 0, 0 ) );
      assertThat( systemUnderTest.isShowing(), is( true ) );
      PlatformImpl.runAndWait( () -> systemUnderTest.getItems().get( systemUnderTest.getItems().size() - 1 ).fire() );
      assertThat( systemUnderTest.isShowing(), is( false ) );
   }//End Method
   
   @Test public void shouldAutoHide() {
      assertThat( systemUnderTest.isAutoHide(), is( true ) );
   }//End Method
   
   @Test public void shouldApplyLayoutBeforeApplyingCancel(){
      assertThat( callLog.get( 0 ), is( APPLY_CONTROLS ) );
      assertThat( callLog.get( 1 ), is( APPLY_CANCEL ) );
   }//End Method
}//End Class
