# Dublin-Bus-Routes-and-Stops
A Java Selenium program to extract the bus stops associated with each Dublin Bus route as a JSON file.

The resulting JSON file is located [here.](https://raw.githubusercontent.com/JTODR/Dublin-Bus-Routes-and-Stops/master/BusRouteStopsJSON.json)

#### Example output:

    [
      {
        ...
      },
      { 
        "routeNumber":"7",
        "stopsOutbound":[
          1174,
          4962,
          4725,
          ...
          3220,
          5046,
          7639
        ],
        "stopsInbound":[
          7639,
          7640,
          5047,
          ...
          400,
          281,
          4
        ]
      },
    ]
