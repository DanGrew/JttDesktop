/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class StatusConfigurationPaneTest {

   @Spy private JavaFxStyle styling; 
   private BuildWallTheme theme;
   private ThemeBuilderShortcutProperties shortcuts;
   private BuildResultStatus status;
   private StatusConfigurationPane systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      status = BuildResultStatus.SUCCESS;
      shortcuts = new ThemeBuilderShortcutProperties();
      shortcuts.shortcutColorProperty().set( Color.GOLD );
      theme = new BuildWallThemeImpl( "Test" );
      theme.barColoursMap().put( status, Color.BLACK );
      theme.trackColoursMap().put( status, Color.RED );
      theme.jobNameColoursMap().put( status, Color.YELLOW );
      theme.buildNumberColoursMap().put( status, Color.GREEN );
      theme.completionEstimateColoursMap().put( status, Color.ORANGE );
      theme.detailColoursMap().put( status, Color.WHEAT );
      
      systemUnderTest = new StatusConfigurationPane( styling, theme, shortcuts, status );
   }//End Method
   
   @Test public void shouldUsePadding(){
      verify( styling ).applyBasicPadding( systemUnderTest );
   }//End Method

   @Test public void shouldUseHalfWidthColumns() {
      verify( styling ).configureConstraintsForColumnPercentages( 
               systemUnderTest,
               StatusConfigurationPane.LABEL_WIDTH_PERCENTAGE,
               StatusConfigurationPane.PICKER_WIDTH_PERCENTAGE,
               StatusConfigurationPane.SHORTCUT_WIDTH_PERCENTAGE
      );
   }//End Method
   
   @Test public void shouldHaveInitialBarColour(){
      assertThat( systemUnderTest.barPicker().getValue(), is( Color.BLACK ) );
   }//End Method
   
   @Test public void shouldHaveInitialTrackColour(){
      assertThat( systemUnderTest.trackPicker().getValue(), is( Color.RED ) );
   }//End Method
   
   @Test public void shouldHaveInitialJobNameColour(){
      assertThat( systemUnderTest.jobNamePicker().getValue(), is( Color.YELLOW ) );
   }//End Method
   
   @Test public void shouldHaveInitialBuildNumberColour(){
      assertThat( systemUnderTest.buildNumberPicker().getValue(), is( Color.GREEN ) );
   }//End Method
   
   @Test public void shouldHaveInitialCompletionEstimateColour(){
      assertThat( systemUnderTest.completionEstimatePicker().getValue(), is( Color.ORANGE ) );
   }//End Method
   
   @Test public void shouldHaveInitialDetailColour(){
      assertThat( systemUnderTest.detailPicker().getValue(), is( Color.WHEAT ) );
   }//End Method
   
   private void assertThatElementsAreProvided( 
            Label label, String labelText, 
            ColorPicker picker,
            Button button,
            ObservableMap< BuildResultStatus, Color > map 
   ){
      assertThat( systemUnderTest.getChildren().contains( label ), is( true ) );
      assertThat( label.getText(), is( labelText ) );
      verify( styling ).createBoldLabel( labelText );
      
      assertThat( systemUnderTest.getChildren().contains( picker ), is( true ) );
      verify( styling ).configureColorPicker( picker, map.get( status ) );
      
      assertThat( systemUnderTest.getChildren().contains( button ), is( true ) );
      assertThat( button.getText(), is( StatusConfigurationPane.SHORTCUT_BUTTON_TEXT ) );
      assertThat( button.getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldProvideBarElements() {
      assertThatElementsAreProvided( 
               systemUnderTest.barLabel(), StatusConfigurationPane.BAR_COLOUR_STRING, 
               systemUnderTest.barPicker(),
               systemUnderTest.barShortcut(),
               theme.barColoursMap()
      );
   }//End Method
   
   @Test public void shouldProvideTrackElements() {
      assertThatElementsAreProvided( 
               systemUnderTest.trackLabel(), StatusConfigurationPane.TRACK_COLOUR_STRING, 
               systemUnderTest.trackPicker(), 
               systemUnderTest.trackShortcut(), 
               theme.trackColoursMap()
      );
   }//End Method
   
   @Test public void shouldProvideJobNameElements() {
      assertThatElementsAreProvided( 
               systemUnderTest.jobNameLabel(), StatusConfigurationPane.JOB_NAME_COLOUR_STRING, 
               systemUnderTest.jobNamePicker(),
               systemUnderTest.jobNameShortcut(),
               theme.jobNameColoursMap()
      );
   }//End Method
   
   @Test public void shouldProvideBuildNumberElements() {
      assertThatElementsAreProvided( 
               systemUnderTest.buildNumberLabel(), StatusConfigurationPane.BUILD_NUMBER_COLOUR_STRING, 
               systemUnderTest.buildNumberPicker(), 
               systemUnderTest.buildNumberShortcut(),
               theme.buildNumberColoursMap()
      );
   }//End Method
   
   @Test public void shouldProvideCompletionEstimateElements() {
      assertThatElementsAreProvided( 
               systemUnderTest.completionEstimateLabel(), StatusConfigurationPane.BUILD_ESTIMATE_COLOUR_STRING, 
               systemUnderTest.completionEstimatePicker(), 
               systemUnderTest.completionEstimateShortcut(),
               theme.completionEstimateColoursMap()
      );
   }//End Method
   
   @Test public void shouldProvideDetailElements() {
      assertThatElementsAreProvided( 
               systemUnderTest.detailLabel(), StatusConfigurationPane.DETAIL_COLOUR_STRING, 
               systemUnderTest.detailPicker(), 
               systemUnderTest.detailShortcut(),
               theme.detailColoursMap()
      );
   }//End Method
   
   private void assertThatElementsAreUpdated(
            ObservableMap< BuildResultStatus, Color > map,
            ColorPicker picker
   ) {
      map.put( status, Color.BLANCHEDALMOND );
      assertThat( picker.getValue(), is( Color.BLANCHEDALMOND ) );
      
      map.put( status, null );
      assertThat( picker.getValue(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldUpdateBarElements() {
      assertThatElementsAreUpdated( theme.barColoursMap(), systemUnderTest.barPicker() );
   }//End Method
   
   @Test public void shouldUpdateTrackElements() {
      assertThatElementsAreUpdated( theme.trackColoursMap(), systemUnderTest.trackPicker() );
   }//End Method
   
   @Test public void shouldUpdateJobNameElements() {
      assertThatElementsAreUpdated( theme.jobNameColoursMap(), systemUnderTest.jobNamePicker() );
   }//End Method
   
   @Test public void shouldUpdateBuildNumberElements() {
      assertThatElementsAreUpdated( theme.buildNumberColoursMap(), systemUnderTest.buildNumberPicker() );
   }//End Method
   
   @Test public void shouldUpdateCompletionEstimateElements() {
      assertThatElementsAreUpdated( theme.completionEstimateColoursMap(), systemUnderTest.completionEstimatePicker() );
   }//End Method
   
   @Test public void shouldUpdateDetailElements() {
      assertThatElementsAreUpdated( theme.detailColoursMap(), systemUnderTest.detailPicker() );
   }//End Method
   
   private void assertThatElementsUpdateTheme( ObservableMap< BuildResultStatus, Color > map, ColorPicker picker ) {
      picker.setValue( Color.BLANCHEDALMOND );
      assertThat( map.get( status ), is( Color.BLANCHEDALMOND ) );
   }//End Method
   
   @Test public void shouldUpdateThemeFromBarElements() {
      assertThatElementsUpdateTheme( theme.barColoursMap(), systemUnderTest.barPicker() );
   }//End Method
   
   @Test public void shouldUpdateThemeFromTrackElements() {
      assertThatElementsUpdateTheme( theme.trackColoursMap(), systemUnderTest.trackPicker() );
   }//End Method
   
   @Test public void shouldUpdateThemeFromJobNameElements() {
      assertThatElementsUpdateTheme( theme.jobNameColoursMap(), systemUnderTest.jobNamePicker() );
   }//End Method
   
   @Test public void shouldUpdateThemeFromBuildNumberElements() {
      assertThatElementsUpdateTheme( theme.buildNumberColoursMap(), systemUnderTest.buildNumberPicker() );
   }//End Method
   
   @Test public void shouldUpdateThemeFromCompletionEstimateElements() {
      assertThatElementsUpdateTheme( theme.completionEstimateColoursMap(), systemUnderTest.completionEstimatePicker() );
   }//End Method
   
   @Test public void shouldUpdateThemeFromDetailElements() {
      assertThatElementsUpdateTheme( theme.detailColoursMap(), systemUnderTest.detailPicker() );
   }//End Method
   
   private void assertThatShortcutIsApplied(
            Button shortcut,
            ColorPicker picker
   ) {
      shortcut.getOnAction().handle( new ActionEvent() );
      assertThat( picker.getValue(), is( Color.GOLD ) );
      
      shortcuts.shortcutColorProperty().set( Color.ANTIQUEWHITE );
      shortcut.getOnAction().handle( new ActionEvent() );
      assertThat( picker.getValue(), is( Color.ANTIQUEWHITE ) );
   }//End Method
   
   @Test public void shouldApplyShortcutToBarColor() {
      assertThatShortcutIsApplied( systemUnderTest.barShortcut(), systemUnderTest.barPicker() );
   }//End Method
   
   @Test public void shouldApplyShortcutToTrackColor() {
      assertThatShortcutIsApplied( systemUnderTest.trackShortcut(), systemUnderTest.trackPicker() );
   }//End Method
   
   @Test public void shouldApplyShortcutToJobNameColor() {
      assertThatShortcutIsApplied( systemUnderTest.jobNameShortcut(), systemUnderTest.jobNamePicker() );
   }//End Method
   
   @Test public void shouldApplyShortcutToBuildNumberColor() {
      assertThatShortcutIsApplied( systemUnderTest.buildNumberShortcut(), systemUnderTest.buildNumberPicker() );
   }//End Method
   
   @Test public void shouldApplyShortcutToCompletionEstimateColor() {
      assertThatShortcutIsApplied( systemUnderTest.completionEstimateShortcut(), systemUnderTest.completionEstimatePicker() );
   }//End Method
   
   @Test public void shouldApplyShortcutToDetailColor() {
      assertThatShortcutIsApplied( systemUnderTest.detailShortcut(), systemUnderTest.detailPicker() );
   }//End Method
   
   @Test public void shouldBeAssociatedWithTheme(){
      assertThat( systemUnderTest.isAssociatedWith( theme ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new BuildWallThemeImpl( "anything" ) ), is( false ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithShortcuts(){
      assertThat( systemUnderTest.isAssociatedWith( shortcuts ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new ThemeBuilderShortcutProperties() ), is( false ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithStatus(){
      assertThat( systemUnderTest.isAssociatedWith( status ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( BuildResultStatus.FAILURE ), is( false ) );
   }//End Method

}//End Class
