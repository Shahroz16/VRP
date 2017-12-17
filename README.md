# Kotlin version of [python brute-force solution](https://github.com/ybashir/vrpfun) for the Vehicle Routing Problem (VRP)

VRP is a generalized version of the Traveling Salesperson Problem (TSP): Given a depot, a set of delivery locations and the number of vehicles starting from the depot, we need to minimize the time (or travel distance) required to visit all locations.

This repo is translation of a pythonic implementation of the brute-force solution to the VRP. VRP is an NP-Hard problem so the brute force obviously becomes very prohibitive very quickly as the number of destinations is increased or the number of vehicles is reduced.

The code expects two input files, one with locations and their geo-coordinates and the other with distances between locations. There are some simplistic assumptions:

1. Each delivery location needs to be visited just once
2. Vehicles come back to the depot
3. The first location in the list of locations is the depot
4. Distance from X to Y is the same as the distance from Y to X

Here is a sample plot of the starting input locations resulting from search query ```McDonalds near Lahore``` with the truck icon showing the depot:

![alt text](https://i.imgur.com/82QgV4X.jpg)

Here is the output created after the algorithm has been run with 3 vehicles and 11 locations:
```
Solution time: 3 seconds
Shortest route time: 66.2 minutes
Shortest route: [[0, 9, 11, 1], [0, 6, 10, 2, 5], [0, 7, 4, 8]]
Peak memory used before GC - 265 MB
```

Output after the algorithm has been run with 3 vehicles and 12 locations:
```
Solution time: 19 seconds
Shortest route time: 72.3 minutes
Shortest route: [[0, 7, 5, 11, 1], [0, 6, 10, 2, 12], [0, 4, 8, 9]]
Peak memory used before GC - 391 MB
```
