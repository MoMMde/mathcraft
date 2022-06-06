package xyz.mommde.mathcraft.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.runs
import net.axay.kspigot.commands.suggestList
import net.axay.kspigot.extensions.worlds
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.*
import xyz.mommde.mathcraft.math.CoordinateSystemRenderer
import xyz.mommde.mathcraft.models.*
import xyz.mommde.mathcraft.util.FlatWorldGenerator
import java.util.*
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


fun command(renderer: CoordinateSystemRenderer) = net.axay.kspigot.commands.command("math", true) {

    literal("coordinate_system") {
        literal("create") {
            argument("dimensions", StringArgumentType.word()) {
                argument("min_x", IntegerArgumentType.integer()) {
                    argument("max_x", IntegerArgumentType.integer()) {
                        argument("min_y", IntegerArgumentType.integer()) {
                            argument("max_y", IntegerArgumentType.integer()) {
                                argument("min_z", IntegerArgumentType.integer()) {
                                    argument("max_z", IntegerArgumentType.integer()) {
                                        suggestList { CoordinateSystemDimension.values().map { it.name } }
                                        runs {
                                            val dimensions =
                                                CoordinateSystemDimension.valueOf(getArgument<String>("dimensions").uppercase())
                                            val minXArg = getArgument<Int>("min_x")
                                            val maxXArg = getArgument<Int>("max_x")
                                            val minYArg = getArgument<Int>("min_y")
                                            val maxYArg = getArgument<Int>("max_y")
                                            val minZArg = getArgument<Int>("min_z")
                                            val maxZArg = getArgument<Int>("max_z")
                                            val system = when (dimensions) {
                                                CoordinateSystemDimension.FIRST_DIMENSION -> buildCoordinateSystem(
                                                    player.location,
                                                    minXArg to maxXArg
                                                )
                                                CoordinateSystemDimension.SECOND_DIMENSION -> buildCoordinateSystem(
                                                    player.location,
                                                    minXArg to maxXArg,
                                                    minYArg to maxYArg
                                                )
                                                CoordinateSystemDimension.THIRD_DIMENSION -> buildCoordinateSystem(
                                                    player.location,
                                                    minXArg to maxXArg,
                                                    minYArg to maxYArg,
                                                    minZArg to maxZArg
                                                )
                                            }
                                            renderer.render(system)
                                            val id = UUID.randomUUID()
                                            coordinateSystems[id] =
                                                CoordinateSystemStorage(id = id, mutableListOf(), system)
                                            if (system is CoordinateSystem.CoordinateSystem2D) {
                                                system.forEachX(.1) {
                                                     system.place(it, sin(it/10) * maxXArg)
                                                }
                                            }
                                            if (system is CoordinateSystem.CoordinateSystem3D) {
                                                system.forEachXY(.1) { x, y ->
                                                    system.place(x, y, sin(x)*8)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            literal("execute") {
                argument("id", StringArgumentType.string()) {
                    argument("expression", StringArgumentType.string()) {
                        suggestList { coordinateSystems.map { it.key } }
                        runs {
                            val system = coordinateSystems.values.first { it.id.toString() == getArgument("id") }
                            system.expressions.add(getArgument("expression"))
                        }
                    }
                }
            }
        }


        literal("world") {
            literal("create") {
                runs {
                    val name = UUID.randomUUID().toString()
                    val world = Bukkit.createWorld(
                        WorldCreator(name)
                            .type(WorldType.NORMAL)
                            .environment(World.Environment.NORMAL)
                            .generator(FlatWorldGenerator(Material.BEDROCK))
                    )
                    player.sendMessage(literalText("Created world: $name") {
                        newLine()
                        text("Click to teleport") {
                            bold = true
                            clickEvent = ClickEvent.runCommand("/math world teleport ${world!!.uid}")
                        }
                    })
                }
            }
            literal("register") {
                argument("folder", StringArgumentType.string()) {
                    suggestList {
                        Bukkit.getWorldContainer().listFiles { dir, name ->
                            dir.isDirectory
                        }.map { it.name }
                    }
                    runs {
                        val folder = getArgument<String>("folder")
                        val world = Bukkit.createWorld(
                            WorldCreator.name(folder).generator(FlatWorldGenerator(Material.BEDROCK))
                        )
                        player.sendMessage(literalText("World Registered!") {
                            newLine()
                            text("Click to teleport") {
                                bold = true
                                clickEvent = ClickEvent.runCommand("/math world teleport ${world!!.uid}")
                            }
                        })
                    }
                }
            }
            literal("teleport") {
                argument("world", StringArgumentType.string()) {
                    suggestList { worlds.map { it.uid } }
                    runs {
                        val world = getArgument<String>("world")
                        player.teleport(worlds.first { it.uid.toString() == world }.spawnLocation)
                    }
                }
            }
        }
    }
}
