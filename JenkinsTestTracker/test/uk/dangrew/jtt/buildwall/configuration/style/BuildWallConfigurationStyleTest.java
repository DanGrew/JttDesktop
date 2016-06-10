/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.style;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.buildwall.configuration.style.BuildWallConfigurationStyle;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.javafx.spinner.DefensiveDoubleSpinnerValueFactory;
import uk.dangrew.jtt.javafx.spinner.DefensiveIntegerSpinnerValueFactory;
import uk.dangrew.jtt.javafx.spinner.DoublePropertySpinner;
import uk.dangrew.jtt.javafx.spinner.IntegerPropertySpinner;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link BuildWallConfigurationStyle} test.
 */
public class BuildWallConfigurationStyleTest {

   public static final int TITLE_FONT_SIZE = BuildWallConfigurationStyle.TITLE_FONT_SIZE;
   public static final double LABEL_PERCENTAGE_WIDTH = BuildWallConfigurationStyle.LABEL_PERCENTAGE_WIDTH;
   public static final double CONTROLS_PERCENTAGE_WIDTH = BuildWallConfigurationStyle.CONTROLS_PERCENTAGE_WIDTH;
   private BuildWallConfigurationStyle systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new BuildWallConfigurationStyle();
   }//End Method
   
   @Test public void shouldProvideLabelWithBoldFont() {
      final String text = "anything";
      Label label = systemUnderTest.createBoldLabel( text );
      
      assertThat( label.getText(), is( text ) );
      assertThat( FontWeight.findByName( label.getFont().getStyle() ), is( FontWeight.BOLD ) );
   }//End Method
   
   @Test public void shouldProvideLabelWithBoldFontAndSize() {
      final String text = "anything";
      final double size = 34;
      Label label = systemUnderTest.createBoldLabel( text, size );
      
      assertThat( label.getText(), is( text ) );
      assertThat( FontWeight.findByName( label.getFont().getStyle() ), is( FontWeight.BOLD ) );
      assertThat( label.getFont().getSize(), closeTo( size, TestCommon.precision() ) );
   }//End Method
   
   @Test public void shouldConfigureIntegerPropertySpinner(){
      IntegerProperty property = new SimpleIntegerProperty();
      IntegerPropertySpinner spinner = new IntegerPropertySpinner();
      final int min = 20;
      final int max = 40;
      final int step = 5;
      
      systemUnderTest.configureIntegerSpinner( spinner, property, min, max, step );
      
      assertThat( spinner.getValueFactory(), instanceOf( DefensiveIntegerSpinnerValueFactory.class ) );
      
      spinner.getValueFactory().setValue( 50 );
      assertThat( spinner.getValueFactory().getValue(), is( max ) );
      
      spinner.getValueFactory().setValue( 10 );
      assertThat( spinner.getValueFactory().getValue(), is( min ) );
      
      spinner.increment();
      assertThat( spinner.getValueFactory().getValue(), is( min + step ) );
      assertThat( property.intValue(), is( min + step ) );
      
      final int newValue = 35;
      property.setValue( newValue );
      assertThat( spinner.getValueFactory().getValue(), is( newValue ) );
      
      assertThat( spinner.isEditable(), is( true ) );
      assertThat( spinner.getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldConfigureDoublePropertySpinner(){
      DoubleProperty property = new SimpleDoubleProperty();
      DoublePropertySpinner spinner = new DoublePropertySpinner();
      final double min = 20.5;
      final double max = 40.1;
      final double step = 5.6;
      
      systemUnderTest.configureDoubleSpinner( spinner, property, min, max, step );
      
      assertThat( spinner.getValueFactory(), instanceOf( DefensiveDoubleSpinnerValueFactory.class ) );
      
      spinner.getValueFactory().setValue( 50.0 );
      assertThat( spinner.getValueFactory().getValue(), is( max ) );
      
      spinner.getValueFactory().setValue( 10.4 );
      assertThat( spinner.getValueFactory().getValue(), is( min ) );
      
      spinner.increment();
      assertThat( spinner.getValueFactory().getValue(), is( min + step ) );
      assertThat( property.doubleValue(), is( min + step ) );
      
      final double newValue = 35.7;
      property.setValue( newValue );
      assertThat( spinner.getValueFactory().getValue(), is( newValue ) );
      
      assertThat( spinner.isEditable(), is( true ) );
      assertThat( spinner.getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldConstructTitle(){
      final String title = "some title";
      Label constructed = systemUnderTest.constructTitle( title );
      
      assertThat( constructed.getText(), is( title ) );
      TestCommon.assertThatFontIsBold( constructed.getFont() );
      assertThat( constructed.getAlignment(), is( Pos.CENTER ) );
      
      assertThat( GridPane.getColumnIndex( constructed ), is( 0 ) );
      assertThat( GridPane.getRowIndex( constructed ), is( 0 ) );
      assertThat( GridPane.getColumnSpan( constructed ), is( 2 ) );
      assertThat( GridPane.getRowSpan( constructed ), is( 1 ) );
      assertThat( GridPane.getHalignment( constructed ), is( HPos.CENTER ) );
      assertThat( GridPane.getValignment( constructed ), is( VPos.CENTER ) );
   }//End Method
   
   @Test public void shouldProvideColumnConstraintsForGrid(){
      GridPane grid = new GridPane();
      systemUnderTest.configureColumnConstraints( grid );
      
      assertThat( grid.getColumnConstraints(), hasSize( 2 ) );
      
      ColumnConstraints first = grid.getColumnConstraints().get( 0 );
      assertThat( first.getPercentWidth(), is( BuildWallConfigurationStyle.LABEL_PERCENTAGE_WIDTH ) );
      assertThat( first.getHgrow(), is( Priority.ALWAYS ) );
      
      ColumnConstraints second = grid.getColumnConstraints().get( 1 );
      assertThat( second.getPercentWidth(), is( BuildWallConfigurationStyle.CONTROLS_PERCENTAGE_WIDTH ) );
      assertThat( second.getHgrow(), is( Priority.ALWAYS ) );
   }//End Method

}//End Class