import ListPartitioner.getAllPartitions
import java.util.*
import java.util.HashMap
import java.util.ArrayList
import java.util.concurrent.TimeUnit

lateinit var distances: Array2D<Double>

/**
 * Vehicle Routing Problem, brute-force solution
 * Ported from python code at https://github.com/ybashir/vrpfun
 */

fun main(args: Array<String>) {
    val vehicles = 3
    val locations = getLocations()
    val locationIds: List<Int> = locations.map { it.id }.toList()
    distances = getDistances()
    val startTimeMillis = System.currentTimeMillis()
    val shortestRouteSet = shortestRouteWithPartitions(locationIds, vehicles)
    println("Solution time: ${TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTimeMillis)} seconds")
    System.out.printf("Shortest route time: %.1f minutes\n", maxLengthForRouteSet(shortestRouteSet));
    println("Shortest route: " + shortestRouteSet)
}

fun distance(x: Int, y: Int): Double {
    return distances[x, y]
}

fun routeLength(route: List<Int>): Double {
    var sum = 0.0
    for (i in 1 until route.size) {
        sum += distance(route[i], route[i - 1])
    }
    sum += distance(route[0], route[route.size - 1])
    return sum
}

fun shortestRoute(routes: ArrayList<ArrayList<Int>>): ArrayList<Int> {
    return routes.minBy {
        routeLength(it)
    }!!
}

fun allRoutes(original: ArrayList<Int>): ArrayList<ArrayList<Int>> {
    if (original.size < 2) {
        return arrayListOf(original)
    } else {
        val firstElement = original.removeAt(0)
        return permutations(original).map {
            it.add(0, firstElement)
            return@map it
        } as ArrayList<ArrayList<Int>>
    }
}

fun maxLengthForRouteSet(routeSet: List<List<Int>>): Double {
    val routeLengths = ArrayList<Double>()
    return routeSet.mapTo(routeLengths) {
        routeLength(it)
    }.max()!!
}

fun shortestRouteWithPartitions(locationIds: List<Int>, partitions: Int): List<List<Int>> {
    return allShortRoutesWithPartitions(locationIds, partitions)
            .distinct()
            .minBy {
                maxLengthForRouteSet(it)
            }!!
}

fun allShortRoutesWithPartitions(seq: List<Int>, vehicles: Int): ArrayList<List<List<Int>>> {
    val shortRoutesList = ArrayList<List<List<Int>>>()
    return getAllPartitions(seq.drop(1)).filter {
        it.size == vehicles
    }.mapTo(shortRoutesList) {
        val shortestRouteWithCurrentPartitions = ArrayList<List<Int>>()
        it.mapTo(shortestRouteWithCurrentPartitions) {
            val r = arrayListOf<Int>(seq[0])
            r.addAll(it)
            shortestRoute(allRoutes(r))
        }
    }
}