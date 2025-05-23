package io.papermc.paper.event.world;

import io.papermc.paper.math.Position;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called <b>before</b> a set of configured structures is located.
 * This happens when:
 * <ul>
 *     <li>The /locate command is used.<br></li>
 *     <li>An Eye of Ender is used.</li>
 *     <li>An Explorer/Treasure Map is activated.</li>
 *     <li>A dolphin swims to a treasure location.</li>
 *     <li>A trade is done with a villager for a map.</li>
 *     <li>{@link World#locateNearestStructure(Location, StructureType, int, boolean)} is invoked.</li>
 *     <li>{@link World#locateNearestStructure(Location, Structure, int, boolean)} is invoked.</li>
 * </ul>
 */
@NullMarked
public class StructuresLocateEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location origin;
    private @Nullable Result result;
    private List<Structure> structures;
    private int radius;
    private boolean findUnexplored;

    private boolean cancelled;

    @ApiStatus.Internal
    public StructuresLocateEvent(final World world, final Location origin, final List<Structure> structures, final int radius, final boolean findUnexplored) {
        super(world);
        this.origin = origin;
        this.structures = structures;
        this.radius = radius;
        this.findUnexplored = findUnexplored;
    }

    /**
     * Gets the {@link Location} from which the search is to be conducted.
     *
     * @return {@link Location} where search begins
     */
    public Location getOrigin() {
        return this.origin.clone();
    }

    /**
     * Gets the {@link Location} and {@link Structure} set as the result, if it was defined.
     * <p>
     * Returns {@code null} if it has not been set by {@link StructuresLocateEvent#setResult(Result)}.
     * Since this event fires <i>before</i> the search is done, the actual result is unknown at this point.
     *
     * @return The result location and structure, if it has been set. {@code null} if it has not.
     * @see World#locateNearestStructure(Location, StructureType, int, boolean)
     */
    public @Nullable Result getResult() {
        return this.result;
    }

    /**
     * Sets the result {@link Location} and {@link Structure}. This causes the search to be
     * skipped, and the result object passed here to be used as the result.
     *
     * @param result the {@link Location} and {@link Structure} of the search.
     */
    public void setResult(final @Nullable Result result) {
        this.result = result;
    }

    /**
     * Gets an unmodifiable list of Structures that are valid targets for the search.
     *
     * @return an unmodifiable list of Structures
     */
    public @UnmodifiableView List<Structure> getStructures() {
        return Collections.unmodifiableList(this.structures);
    }

    /**
     * Sets the list of Structures that are valid targets for the search.
     *
     * @param structures a list of Structures targets
     */
    public void setStructures(final List<Structure> structures) {
        this.structures = structures;
    }

    /**
     * Gets the search radius in which to attempt locating the structure.
     * <p>
     * This radius may not always be obeyed during the structure search!
     *
     * @return the search radius (in chunks)
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Sets the search radius in which to attempt locating the structure.
     * <p>
     * This radius may not always be obeyed during the structure search!
     *
     * @param radius the search radius (in chunks)
     */
    public void setRadius(final int radius) {
        this.radius = radius;
    }

    /**
     * Gets whether to search exclusively for unexplored structures.
     * <p>
     * As with the search radius, this value is not always obeyed.
     *
     * @return Whether to search for only unexplored structures.
     */
    public boolean shouldFindUnexplored() {
        return this.findUnexplored;
    }

    /**
     * Sets whether to search exclusively for unexplored structures.
     * <p>
     * As with the search radius, this value is not always obeyed.
     *
     * @param findUnexplored Whether to search for only unexplored structures.
     */
    public void setFindUnexplored(final boolean findUnexplored) {
        this.findUnexplored = findUnexplored;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Result for {@link StructuresLocateEvent}.
     */
    public record Result(Position pos, Structure structure) {

        @Deprecated(forRemoval = true)
        public Location position() {
            //noinspection DataFlowIssue
            return this.pos.toLocation(null);
        }
    }
}
