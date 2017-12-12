import com.google.common.base.Splitter
import java.nio.file.Files
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

const val LIMIT_LOCATIONS = 11
const val MAX_LOCATIONS = 25

data class Location(var id: Int, var address: String, var lat: Double, var lon: Double)

fun getLocations(): List<Location> {
    val file = File("./sample_locations.csv")
    val lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
    val locations: ArrayList<Location> = ArrayList()
    lines
            .drop(1)
            .map { line ->
                Splitter.on(Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")).split(line).dropWhile {
                    it.isEmpty()
                }
            }
            .mapTo(locations) { Location(it[0].toInt(), it[1], it[2].toDouble(), it[3].toDouble()) }
    return locations.subList(0, LIMIT_LOCATIONS).toList()
}

fun getDistances(): Array2D<Double> {
    val file = File("./sample_distances.csv")
    val lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
    val distances = Array2D<Double>(MAX_LOCATIONS, MAX_LOCATIONS) { x: Int, y: Int -> 0.0 }
    lines
            .drop(1)
            .map { line -> line.split(",".toRegex()).dropLastWhile { it.isEmpty() } }
            .map {
                distances[it[0].toInt(), it[1].toInt()] = it[3].toDouble()
                return@map it
            }
            .map { distances[it[1].toInt(), it[0].toInt()] = it[3].toDouble() }

    return distances
}


