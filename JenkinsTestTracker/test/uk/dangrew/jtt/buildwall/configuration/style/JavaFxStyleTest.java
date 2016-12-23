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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.javafx.spinner.DefensiveDoubleSpinnerValueFactory;
import uk.dangrew.jtt.javafx.spinner.DefensiveIntegerSpinnerValueFactory;
import uk.dangrew.jtt.javafx.spinner.DoublePropertySpinner;
import uk.dangrew.jtt.javafx.spinner.IntegerPropertySpinner;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link JavaFxStyle} test.
 */
public class JavaFxStyleTest {

   public static final int TITLE_FONT_SIZE = JavaFxStyle.TITLE_FONT_SIZE;
   public static final double LABEL_PERCENTAGE_WIDTH = JavaFxStyle.LABEL_PERCENTAGE_WIDTH;
   public static final double CONTROLS_PERCENTAGE_WIDTH = JavaFxStyle.CONTROLS_PERCENTAGE_WIDTH;
   private JavaFxStyle systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JavaFxStyle();
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
      assertThat( first.getPercentWidth(), is( JavaFxStyle.LABEL_PERCENTAGE_WIDTH ) );
      assertThat( first.getHgrow(), is( Priority.ALWAYS ) );
      
      ColumnConstraints second = grid.getColumnConstraints().get( 1 );
      assertThat( second.getPercentWidth(), is( JavaFxStyle.CONTROLS_PERCENTAGE_WIDTH ) );
      assertThat( second.getHgrow(), is( Priority.ALWAYS ) );
   }//End Method
   
   @Test public void shouldProvideUniqueLabelWithWrappedText(){
      final String anyText = "anything that should be wrapped";
      Label label = systemUnderTest.createWrappedTextLabel( anyText );
      
      assertThat( label.getText(), is( anyText ) );
      assertThat( label.isWrapText(), is( true ) );
      assertThat( systemUnderTest.createWrappedTextLabel( anyText ), is( not( label ) ) );
   }//End Method
   
   @Test public void shouldRemoveBackgroundAndColourOnClick(){
      Button button = new Button();
      systemUnderTest.removeBackgroundAndColourOnClick( button, Color.AQUAMARINE );
      
      assertThat( button.getBackground(), is( nullValue() ) );
      
      button.getOnMousePressed().handle( mock( MouseEvent.class ) );
      assertThat( button.getBackground().getFills().get( 0 ).getFill(), is( Color.AQUAMARINE ) );
      
      button.getOnMouseReleased().handle( mock( MouseEvent.class ) );
      assertThat( button.getBackground(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldConfigureColorPickerWithProperty(){
      ObjectProperty< Color > property = new SimpleObjectProperty<>( Color.BLACK );
      ColorPicker picker = new ColorPicker( Color.RED );
      
      systemUnderTest.configureColorPicker( picker, property );
      assertThat( picker.getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( picker.getValue(), is( Color.BLACK ) );
      
      property.set( Color.YELLOW );
      assertThat( picker.getValue(), is( Color.YELLOW ) );
      
      picker.setValue( Color.PURPLE );
      assertThat( property.get(), is( Color.PURPLE ) );
   }//End Method
   
   @Test public void shouldConfigureColorPicker(){
      ColorPicker picker = new ColorPicker( Color.RED );
      
      systemUnderTest.configureColorPicker( picker, Color.BLACK );
      assertThat( picker.getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( picker.getValue(), is( Color.BLACK ) );
   }//End Method
   
   @Test public void shouldConfigureColumnConstraints(){
      GridPane pane = new GridPane();
      systemUnderTest.configureConstraintsForColumnPercentages( pane, 10, 20, 70 );
      
      assertThat( pane.getColumnConstraints(), hasSize( 3 ) );
      assertThat( pane.getColumnConstraints().get( 0 ).getPercentWidth(), is( 10.0 ) );
      assertThat( pane.getColumnConstraints().get( 1 ).getPercentWidth(), is( 20.0 ) );
      assertThat( pane.getColumnConstraints().get( 2 ).getPercentWidth(), is( 70.0 ) );
   }//End Method
   
   @Test public void shouldConfigureHalfWidthColumnConstraints(){
      GridPane pane = new GridPane();
      systemUnderTest.configureHalfWidthConstraints( pane );
      
      assertThat( pane.getColumnConstraints(), hasSize( 2 ) );
      assertThat( pane.getColumnConstraints().get( 0 ).getPercentWidth(), is( JavaFxStyle.HALF_WIDTH_COLUMN ) );
      assertThat( pane.getColumnConstraints().get( 1 ).getPercentWidth(), is( JavaFxStyle.HALF_WIDTH_COLUMN ) );
   }//End Method
   
   @Test public void shouldConfigureEvenWidthColumnConstraints(){
      GridPane pane = new GridPane();
      systemUnderTest.configureConstraintsForEvenColumns( pane, 5 );
      
      assertThat( pane.getColumnConstraints(), hasSize( 5 ) );
      for ( int i = 0; i < 5; i++ ) {
         assertThat( pane.getColumnConstraints().get( i ).getPercentWidth(), is( 20.0 ) );
      }
   }//End Method
   
   @Test public void shouldConfigureFullWithColumnConstraints(){
      GridPane pane = new GridPane();
      systemUnderTest.configureFullWidthConstraints( pane );
      
      assertThat( pane.getColumnConstraints(), hasSize( 1 ) );
      assertThat( pane.getColumnConstraints().get( 0 ).getPercentWidth(), is( JavaFxStyle.FULL_WIDTH_COLUMN ) );
   }//End Method
   
   @Test public void shouldProvideHaldWidthColumn(){
      assertThat( systemUnderTest.halfColumnWidth(), is( JavaFxStyle.HALF_WIDTH_COLUMN ) );
   }//End Method
   
   @Test public void shouldConfigureRowConstraints(){
      GridPane pane = new GridPane();
      systemUnderTest.configureConstraintsForRowPercentages( pane, 10, 20, 70 );
      
      assertThat( pane.getRowConstraints(), hasSize( 3 ) );
      assertThat( pane.getRowConstraints().get( 0 ).getPercentHeight(), is( 10.0 ) );
      assertThat( pane.getRowConstraints().get( 1 ).getPercentHeight(), is( 20.0 ) );
      assertThat( pane.getRowConstraints().get( 2 ).getPercentHeight(), is( 70.0 ) );
   }//End Method
   
   @Test public void shouldApplyBasicPaddingToRegion(){
      BorderPane region = new BorderPane();
      systemUnderTest.applyBasicPadding( region );
      assertThat( region.getInsets().getBottom(), is( JavaFxStyle.PADDING ) );
      assertThat( region.getInsets().getTop(), is( JavaFxStyle.PADDING ) );
      assertThat( region.getInsets().getLeft(), is( JavaFxStyle.PADDING ) );
      assertThat( region.getInsets().getRight(), is( JavaFxStyle.PADDING ) );
   }//End Method
   
   @Test public void shouldConfigureFriendlyFileChooser(){
      FriendlyFileChooser chooser = mock( FriendlyFileChooser.class );
      systemUnderTest.configureFileChooser( chooser, "anything" );
      verify( chooser ).setTitle( "anything" );
      verify( chooser ).setInitialDirectory( JavaFxStyle.USER_HOME_FILE );
   }//End Method

}//End Class
