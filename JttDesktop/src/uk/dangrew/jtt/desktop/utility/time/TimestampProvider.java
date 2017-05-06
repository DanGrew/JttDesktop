/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.utility.time;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * Bespoke interface for a {@link Supplier} of {@link LocalDateTime}.
 */
public interface TimestampProvider extends Supplier< LocalDateTime >{}
