/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;


import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javafx.scene.Node;
import javafx.scene.control.Label;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.graphics.JavaFxInitializer;

/**
 * {@link ConfigurationTreeController} test.
 */
public class ConfigurationTreeControllerTest {

   @Mock private ConfigurationTreeContent content;
   private ConfigurationTreeController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      systemUnderTest = new ConfigurationTreeController( content );
   }//End Method
   
   @Test public void shouldInstructContentToShowNewNodes() {
      Node titleNode = new Label();
      Node contentNode = new Label();
      
      systemUnderTest.displayContent( titleNode, contentNode );
      verify( content ).setContent( titleNode, contentNode );
   }//End Method

}//End Class
