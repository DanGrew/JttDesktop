/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

import uk.dangrew.jtt.event.structure.EventManager;
import uk.dangrew.jtt.event.structure.EventSubscription;

/**
 * The {@link JobBuiltEvent} provides a {@link uk.dangrew.jtt.event.structure.Event} with a
 * {@link JobBuiltResult} when a {@link uk.dangrew.jtt.model.jobs.JenkinsJob} has completed.
 */
public class JobBuiltEvent extends EventManager< JobBuiltResult > {

   private static final Collection< EventSubscription< JobBuiltResult > > subscriptions = 
            new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link JobBuiltEvent}.
    */
   public JobBuiltEvent() {
      super( subscriptions, lock );
   }//End Constructor
}//End Class
