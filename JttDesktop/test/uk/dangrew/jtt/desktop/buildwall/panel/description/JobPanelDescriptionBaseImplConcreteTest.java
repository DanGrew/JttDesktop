/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel.description;

import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

public class JobPanelDescriptionBaseImplConcreteTest extends JobPanelDescriptionBaseImplTest {

   /** Concrete impl to test against. */
   private static class ConcreteDescription extends JobPanelDescriptionBaseImpl {

      protected ConcreteDescription( BuildWallConfiguration configuration, BuildWallTheme theme, JenkinsJob job ) {
         super( configuration, theme, job );
      }//End Constructor

      @Override protected void applyLayout() {}
      @Override protected void applyColumnConstraints() {}
      
   }//End Class
   
   /**
    * {@inheritDoc}
    */
   @Override protected JobPanelDescriptionBaseImpl constructSut() {
      return new ConcreteDescription( configuration, theme, job );
   }//End Method

}//End Class
