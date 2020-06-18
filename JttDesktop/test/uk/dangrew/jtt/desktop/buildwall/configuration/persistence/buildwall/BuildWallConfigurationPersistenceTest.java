/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.BUILD_NUMBER_COLOUR;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.BUILD_NUMBER_FAMILY;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.BUILD_NUMBER_SIZE;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.BUILD_WALL;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.COLOURS;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.COLUMNS;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.COMPLETION_ESTIMATE_COLOUR;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.COMPLETION_ESTIMATE_FAMILY;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.COMPLETION_ESTIMATE_SIZE;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.DESCRIPTION_TYPE;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.DETAIL_COLOUR;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.DETAIL_FAMILY;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.DETAIL_SIZE;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.DIMENSIONS;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.FONTS;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.JOB_NAME;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.JOB_NAME_COLOUR;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.JOB_NAME_FAMILY;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.JOB_NAME_SIZE;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.JOB_POLICIES;
import static uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationPersistence.POLICY;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.desktop.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.utility.io.IoCommon;

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
   
   @Mock private BuildWallConfigurationModel parseModel;
   @Mock private BuildWallConfigurationModel writeModel;
   private BuildWallConfigurationPersistence systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      when( writeModel.getNumberOfJobs( Mockito.anyString() ) ).thenReturn( JOB_COUNT );
      
      systemUnderTest = new BuildWallConfigurationPersistence( parseModel, writeModel );
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
               COMPLETION_ESTIMATE_FAMILY, COMPLETION_ESTIMATE_SIZE,
               DETAIL_FAMILY, DETAIL_SIZE
      ) );
      assertThat( fonts.get( JOB_NAME_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( JOB_NAME_SIZE ), is( placeholder ) );
      assertThat( fonts.get( BUILD_NUMBER_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( BUILD_NUMBER_SIZE ), is( placeholder ) );
      assertThat( fonts.get( COMPLETION_ESTIMATE_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( COMPLETION_ESTIMATE_SIZE ), is( placeholder ) );
      assertThat( fonts.get( DETAIL_FAMILY ), is( placeholder ) );
      assertThat( fonts.get( DETAIL_SIZE ), is( placeholder ) );
      
      JSONObject colours = buildWall.getJSONObject( COLOURS );
      assertThat( colours.keySet(), containsInAnyOrder( 
               JOB_NAME_COLOUR, BUILD_NUMBER_COLOUR, COMPLETION_ESTIMATE_COLOUR, DETAIL_COLOUR 
      ) );
      assertThat( colours.get( JOB_NAME_COLOUR ), is( placeholder ) );
      assertThat( colours.get( BUILD_NUMBER_COLOUR ), is( placeholder ) );
      assertThat( colours.get( COMPLETION_ESTIMATE_COLOUR ), is( placeholder ) );
      assertThat( colours.get( DETAIL_COLOUR ), is( placeholder ) );
   }//End Method
   
   @Test public void readShouldInvokeHandles(){
      String input = new IoCommon().readFileIntoString( getClass(), "sample-config.json" );
      JSONObject object = new JSONObject( input );
      
      systemUnderTest.readHandles().parse( object );
      
      verify( parseModel ).setColumns( COLUMNS, COLUMNS_VALUE );
      verify( parseModel ).setDescriptionType( DESCRIPTION_TYPE, DESCRIPTION_TYPE_VALUE );
      
      verify( parseModel ).startParsingJobs( JOB_POLICIES );
      verify( parseModel ).setJobName( JOB_NAME, JUPA_JOB_NAME_VALUE );
      verify( parseModel ).setJobPolicy( POLICY, JUPA_JOB_POLICY_VALUE );
      verify( parseModel ).setJobName( JOB_NAME, JTT_JOB_NAME_VALUE );
      verify( parseModel ).setJobPolicy( POLICY, JTT_JOB_POLICY_VALUE );
      verify( parseModel ).setJobName( JOB_NAME, DIGEST_JOB_NAME_VALUE );
      verify( parseModel ).setJobPolicy( POLICY, DIGEST_JOB_POLICY_VALUE );
      
      verify( parseModel ).setJobNameFontFamily( JOB_NAME_FAMILY, JOB_NAME_FONT_FAMILY_VALUE );
      verify( parseModel ).setJobNameFontSize( JOB_NAME_SIZE, JOB_NAME_FONT_SIZE_VALUE );
      verify( parseModel ).setBuildNumberFontFamily( BUILD_NUMBER_FAMILY, BUILD_NUMBER_FONT_FAMILY_VALUE );
      verify( parseModel ).setBuildNumberFontSize( BUILD_NUMBER_SIZE, BUILD_NUMBER_FONT_SIZE_VALUE );
      verify( parseModel ).setCompletionEstimateFontFamily( COMPLETION_ESTIMATE_FAMILY, COMPLETION_ESTIMATE_FONT_FAMILY_VALUE );
      verify( parseModel ).setCompletionEstimateFontSize( COMPLETION_ESTIMATE_SIZE, COMPLETION_ESTIMATE_FONT_SIZE_VALUE );
      verify( parseModel ).setDetailFontFamily( DETAIL_FAMILY, DETAIL_FONT_FAMILY_VALUE );
      verify( parseModel ).setDetailFontSize( DETAIL_SIZE, DETAIL_FONT_SIZE_VALUE );
      
      verify( parseModel ).setJobNameFontColour( JOB_NAME_COLOUR, JOB_NAME_COLOUR_VALUE );
      verify( parseModel ).setBuildNumberFontColour( BUILD_NUMBER_COLOUR, BUILD_NUMBER_COLOUR_VALUE );
      verify( parseModel ).setCompletionEstimateFontColour( COMPLETION_ESTIMATE_COLOUR, COMPLETION_ESTIMATE_COLOUR_VALUE );
      verify( parseModel ).setDetailFontColour( DETAIL_COLOUR, DETAIL_COLOUR_VALUE );
      
      verifyNoMoreInteractions( parseModel );
   }//End Method
   
   @Test public void writeShouldInvokeHandles(){
      JSONObject object = new JSONObject();
      systemUnderTest.structure().build( object );
      systemUnderTest.writeHandles().parse( object );
      
      verify( writeModel ).getNumberOfJobs( JOB_POLICIES );
      verify( writeModel ).getColumns( COLUMNS );
      verify( writeModel ).getDescriptionType( DESCRIPTION_TYPE );
      
      verify( writeModel ).startWritingJobs( JOB_POLICIES );
      verify( writeModel, times( 3 ) ).getJobName( JOB_NAME );
      verify( writeModel, times( 3 ) ).getJobPolicy( POLICY );
                      
      verify( writeModel ).getJobNameFontFamily( JOB_NAME_FAMILY );
      verify( writeModel ).getJobNameFontSize( JOB_NAME_SIZE );
      verify( writeModel ).getBuildNumberFontFamily( BUILD_NUMBER_FAMILY );
      verify( writeModel ).getBuildNumberFontSize( BUILD_NUMBER_SIZE );
      verify( writeModel ).getCompletionEstimateFontFamily( COMPLETION_ESTIMATE_FAMILY );
      verify( writeModel ).getCompletionEstimateFontSize( COMPLETION_ESTIMATE_SIZE );
      verify( writeModel ).getDetailFontFamily( DETAIL_FAMILY );
      verify( writeModel ).getDetailFontSize( DETAIL_SIZE );
                      
      verify( writeModel ).getJobNameFontColour( JOB_NAME_COLOUR );
      verify( writeModel ).getBuildNumberFontColour( BUILD_NUMBER_COLOUR );
      verify( writeModel ).getCompletionEstimateFontColour( COMPLETION_ESTIMATE_COLOUR );
      verify( writeModel ).getDetailFontColour( DETAIL_COLOUR );
      
      verifyNoMoreInteractions( writeModel );
   }//End Method
   
   @Test public void regularConstructorShouldCreateModelFromParameters(){
      systemUnderTest = new BuildWallConfigurationPersistence( new BuildWallConfigurationImpl(), new TestJenkinsDatabaseImpl() );
      assertThat( systemUnderTest.structure(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.readHandles(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.writeHandles(), is( not( nullValue() ) ) );
   }//End Method

}//End Class
