package name.modid.beton.storage.logic.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class Check {
    public static boolean check(Location location) {
        List<Block> touching = new ArrayList<>();
        Block centralBlock = location.getBlock();
        touching.add(centralBlock.getRelative(BlockFace.UP));
        touching.add(centralBlock.getRelative(BlockFace.DOWN));
        touching.add(centralBlock.getRelative(BlockFace.NORTH));
        touching.add(centralBlock.getRelative(BlockFace.SOUTH));
        touching.add(centralBlock.getRelative(BlockFace.EAST));
        touching.add(centralBlock.getRelative(BlockFace.WEST));
        if (centralBlock.getType().equals(Material.BLACK_CONCRETE)) {
            return true;
        }
        for (Block block : touching) {
            if (block.getType().equals(Material.BLACK_CONCRETE)) {
                return true;
            }
        }
        return false;
    }
}
