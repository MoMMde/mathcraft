package xyz.mommde.mathcraft.util

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.*
import kotlin.math.sin

class FlatWorldGenerator(val material: Material) : ChunkGenerator() {
    override fun generateSurface(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {
        for(x in 0..15) for (z in 0..15) chunkData.setBlock(x, 0, z, material)
    }
}
