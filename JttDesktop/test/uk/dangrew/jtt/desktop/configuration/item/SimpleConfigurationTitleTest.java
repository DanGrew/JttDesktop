/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.item;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * {@link SimpleConfigurationTitle} test.
 */
public class SimpleConfigurationTitleTest {
   
   private static final String TITLE = "some special title";
   private static final String DESCRIPTION = "some description that should be quite long";
   
   private Label boldLabel;
   private Label wrappedLabel;
   @Mock private JavaFxStyle styling;
   private SimpleConfigurationTitle systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      boldLabel = new Label();
      wrappedLabel = new Label();
      
      when( styling.createBoldLabel( TITLE ) ).thenReturn( boldLabel );
      when( styling.createWrappedTextLabel( DESCRIPTION ) ).thenReturn( wrappedLabel );
      
      systemUnderTest = new SimpleConfigurationTitle( TITLE, DESCRIPTION, styling );
   }//End Method

   @Test public void shouldProvideBoldTextTitleAlignedCenter() {
      assertThat( systemUnderTest.getChildren().get( 0 ), is( boldLabel ) );
      assertThat( boldLabel.getAlignment(), is( Pos.CENTER_LEFT ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullTitle(){
      systemUnderTest = new SimpleConfigurationTitle( null, DESCRIPTION );
   }//End Method
   
   @Test public void shouldProvideDescriptionUnderneathTitleThatIsWrapped(){
      assertThat( systemUnderTest.getChildren().get( 1 ), is( wrappedLabel ) );
   }//End Method
   
   @Test public void shouldIgnoreDescriptionIfNull(){
      systemUnderTest = new SimpleConfigurationTitle( TITLE, null );
      assertThat( systemUnderTest.getChildren(), hasSize( 2 ) );
   }//End Method
   
   @Test public void shouldUseSpacingBetweenElementsAndEndWithASeparator(){
      assertThat( systemUnderTest.getSpacing(), is( closeTo( SimpleConfigurationTitle.SPACING, TestCommon.precision() ) ) );
      assertThat( systemUnderTest.getChildren().get( 2 ), is( instanceOf( Separator.class ) ) );
      assertThat( systemUnderTest.getChildren(), hasSize( 3 ) );
   }//End Method

}//End Class
