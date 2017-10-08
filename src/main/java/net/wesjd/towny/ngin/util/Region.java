package net.wesjd.towny.ngin.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cuboid box
 */
public class Region {

    /**
     * The two locations that represent the confines of this box
     */
    private Location pos1, pos2;

    /**
     * Constructs a new box with the
     * specified world and boundaries,
     * using doubles
     *
     * @param world The world the Box is in
     * @param x1 The minimum x coordinate
     * @param y1 The minimum y coordinate
     * @param z1 The minimum z coordinate
     * @param x2 The maximum x coordinate
     * @param y2 The maximum y coordinate
     * @param z2 The maximum z coordinate
     */
    public Region(World world, double x1, double y1, double z1, double x2, double y2, double z2) {
        double minX = Math.min(x1, x2), minY = Math.min(y1, y2), minZ = Math.min(z1, z2);

        double maxX = Math.max(x1, x2), maxY = Math.max(y1, y2), maxz = Math.max(z1, z2);

        pos1 = new Location(world, minX, minY, minZ);
        pos2 = new Location(world, maxX, maxY, maxz);
    }

    /**
     * Constructs a new box with the
     * specified world and boundaries,
     * using locations.
     *
     * @param pos1 The {@link Location} first corner
     * @param pos2 The {@link Location} second corner
     */
    public Region(Location pos1, Location pos2) {
        this(pos1.getWorld(), pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    /**
     * Returns an array of all the
     * blocks between the first corner
     * and the second corner.
     *
     * @return A {@link List<Location>} of all blocks inside the boundaries
     */
    public List<Location> getAllBlocks() {
        List<Location> ret = new ArrayList<>();
        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++) {
                for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                    ret.add(new Location(pos1.getWorld(), x, y, z));
                }
            }
        }
        return ret;
    }

    /**
     * Checks whether this box intercepts with another box
     *
     * @param other The other box you're checking
     * @return Whether the two boxes intersect
     */
    public boolean intersectsWith(Region other) {
        return this.intersectsWith(other.pos1.getX(), other.pos1.getY(), other.pos1.getZ(), other.pos2.getX(), other.pos2.getY(), other.pos2.getZ());
    }

    /**
     * Checks whether this box intercepts with two corners
     *
     * @param minX The minimum x coordinate
     * @param minY The minimum y coordinate
     * @param minZ The minimum z coordinate
     * @param maxX The maximum x coordinate
     * @param maxY The maximum y coordinate
     * @param maxZ The maximum z coordinate
     * @return Whether this box and the two corners intercept
     */
    public boolean intersectsWith(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.pos1.getX() < maxX && this.pos2.getX() > minX && this.pos1.getY() < maxY && this.pos2.getY() > minY && this.pos1.getZ() < maxZ && this.pos2.getZ() > minZ;
    }

    /**
     * Checks whether this box intercepts
     * with a players hitbox
     *
     * @param player The player to check
     * @return Whether this box intercepts with the players hitbox
     */
    public boolean intersectsWith(Player player) {
        return playerToBox(player.getLocation()).intersectsWith(this);
    }

    /**
     * Attempts to get an accurate box for a players hitbox
     *
     * @param l The location of the player
     * @return The calculated Box
     */
    private Region playerToBox(Location l) {
        Location pos1 = l.clone().subtract(0.3, 0, 0.3);
        Location pos2 = pos1.clone().add(0.6, 1.8, 0.6);
        return new Region(pos1, pos2);
    }


}