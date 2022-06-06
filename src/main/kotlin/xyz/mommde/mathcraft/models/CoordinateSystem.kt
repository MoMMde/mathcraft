package xyz.mommde.mathcraft.models

import org.bukkit.Location
import org.bukkit.Material
import java.util.UUID
import kotlin.math.round

val coordinateSystems = mutableMapOf<UUID, CoordinateSystemStorage>()

sealed interface CoordinateSystem {
    val center: Location
    val markingMaterial: Material
        get() = Material.YELLOW_WOOL

    interface CoordinateSystem1D : CoordinateSystem {
        val minX: Int
        val maxX: Int
        fun place(x: Double, material: Material = markingMaterial) {
            if (insideX(x)) center.clone().add(round(x), .0, .0).block.type = material
        }

        fun insideX(x: Double) = x < maxX && x > minX
    }

    interface CoordinateSystem2D : CoordinateSystem1D {
        val minY: Int
        val maxY: Int
        fun place(x: Double, y: Double, material: Material = markingMaterial) {
            if (insideY(y) && insideX(x)) center.clone().add(round(x), .0, round(y)).block.type = material
        }

        fun insideY(y: Double) = y < maxY && y > minY
    }

    interface CoordinateSystem3D : CoordinateSystem2D {
        val minZ: Int
        val maxZ: Int
        fun place(x: Double, y: Double, z: Double, material: Material = markingMaterial) {
            if (insideX(x) && insideY(y) && insideZ(z)) center.clone().add(round(x), round(z), round(y)).block.type = material
        }

        fun insideZ(z: Double) = z < maxZ && z > minZ
    }
}

fun buildCoordinateSystem(center: Location, vararg minAndMaxs: Pair<Int, Int>): CoordinateSystem {
    return when(minAndMaxs.size) {
        1 -> object : CoordinateSystem.CoordinateSystem1D {
            override val minX: Int = minAndMaxs[0].first
            override val maxX: Int = minAndMaxs[0].second
            override val center: Location = center
        }
        2 -> object : CoordinateSystem.CoordinateSystem2D {
            override val minX: Int = minAndMaxs[0].first
            override val maxX: Int = minAndMaxs[0].second
            override val minY: Int = minAndMaxs[1].first
            override val maxY: Int = minAndMaxs[1].second
            override val center: Location = center
        }
        3 -> object : CoordinateSystem.CoordinateSystem3D {
            override val minX: Int = minAndMaxs[0].first
            override val maxX: Int = minAndMaxs[0].second
            override val minY: Int = minAndMaxs[1].first
            override val maxY: Int = minAndMaxs[1].second
            override val minZ: Int = minAndMaxs[2].first
            override val maxZ: Int = minAndMaxs[2].second
            override val center: Location = center
        }
        else -> throw java.lang.IllegalStateException("There can only be 1-3 dimensions: ${minAndMaxs.size}")
    }
}

fun CoordinateSystem.CoordinateSystem1D.forEachX(stepWidth: Double = 1.0, invokable: (Double) -> Unit) {
    var i = minX.toDouble();
    while(i <= maxX) {
        invokable.invoke(i)
        i += stepWidth
    }
}

fun CoordinateSystem.CoordinateSystem2D.forEachY(stepWidth: Double = 1.0, invokable: (Double) -> Unit) {
    var i = minY.toDouble()
    while (i <= maxY) {
        invokable.invoke(i)
        i += stepWidth
    }
}

fun CoordinateSystem.CoordinateSystem3D.forEachZ(stepWidth: Double = 1.0, invokable: (Double) -> Unit) {
    var i = minZ.toDouble()
    while (i <= maxZ) {
        invokable.invoke(i)
        i += stepWidth
    }
}

fun CoordinateSystem.CoordinateSystem3D.forEachXY(stepWidth: Double = 1.0, invokable: (Double, Double) -> Unit) {
    forEachX(stepWidth) { x ->
        forEachY(stepWidth) { y ->
            invokable.invoke(x, y)
        }
    }
}
