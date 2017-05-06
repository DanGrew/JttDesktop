/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.item;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import uk.dangrew.jtt.desktop.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;

public class ScrollableConfigurationItemTest {

   private static final String NAME = "any name";
   
   @Mock private Node contentTitle;
   @Mock private PreferenceController controller;
   @Mock private Node content;
   
   private ScrollableConfigurationItem systemUnderTest;
   
   /** Testable concrete extension.**/
   private static class TestableScrollableItem extends ScrollableConfigurationItem {

      protected TestableScrollableItem( String itemName, Node contentTitle, PreferenceController controller, Node content ) {
         super( itemName, contentTitle, controller, content );
      }//End Constructor
      
   }//End Class

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new TestableScrollableItem( NAME, contentTitle, controller, content );
   }//End Method

   @Test public void shouldProvideToStringUsingName(){
      assertThat( systemUnderTest.toString(), is( NAME ) );
   }//End Method
   
   @Test public void shouldNotBeAssociatedWithAnything(){
      assertThat( systemUnderTest.isAssociatedWith( new Object() ), is( false ) );
   }//End Method
   
   @Test public void shouldWrapInScroller(){
      systemUnderTest = new TestableScrollableItem( NAME, contentTitle, controller, content );
      ArgumentCaptor< Node > contentCaptor = ArgumentCaptor.forClass( Node.class );
      systemUnderTest.handleBeingSelected();
      verify( controller ).displayContent( Mockito.eq( contentTitle ), contentCaptor.capture() );
      
      assertThat( contentCaptor.getValue(), is( instanceOf( ScrollPane.class ) ) );
      ScrollPane scroller = ( ScrollPane ) contentCaptor.getValue();
      assertThat( scroller.getContent(), is( content ) );
      assertThat( scroller.getHbarPolicy(), is( ScrollBarPolicy.NEVER ) );
      assertThat( scroller.isFitToWidth(), is( true ) );
   }//End Method

}//End Class
