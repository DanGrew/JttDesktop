/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.panel;

import uk.dangrew.jtt.desktop.statistics.panel.StatisticView;

/**
 * Implementation for testing the {@link StatisticView} mechanism.
 */
public class StatisticViewImpl implements StatisticView {

   private String value;
   
   /**
    * {@inheritDoc}
    */
   @Override public void setStatisticValue( String value ) {
      this.value = value;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String getStatisticValue() {
      return value;
   }//End Method

}//End Class
