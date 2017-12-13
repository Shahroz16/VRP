import ListPartitioner.getAllPartitions
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
    System.out.printf("Shortest route time: %.1f minutes\n", maxLengthForRoutes(shortestRouteSet));
    println("Shortest route: " + shortestRouteSet)
}

/**
 *Distance between first and last and consecutive elements of a list.
 **/
fun distance(x: Int, y: Int): Double {
    return distances[x, y]
}

/**
 * Distance between first and last and consecutive elements of a list
 */
fun routeLength(route: List<Int>): Double {
    var sum = 0.0
    for (i in 1 until route.size) sum += distance(route[i], route[i - 1])
    sum += distance(route[0], route[route.size - 1])
    return sum
}

/**
 * Returns minimum from a list based on route length
 */
fun shortestRoute(routes: ArrayList<ArrayList<Int>>): ArrayList<Int> {
    return routes.minBy {
        routeLength(it)
    }!!
}

/**
 * Return all permutations of a list, each starting with the first item
 */
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

/**
 * Return maximum from a given route list
 */
fun maxLengthForRoutes(routeList: List<List<Int>>): Double {
    val routeLengths = ArrayList<Double>()
    return routeList.mapTo(routeLengths) {
        routeLength(it)
    }.max()!!
}

/**
 * This function receives all k-subsets of a route and returns the subset
 * with minimum distance cost. Note the total time is always equal to
 * the max time taken by any single vehicle
 */
fun shortestRouteWithPartitions(locationIds: List<Int>, partitions: Int): List<List<Int>> {
    return allShortRoutesWithPartitions(locationIds, partitions)
            .distinct()
            .minBy {
                maxLengthForRoutes(it)
            }!!
}

/**
 * Our partitions represent number of vehicles. This function yields
 * an optimal path for each vehicle given the destinations assigned to it
 */
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