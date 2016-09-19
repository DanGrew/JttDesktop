/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.mc.view.tree.NotificationEvent;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link BuildResultStatusNotifier} is responsible for notifying changes to the
 * {@link uk.dangrew.jtt.model.jobs.BuildResultStatus} on {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s.
 */
public class BuildResultStatusNotifier {

   private final NotificationEvent events;
   private final JenkinsDatabase database;
   
   /**
    * Constructs a new {@link BuildResultStatusNotifier}.
    * @param database the {@link JenkinsDatabase} to monitor.
    */
   public BuildResultStatusNotifier( JenkinsDatabase database ) {
      this.database = database;
      
      this.events = new NotificationEvent();
      this.database.jenkinsJobProperties().addBuildResultStatusListener( 
               ( job, old, updated ) -> events.fire( 
                        new Event<>( new BuildResultStatusNotification( job, old, updated ) ) 
               ) 
      );
   }//End Constructor
   
}//End Class
