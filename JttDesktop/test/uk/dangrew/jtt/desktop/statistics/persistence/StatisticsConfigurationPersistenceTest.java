/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.persistence;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.BACKGROUND_COLOUR;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.COLOURS;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.DESCRIPTION_TEXT_FAMILY;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.DESCRIPTION_TEXT_SIZE;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.EXCLUSIONS;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.FONTS;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.STATISTICS;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.TEXT_COLOUR;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.VALUE_TEXT_FAMILY;
import static uk.dangrew.jtt.desktop.statistics.persistence.StatisticsConfigurationPersistence.VALUE_TEXT_SIZE;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.utility.io.IoCommon;

public class StatisticsConfigurationPersistenceTest {

   private static final int EXCLUSION_COUNT = 3;
   private static final String FIRST_EXCLUSION = "Jupa-Build";
   private static final String SECOND_EXCLUSION = "ObjectBuilder";
   private static final String THIRD_EXCLUSION = "ObjectBuilderEnvironment";
   
   private static final String VALUE_TEXT_FONT_FAMILY_VALUE = "Apple Chancery";
   private static final double VALUE_TEXT_FONT_SIZE_VALUE = 78.0;
   private static final String DESCRIPTION_TEXT_FONT_FAMILY_VALUE = "Apple Color Emoji";
   private static final double DESCRIPTION_TEXT_FONT_SIZE_VALUE = 5.0;
   
   private static final String BACKGROUND_COLOUR_VALUE = "#334db3";
   private static final String TEXT_COLOUR_VALUE = "#ff9966";
   
   @Mock private StatisticsConfigurationModel parseModel;
   @Mock private StatisticsConfigurationModel writeModel;
   private StatisticsConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      when( writeModel.getNumberOfExclusions( Mockito.anyString() ) ).thenReturn( EXCLUSION_COUNT );
      when(writeModel.getExclusion(any(), any())).thenReturn("anything");
      
      systemUnderTest = new StatisticsConfigurationPersistence( parseModel, writeModel );
   }//End Method
   
   @Test public void shouldBuildCorrectStructure() {
      final Object placeholder = systemUnderTest.structure().placeholder();
      
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      assertThat( object.keySet(), containsInAnyOrder( STATISTICS ) );
      
      JSONObject statistics = object.getJSONObject( STATISTICS );
      
      JSONArray exclusions = statistics.getJSONArray( EXCLUSIONS );
      assertThat( exclusions.length(), is( EXCLUSION_COUNT ) );
      
      JSONObject fonts = statistics.getJSONObject( FONTS );
      assertThat( fonts.keySet(), containsInAnyOrder( 
               VALUE_TEXT_FAMILY, 
               VALUE_TEXT_SIZE,
               DESCRIPTION_TEXT_FAMILY, 
               DESCRIPTION_TEXT_SIZE
      ) );
      assertThat( fonts.get( VALUE_TEXT_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( VALUE_TEXT_SIZE ), is( placeholder ) );
      assertThat( fonts.get( DESCRIPTION_TEXT_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( DESCRIPTION_TEXT_SIZE ), is( placeholder ) );
      
      JSONObject colours = statistics.getJSONObject( COLOURS );
      assertThat( colours.keySet(), containsInAnyOrder( 
               BACKGROUND_COLOUR, 
               TEXT_COLOUR
      ) );
      assertThat( colours.get( BACKGROUND_COLOUR ), is( placeholder ) );
      assertThat( colours.get( TEXT_COLOUR ), is( placeholder ) );
   }//End Method
   
   @Test public void readShouldInvokeHandles(){
      String input = new IoCommon().readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      
      systemUnderTest.readHandles().parse( object );
      
      verify( parseModel ).startReadingExclusions( EXCLUSIONS );
      verify( parseModel ).setExclusion( EXCLUSIONS, FIRST_EXCLUSION );
      verify( parseModel ).setExclusion( EXCLUSIONS, SECOND_EXCLUSION );
      verify( parseModel ).setExclusion( EXCLUSIONS, THIRD_EXCLUSION );
      
      verify( parseModel ).setValueTextFontFamily( VALUE_TEXT_FAMILY, VALUE_TEXT_FONT_FAMILY_VALUE );
      verify( parseModel ).setValueTextFontSize( VALUE_TEXT_SIZE, VALUE_TEXT_FONT_SIZE_VALUE );
      verify( parseModel ).setDescriptionTextFontFamily( DESCRIPTION_TEXT_FAMILY, DESCRIPTION_TEXT_FONT_FAMILY_VALUE );
      verify( parseModel ).setDescriptionTextFontSize( DESCRIPTION_TEXT_SIZE, DESCRIPTION_TEXT_FONT_SIZE_VALUE );
      
      verify( parseModel ).setBackgroundColour( BACKGROUND_COLOUR, BACKGROUND_COLOUR_VALUE );
      verify( parseModel ).setTextColour( TEXT_COLOUR, TEXT_COLOUR_VALUE );
      
      verifyNoMoreInteractions( parseModel );
   }//End Method

   @Test public void writeShouldInvokeHandles(){
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      systemUnderTest.writeHandles().parse( object );
      
      verify( writeModel ).getNumberOfExclusions( EXCLUSIONS );
      
      verify( writeModel ).startWritingExclusions( EXCLUSIONS );
      verify( writeModel ).getExclusion( EXCLUSIONS, 0 );
      verify( writeModel ).getExclusion( EXCLUSIONS, 1 );
      verify( writeModel ).getExclusion( EXCLUSIONS, 2 );
      
      verify( writeModel ).getValueTextFontFamily( VALUE_TEXT_FAMILY );
      verify( writeModel ).getValueTextFontSize( VALUE_TEXT_SIZE );
      verify( writeModel ).getDescriptionTextFontFamily( DESCRIPTION_TEXT_FAMILY );
      verify( writeModel ).getDescriptionTextFontSize( DESCRIPTION_TEXT_SIZE );
      
      verify( writeModel ).getBackgroundColour( BACKGROUND_COLOUR );
      verify( writeModel ).getTextColour( TEXT_COLOUR );
      
      verifyNoMoreInteractions( writeModel );
   }//End Method
   
   @Test public void regularConstructorShouldCreateModelFromParameters(){
      systemUnderTest = new StatisticsConfigurationPersistence( new StatisticsConfiguration() );
      assertThat( systemUnderTest.structure(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.readHandles(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.writeHandles(), is( not( nullValue() ) ) );
   }//End Method

}//End Class
