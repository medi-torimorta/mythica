package medi.makiba.mythica.compat.create;

import com.simibubi.create.api.contraption.train.PortalTrackProvider;
import com.simibubi.create.content.trains.track.AllPortalTracks;

import medi.makiba.mythica.registry.MythicaBlocks;
import medi.makiba.mythica.worldgen.dimension.MythicaDimensions;
import net.createmod.catnip.math.BlockFace;
import net.minecraft.core.BlockPos;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;


public class CreateCompat {
    public static void register() {
         AllPortalTracks.tryRegisterIntegration(MythicaBlocks.MYTHICA_PORTAL.getId(),
            (level, face) -> new MythicaPortalTrackProvider().findExit(level, face));
    }

    public static class MythicaPortalTrackProvider implements PortalTrackProvider {

        @Override
        public Exit findExit(ServerLevel level, BlockFace face) {
            BlockPos portalPos = face.getConnectedPos();
		    BlockState portalState = level.getBlockState(portalPos);
            Block portalBlock = portalState.getBlock();
            if (!(portalBlock instanceof Portal)) {
                return null;
            }
            Portal portal = (Portal) portalBlock;

            return AllPortalTracks.fromPortal(level, face, Level.OVERWORLD, MythicaDimensions.MYTHICA_DIM, portal);
        }
    }
}
 