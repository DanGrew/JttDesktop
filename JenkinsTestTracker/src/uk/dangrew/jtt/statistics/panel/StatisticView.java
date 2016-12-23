/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.panel;

/**
 * The {@link StatisticView} provides an interface for applying calculated statistics to a 
 * graphical element.
 */
public interface StatisticView {

   /**
    * Method to set the value of the statistic. It is provided as a {@link String} to provide
    * a flexible interface, that should interact with most ui elements.
    * @param value the representation of the statistic to display.
    */
   public void setStatisticValue( String value );
   
   /**
    * Getter for the statistics current value as displayed.
    * @return the {@link String} value.
    */
   public String getStatisticValue();
   
}//End Interface
