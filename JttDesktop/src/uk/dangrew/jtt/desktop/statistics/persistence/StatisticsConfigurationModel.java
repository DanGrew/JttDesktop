/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.persistence;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.utility.conversion.ColorConverter;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * The {@link StatisticsConfigurationModel} provides the model to read from and write to when converting
 * a {@link StatisticsConfiguration} to and from json data.
 */
class StatisticsConfigurationModel {
   
   private final StatisticsConfiguration configuration;
   private final JenkinsDatabase database;
   private final ColorConverter colorConverter;
   
   private final List< JenkinsJob > excludedJobs;
   
   /**
    * Constructs a new {@link StatisticsConfigurationModel}.
    * @param configuration the {@link StatisticsConfiguration} to model.
    */
   StatisticsConfigurationModel( StatisticsConfiguration configuration ) {
      this( configuration, new SystemWideJenkinsDatabaseImpl().get() );
   }//End Constructor
   
   /**
    * Constructs a new {@link StatisticsConfigurationModel}.
    * @param configuration the {@link StatisticsConfiguration} being serialized.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsJob}s.
    */
   StatisticsConfigurationModel( StatisticsConfiguration configuration, JenkinsDatabase database ) {
      if ( configuration == null || database == null ) {
         throw new NullPointerException( "Arguments must not be null." );
      }
      
      this.configuration = configuration;
      this.database = database;
      this.colorConverter = new ColorConverter();
      
      this.excludedJobs = new ArrayList<>();
   }//End Constructor

   /**
    * Setter for {@link StatisticsConfiguration#statisticBackgroundColourProperty()}.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setBackgroundColour( String key, String value ) {
      setColourProperty( configuration.statisticBackgroundColourProperty(), value );
   }//End Method
   
   /**
    * Setter for {@link StatisticsConfiguration#statisticTextColourProperty()}.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setTextColour( String key, String value ) {
      setColourProperty( configuration.statisticTextColourProperty(), value );
   }//End Method
   
   /**
    * Setter for {@link StatisticsConfiguration#statisticValueTextFontProperty()} family.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setValueTextFontFamily( String key, String value ) {
      setPropertyFontFamily( configuration.statisticValueTextFontProperty(), value );
   }//End Method
   
   /**
    * Setter for {@link StatisticsConfiguration#statisticValueTextFontProperty()} size.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setValueTextFontSize( String key, Double value ) {
      setPropertyFontSize( configuration.statisticValueTextFontProperty(), value );
   }//End Method
   
   /**
    * Setter for {@link StatisticsConfiguration#statisticDescriptionTextFontProperty()} family.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setDescriptionTextFontFamily( String key, String value ) {
      setPropertyFontFamily( configuration.statisticDescriptionTextFontProperty(), value );
   }//End Method
   
   /**
    * Setter for {@link StatisticsConfiguration#statisticDescriptionTextFontProperty()} size.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setDescriptionTextFontSize( String key, Double value ) {
      setPropertyFontSize( configuration.statisticDescriptionTextFontProperty(), value );
   }//End Method
   
   /**
    * Getter for the number of exclusions currently held.
    * @param key the count is for.
    * @return the number of exclusions.
    */
   Integer getNumberOfExclusions( String key ) {
      return configuration.excludedJobs().size();
   }//End Method
   
   /**
    * Method to clear the buffers used when reading and writing.
    */
   private void clearBuffers(){
      excludedJobs.clear();
   }//End Method
   
   /**
    * Method to prepare the data for reading the exclusions.
    * @param keyString the parsed key.
    */
   void startReadingExclusions( String keyString ) {
      configuration.excludedJobs().clear();
   }//End Method
   
   /**
    * Method to set an exclusion in the {@link StatisticsConfiguration}.
    * @param keyString the parsed key.
    * @param value the parsed value.
    */
   void setExclusion( String key, String value ) {
      JenkinsJob job = database.getJenkinsJob( value );
      if ( job == null ) {
         job = new JenkinsJobImpl( value );
         database.store( job );
      }
      
      configuration.excludedJobs().add( job );
   }//End Method
   
   /**
    * Method to prepare the exclusions for writing.
    * @param keyString the parsed key.
    */
   void startWritingExclusions( String keyString ) {
      clearBuffers();
      
      Set< JenkinsJob > reducedSet = new LinkedHashSet<>();
      configuration.excludedJobs().forEach( reducedSet::add );
      excludedJobs.addAll( reducedSet );
   }//End Method
   
   /**
    * Getter for the next exclusion to write.
    * @param key the parsed key.
    * @param index the index of the item in the array.
    * @return the name of the {@link JenkinsJob} excluded.
    */
   String getExclusion( String key, Integer index ){
      if ( excludedJobs.isEmpty() ) {
         return null;
      }
      return excludedJobs.remove( 0 ).nameProperty().get();
   }//End Method
   
   /**
    * Method to generally set the family of a {@link Font} {@link ObjectProperty}.
    * @param property the {@link Font} {@link ObjectProperty}.
    * @param family the family to use.
    */
   private void setPropertyFontFamily( ObjectProperty< Font > property, String family ) {
      Font current = property.get();
      property.set( Font.font( family, current.getSize() ) );
   }//End Method
   
   /**
    * Method to generally set the size of the {@link Font} in the {@link ObjectProperty} given.
    * @param property the {@link Font} {@link ObjectProperty} to set the size.
    * @param size the size to set.
    */
   private void setPropertyFontSize( ObjectProperty< Font > property, Double size ) {
      Font current = property.get();
      property.set( Font.font( current.getFamily(), size ) );
   }//End Method
   
   /**
    * Method to generally set the {@link Color} on an {@link ObjectProperty} by converting the hex.
    * @param property the {@link ObjectProperty} to set on.
    * @param value the hex representation.
    */
   private void setColourProperty( ObjectProperty< Color > property, String value ) {
      property.set( colorConverter.hexToColor( value ) );
   }//End Method
   
   /**
    * Getter for {@link StatisticsConfiguration#statisticValueTextFontProperty()} family.
    * @param key the parsed key.
    * @return the value.
    */
   String getValueTextFontFamily( String key ) {
      return configuration.statisticValueTextFontProperty().get().getFamily();
   }//End Method
   
   /**
    * Getter for {@link StatisticsConfiguration#statisticDescriptionTextFontProperty()} family.
    * @param key the parsed key.
    * @return the value.
    */
   String getDescriptionTextFontFamily( String key ) {
      return configuration.statisticDescriptionTextFontProperty().get().getFamily();
   }//End Method
   
   /**
    * Getter for {@link StatisticsConfiguration#statisticValueTextFontProperty()} size.
    * @param key the parsed key.
    * @return the value.
    */
   Double getValueTextFontSize( String key ) {
      return configuration.statisticValueTextFontProperty().get().getSize();
   }//End Method
   
   /**
    * Getter for {@link StatisticsConfiguration#statisticDescriptionTextFontProperty()} size.
    * @param key the parsed key.
    * @return the value.
    */
   Double getDescriptionTextFontSize( String key ) {
      return configuration.statisticDescriptionTextFontProperty().get().getSize();
   }//End Method
   
   /**
    * Getter for {@link StatisticsConfiguration#statisticBackgroundColourProperty()} family.
    * @param key the parsed key.
    * @return the value.
    */
   String getBackgroundColour( String key ) {
      return colorConverter.colorToHex( configuration.statisticBackgroundColourProperty().get() );
   }//End Method

   /**
    * Getter for {@link StatisticsConfiguration#statisticTextColourProperty()} family.
    * @param key the parsed key.
    * @return the value.
    */
   String getTextColour( String key ) {
      return colorConverter.colorToHex( configuration.statisticTextColourProperty().get() );
   }//End Method
   
}//End Class
