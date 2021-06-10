package us.crystal.ad340kotlin.model

data class Camera(
    val Id: String,
    val Description: String,
    val ImageUrl: String,
    val Type: String,
    val points: DoubleArray,
)