package xyz.mommde.mathcraft.models

import java.util.UUID

data class CoordinateSystemStorage(
    val id: UUID = UUID.randomUUID(),
    val expressions: MutableList<String>,
    val raw: CoordinateSystem
)
