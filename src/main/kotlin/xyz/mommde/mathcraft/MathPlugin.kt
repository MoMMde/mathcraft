package xyz.mommde.mathcraft

import net.axay.kspigot.main.KSpigot
import xyz.mommde.mathcraft.commands.command
import xyz.mommde.mathcraft.math.CoordinateSystemRenderer

class MathPlugin : KSpigot() {
    private val renderer = CoordinateSystemRenderer()
    override fun startup() {
        command(renderer)
    }
}
