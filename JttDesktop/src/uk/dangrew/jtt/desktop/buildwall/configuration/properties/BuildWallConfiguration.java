/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.properties;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.desktop.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link BuildWallConfiguration} provides a mechanism for configuring the appearance
 * and layout of the build wall.
 */
public interface BuildWallConfiguration {

   /**
    * Property for the {@link Font} for the build number.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > buildNumberFont();
   
   /**
    * Property for the {@link Color} for the build number.
    * @return the {@link ObjectProperty}. 
    */
   public ObjectProperty< Color > buildNumberColour(); 

   /**
    * Property for the {@link Font} for the completion estimate.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > completionEstimateFont();
   
   /**
    * Property for the {@link Color} for the completion estimate.
    * @return the {@link ObjectProperty}. 
    */
   public ObjectProperty< Color > completionEstimateColour(); 
   
   /**
    * Property for the job name {@link Font}.
    * @return the jobNameFont the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > jobNameFont(); 
   
   /**
    * Property for the job name {@link Color}.
    * @return the jobNameColour the {@link ObjectProperty}.
    */
   public ObjectProperty< Color > jobNameColour();

   /**
    * Property for the number of columns in the {@link BuildWall}.
    * @return the number of columns {@link IntegerProperty}.
    */
   public IntegerProperty numberOfColumns();

   /**
    * {@link ObservableMap} for the {@link JenkinsJob}s to their {@link BuildWallJobPolicy}.
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< JenkinsJob, BuildWallJobPolicy > jobPolicies();

   /**
    * {@link ObjectProperty} for the type of {@link JobPanelDescriptionProviders} to use for
    * the {@link JobPanelImpl}.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< JobPanelDescriptionProviders > jobPanelDescriptionProvider();

   /**
    * Property for the detail {@link Font}.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > detailFont(); 
   
   /**
    * Property for the detail {@link Color}.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Color > detailColour();
}//End Interface
