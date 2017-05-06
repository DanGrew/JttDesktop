/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.content;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.control.Label;
import uk.dangrew.jtt.desktop.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;

/**
 * {@link ConfigurationTreeContent} test.
 */
public class ConfigurationTreeContentTest {

   private ConfigurationTreeContent systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      systemUnderTest = new ConfigurationTreeContent();
   }//End Method
   
   @Test public void shouldContainTopAnCenterSubPanels() {
      assertThat( systemUnderTest.getTop(), is( systemUnderTest.top() ) );
      assertThat( systemUnderTest.getCenter(), is( systemUnderTest.center() ) );
   }//End Method
   
   @Test public void shouldUseInnerInsetsForSubPanels(){
      assertThat( systemUnderTest.top().getInsets().getTop(), is( ConfigurationTreeContent.INNER_INSETS ) );
      assertThat( systemUnderTest.top().getInsets().getBottom(), is( ConfigurationTreeContent.INNER_INSETS ) );
      assertThat( systemUnderTest.top().getInsets().getLeft(), is( ConfigurationTreeContent.INNER_INSETS ) );
      assertThat( systemUnderTest.top().getInsets().getRight(), is( ConfigurationTreeContent.INNER_INSETS ) );
      
      assertThat( systemUnderTest.center().getInsets().getTop(), is( ConfigurationTreeContent.INNER_INSETS ) );
      assertThat( systemUnderTest.center().getInsets().getBottom(), is( ConfigurationTreeContent.INNER_INSETS ) );
      assertThat( systemUnderTest.center().getInsets().getLeft(), is( ConfigurationTreeContent.INNER_INSETS ) );
      assertThat( systemUnderTest.center().getInsets().getRight(), is( ConfigurationTreeContent.INNER_INSETS ) );
   }//End Method
   
   @Test public void shouldUseOuterInsetsForContent(){
      assertThat( systemUnderTest.getInsets().getTop(), is( ConfigurationTreeContent.OUTER_OFFSETS ) );
      assertThat( systemUnderTest.getInsets().getBottom(), is( ConfigurationTreeContent.OUTER_OFFSETS ) );
      assertThat( systemUnderTest.getInsets().getLeft(), is( ConfigurationTreeContent.OUTER_OFFSETS ) );
      assertThat( systemUnderTest.getInsets().getRight(), is( ConfigurationTreeContent.OUTER_OFFSETS ) );
   }//End Method
   
   @Test public void shouldReplaceContentWhenInstructed(){
      Node title = new Label();
      Node content = new Label();
      
      systemUnderTest.setContent( title, content );
      assertThat( systemUnderTest.top().getCenter(), is( title ) );
      assertThat( systemUnderTest.center().getCenter(), is( content ) );
   }//End Method

}//End Class
