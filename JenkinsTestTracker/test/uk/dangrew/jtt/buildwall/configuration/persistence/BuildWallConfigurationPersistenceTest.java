/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.BUILD_NUMBER_COLOUR;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.BUILD_NUMBER_FAMILY;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.BUILD_NUMBER_SIZE;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.BUILD_TIME_COLOUR;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.BUILD_TIME_FAMILY;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.BUILD_TIME_SIZE;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.BUILD_WALL;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.COLOURS;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.COLUMNS;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.DESCRIPTION_TYPE;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.DETAIL_COLOUR;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.DETAIL_FAMILY;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.DETAIL_SIZE;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.DIMENSIONS;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.FONTS;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.JOB_NAME;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.JOB_NAME_COLOUR;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.JOB_NAME_FAMILY;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.JOB_NAME_SIZE;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.JOB_POLICIES;
import static uk.dangrew.jtt.buildwall.configuration.persistence.BuildWallConfigurationPersistence.POLICY;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.BuildWallJobPolicy;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link BuildWallConfigurationPersistence} test.
 */
public class BuildWallConfigurationPersistenceTest {

   private static final int COLUMNS_VALUE = 6;
   private static final JobPanelDescriptionProviders DESCRIPTION_TYPE_VALUE = JobPanelDescriptionProviders.Detailed;
   
   private static final int JOB_COUNT = 3;
   private static final String JUPA_JOB_NAME_VALUE = "JUPA";
   private static final BuildWallJobPolicy JUPA_JOB_POLICY_VALUE = BuildWallJobPolicy.OnlyShowFailures;
   private static final String JTT_JOB_NAME_VALUE = "JTT";
   private static final BuildWallJobPolicy JTT_JOB_POLICY_VALUE = BuildWallJobPolicy.AlwaysShow;
   private static final String DIGEST_JOB_NAME_VALUE = "Digest";
   private static final BuildWallJobPolicy DIGEST_JOB_POLICY_VALUE = BuildWallJobPolicy.OnlyShowPassing;
   
   private static final String JOB_NAME_FONT_FAMILY_VALUE = "Arial";
   private static final double JOB_NAME_FONT_SIZE_VALUE = 10.0;
   private static final String BUILD_NUMBER_FONT_FAMILY_VALUE = "Courier";
   private static final double BUILD_NUMBER_FONT_SIZE_VALUE = 20.0;
   private static final String COMPLETION_ESTIMATE_FONT_FAMILY_VALUE = "Georgia";
   private static final double COMPLETION_ESTIMATE_FONT_SIZE_VALUE = 45.0;
   private static final String DETAIL_FONT_FAMILY_VALUE = "Helvetica";
   private static final double DETAIL_FONT_SIZE_VALUE = 5.0;
   
   private static final String JOB_NAME_COLOUR_VALUE = "#ff0000";
   private static final String BUILD_NUMBER_COLOUR_VALUE = "#008b8b";
   private static final String COMPLETION_ESTIMATE_COLOUR_VALUE = "#7fffd4";
   private static final String DETAIL_COLOUR_VALUE = "#ff6347";
   
   @Mock private BuildWallConfigurationModel model;
   private BuildWallConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      when( model.getNumberOfJobs( Mockito.anyString() ) ).thenReturn( JOB_COUNT );
      
      systemUnderTest = new BuildWallConfigurationPersistence( model );
   }//End Method
   
   @Test public void shouldBuildCorrectStructure() {
      final Object placeholder = systemUnderTest.structure().placeholder();
      
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      assertThat( object.keySet(), containsInAnyOrder( BUILD_WALL ) );
      
      JSONObject buildWall = object.getJSONObject( BUILD_WALL );
      assertThat( buildWall.keySet(), containsInAnyOrder( DIMENSIONS, JOB_POLICIES, FONTS, COLOURS ) );
      
      JSONObject dimensions = buildWall.getJSONObject( DIMENSIONS );
      assertThat( dimensions.keySet(), containsInAnyOrder( COLUMNS, DESCRIPTION_TYPE ) );
      assertThat( dimensions.get( COLUMNS ), is( placeholder ) );
      assertThat( dimensions.get( DESCRIPTION_TYPE ), is( placeholder ) );
      
      JSONArray policies = buildWall.getJSONArray( JOB_POLICIES );
      for ( int i = 0; i < JOB_COUNT; i++ ) {
         JSONObject policy = policies.getJSONObject( i );
         assertThat( policy.keySet(), containsInAnyOrder( JOB_NAME, POLICY ) );
         assertThat( policy.get( JOB_NAME ), is( placeholder ) );
         assertThat( policy.get( POLICY ), is( placeholder ) );
      }
      
      JSONObject fonts = buildWall.getJSONObject( FONTS );
      assertThat( fonts.keySet(), containsInAnyOrder( 
               JOB_NAME_FAMILY, JOB_NAME_SIZE,
               BUILD_NUMBER_FAMILY, BUILD_NUMBER_SIZE,
               BUILD_TIME_FAMILY, BUILD_TIME_SIZE,
               DETAIL_FAMILY, DETAIL_SIZE
      ) );
      assertThat( fonts.get( JOB_NAME_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( JOB_NAME_SIZE ), is( placeholder ) );
      assertThat( fonts.get( BUILD_NUMBER_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( BUILD_NUMBER_SIZE ), is( placeholder ) );
      assertThat( fonts.get( BUILD_TIME_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( BUILD_TIME_SIZE ), is( placeholder ) );
      assertThat( fonts.get( DETAIL_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( DETAIL_SIZE ), is( placeholder ) );
      
      JSONObject colours = buildWall.getJSONObject( COLOURS );
      assertThat( colours.keySet(), containsInAnyOrder( 
               JOB_NAME_COLOUR, BUILD_NUMBER_COLOUR, BUILD_TIME_COLOUR, DETAIL_COLOUR 
      ) );
      assertThat( colours.get( JOB_NAME_COLOUR ), is( placeholder ) );
      assertThat( colours.get( BUILD_NUMBER_COLOUR ), is( placeholder ) );
      assertThat( colours.get( BUILD_TIME_COLOUR ), is( placeholder ) );
      assertThat( colours.get( DETAIL_COLOUR ), is( placeholder ) );
   }//End Method
   
   @Test public void readShouldInvokeHandles(){
      String input = TestCommon.readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      
      systemUnderTest.readHandles().parse( object );
      
      verify( model ).setColumns( COLUMNS, COLUMNS_VALUE );
      verify( model ).setDescriptionType( DESCRIPTION_TYPE, DESCRIPTION_TYPE_VALUE );
      
      verify( model ).startWritingJobs( JOB_POLICIES );
      verify( model ).setJobName( JOB_NAME, JUPA_JOB_NAME_VALUE );
      verify( model ).setJobPolicy( POLICY, JUPA_JOB_POLICY_VALUE );
      verify( model ).setJobName( JOB_NAME, JTT_JOB_NAME_VALUE );
      verify( model ).setJobPolicy( POLICY, JTT_JOB_POLICY_VALUE );
      verify( model ).setJobName( JOB_NAME, DIGEST_JOB_NAME_VALUE );
      verify( model ).setJobPolicy( POLICY, DIGEST_JOB_POLICY_VALUE );
      
      verify( model ).setJobNameFontFamily( JOB_NAME_FAMILY, JOB_NAME_FONT_FAMILY_VALUE );
      verify( model ).setJobNameFontSize( JOB_NAME_SIZE, JOB_NAME_FONT_SIZE_VALUE );
      verify( model ).setBuildNumberFontFamily( BUILD_NUMBER_FAMILY, BUILD_NUMBER_FONT_FAMILY_VALUE );
      verify( model ).setBuildNumberFontSize( BUILD_NUMBER_SIZE, BUILD_NUMBER_FONT_SIZE_VALUE );
      verify( model ).setCompletionEstimateFontFamily( BUILD_TIME_FAMILY, COMPLETION_ESTIMATE_FONT_FAMILY_VALUE );
      verify( model ).setCompletionEstimateFontSize( BUILD_TIME_SIZE, COMPLETION_ESTIMATE_FONT_SIZE_VALUE );
      verify( model ).setDetailFontFamily( DETAIL_FAMILY, DETAIL_FONT_FAMILY_VALUE );
      verify( model ).setDetailFontSize( DETAIL_SIZE, DETAIL_FONT_SIZE_VALUE );
      
      verify( model ).setJobNameFontColour( JOB_NAME_COLOUR, JOB_NAME_COLOUR_VALUE );
      verify( model ).setBuildNumberFontColour( BUILD_NUMBER_COLOUR, BUILD_NUMBER_COLOUR_VALUE );
      verify( model ).setCompletionEstimateFontColour( BUILD_TIME_COLOUR, COMPLETION_ESTIMATE_COLOUR_VALUE );
      verify( model ).setDetailFontColour( DETAIL_COLOUR, DETAIL_COLOUR_VALUE );
      
      verifyNoMoreInteractions( model );
   }//End Method
   
   @Test public void writeShouldInvokeHandles(){
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      systemUnderTest.writeHandles().parse( object );
      
      verify( model ).getNumberOfJobs( JOB_POLICIES );
      verify( model ).getColumns( COLUMNS );
      verify( model ).getDescriptionType( DESCRIPTION_TYPE );
      
      verify( model ).startParsingJobs( JOB_POLICIES );
      verify( model, times( 3 ) ).getJobName( JOB_NAME );
      verify( model, times( 3 ) ).getJobPolicy( POLICY );
                      
      verify( model ).getJobNameFontFamily( JOB_NAME_FAMILY );
      verify( model ).getJobNameFontSize( JOB_NAME_SIZE );
      verify( model ).getBuildNumberFontFamily( BUILD_NUMBER_FAMILY );
      verify( model ).getBuildNumberFontSize( BUILD_NUMBER_SIZE );
      verify( model ).getCompletionEstimateFontFamily( BUILD_TIME_FAMILY );
      verify( model ).getCompletionEstimateFontSize( BUILD_TIME_SIZE );
      verify( model ).getDetailFontFamily( DETAIL_FAMILY );
      verify( model ).getDetailFontSize( DETAIL_SIZE );
                      
      verify( model ).getJobNameFontColour( JOB_NAME_COLOUR );
      verify( model ).getBuildNumberFontColour( BUILD_NUMBER_COLOUR );
      verify( model ).getCompletionEstimateFontColour( BUILD_TIME_COLOUR );
      verify( model ).getDetailFontColour( DETAIL_COLOUR );
      
      verifyNoMoreInteractions( model );
   }//End Method
   
   @Test public void regularConstructorShouldCreateModelFromParameters(){
      systemUnderTest = new BuildWallConfigurationPersistence( new BuildWallConfigurationImpl(), new JenkinsDatabaseImpl() );
      assertThat( systemUnderTest.structure(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.readHandles(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.writeHandles(), is( not( nullValue() ) ) );
   }//End Method

}//End Class
