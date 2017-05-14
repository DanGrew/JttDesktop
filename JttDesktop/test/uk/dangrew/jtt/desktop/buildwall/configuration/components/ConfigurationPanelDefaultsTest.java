/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ConfigurationPanelDefaultsTest {

   private static final double LABEL_PERCENTAGE_WIDTH = ConfigurationPanelDefaults.LABEL_PERCENTAGE_WIDTH;
   static final double CONTROLS_PERCENTAGE_WIDTH = ConfigurationPanelDefaults.CONTROLS_PERCENTAGE_WIDTH;
   private ConfigurationPanelDefaults systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new ConfigurationPanelDefaults();
   }//End Method

   @Test public void shouldProvideColumnConstraintsForGrid(){
      GridPane grid = new GridPane();
      systemUnderTest.configureColumnConstraints( grid );
      
      assertThat( grid.getColumnConstraints(), hasSize( 2 ) );
      
      ColumnConstraints first = grid.getColumnConstraints().get( 0 );
      assertThat( first.getPercentWidth(), is( LABEL_PERCENTAGE_WIDTH ) );
      assertThat( first.getHgrow(), is( Priority.ALWAYS ) );
      
      ColumnConstraints second = grid.getColumnConstraints().get( 1 );
      assertThat( second.getPercentWidth(), is( CONTROLS_PERCENTAGE_WIDTH ) );
      assertThat( second.getHgrow(), is( Priority.ALWAYS ) );
   }//End Method

}//End Class
