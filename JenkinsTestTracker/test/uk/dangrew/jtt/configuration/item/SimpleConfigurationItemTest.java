/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.item;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import uk.dangrew.jtt.environment.preferences.PreferenceController;

/**
 * {@link SimpleConfigurationItem} test.
 */
public class SimpleConfigurationItemTest {

   private static final String NAME = "any name";
   
   @Mock private Node contentTitle;
   @Mock private PreferenceController controller;
   @Mock private Node content;
   
   /** Test extension for providing the inherited functionality.**/
   private static class TestConfigurationItem extends SimpleConfigurationItem {

      /** Constructor required by parent.**/
      protected TestConfigurationItem( String itemName, Node contentTitle, PreferenceController controller, Node content ) {
         super( itemName, contentTitle, controller, content );
      }//End Constructor
      
   }//End Class
   
   private SimpleConfigurationItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new TestConfigurationItem( NAME, contentTitle, controller, content );
   }//End Method
   
   @Test public void shouldHandleClickByInstructingController() {
      systemUnderTest.handleBeingSelected();
      verify( controller ).displayContent( contentTitle, content );
   }//End Method
   
   @Test public void shouldProvideToStringUsingName(){
      assertThat( systemUnderTest.toString(), is( NAME ) );
   }//End Method
   
   @Test public void shouldNotBeAssociatedWithAnything(){
      assertThat( systemUnderTest.isAssociatedWith( new Object() ), is( false ) );
   }//End Method

}//End Class
