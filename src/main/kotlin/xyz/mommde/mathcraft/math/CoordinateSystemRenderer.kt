package xyz.mommde.mathcraft.math

import org.bukkit.Material
import xyz.mommde.mathcraft.models.CoordinateSystem
import xyz.mommde.mathcraft.models.forEachX
import xyz.mommde.mathcraft.models.forEachY
import xyz.mommde.mathcraft.models.forEachZ

class CoordinateSystemRenderer {
    val material = Material.BLACK_CONCRETE
    fun renderFirstDimension(system: CoordinateSystem.CoordinateSystem1D) {
        println(1)
        system.forEachX {
            println("x = $it")
            system.place(it, Material.BLUE_WOOL)
        }
    }

    fun renderSecondDimension(system: CoordinateSystem.CoordinateSystem2D) {
        println(2)
        system.forEachY {
            println("y = $it")
            system.place(.0, it, Material.RED_WOOL)
        }
    }

    fun renderThirdDimension(system: CoordinateSystem.CoordinateSystem3D) {
        println("3")
        system.forEachZ {
            system.place(.0, .0, it, Material.GREEN_WOOL)
        }
    }

    fun render(system: CoordinateSystem) {
        if (system is CoordinateSystem.CoordinateSystem1D) renderFirstDimension(system)
        if (system is CoordinateSystem.CoordinateSystem2D) renderSecondDimension(system)
        if (system is CoordinateSystem.CoordinateSystem3D) renderThirdDimension(system)
        system.center.block.type = Material.DIAMOND_BLOCK
    }
}
