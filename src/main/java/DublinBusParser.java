import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class DublinBusParser {
	private final static String BASE_URL = "http://www.dublinbus.ie/RTPI/Sources-of-Real-Time-Information/?searchtype=route&searchquery=";

	public static void main(String[] args) {
		
		ArrayList<DublinBusRoute> routeObjList = new ArrayList<DublinBusRoute>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("DublinBusRouteNumbers.txt"));
			String line = null;
	        while ((line = reader.readLine()) != null) {
	            String busRoute = line.trim();
	        	System.out.println("BUS ROUTE: " + busRoute);
	            routeObjList.add(scrapeBusSite(busRoute));
	        }
	        reader.close();
	    } catch (IOException e) {
	        System.out.println(e.getMessage());
    	}

		// Create the JSON object for each bus route
		JSONArray finalJSON = new JSONArray();
		for(DublinBusRoute route : routeObjList){
			finalJSON.put(createJsonForRoute(route));
		}
		
		System.out.print(finalJSON.toString());
		writeFile(finalJSON.toString());
	
	}

	private static JSONObject createJsonForRoute(DublinBusRoute route){
		JSONArray inboundStopsJSON = new JSONArray(route.getInboundStops());
		JSONArray outboundStopsJSON = new JSONArray(route.getOutboundStops());

		JSONObject routeJsonObj = new JSONObject();
		routeJsonObj.put("routeNumber", route.getRouteNumber());
		routeJsonObj.put("stopsInbound", inboundStopsJSON);
		routeJsonObj.put("stopsOutbound", outboundStopsJSON);
		
		return routeJsonObj;
	}
	
	private static DublinBusRoute scrapeBusSite(String busRoute) {
		System.out.println("Starting Bus Route " + busRoute);
		System.setProperty("webdriver.chrome.driver", "U:\\DublinBusParser\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		driver.get(BASE_URL + busRoute);
		
		// The web page for each route number has a View Direction button, with each direction being inbound and outbound
		driver.findElement(By.xpath("//*[contains(text(), 'View Direction')]")).click();
		
		DublinBusRoute route = new DublinBusRoute();
		route.setRouteNumber(busRoute);
		route.setInboundStops(readBusStopTable(driver));
		
		// There are is only one direction for routes: 46e, 51x, 68x, 77x, 118
		if(!busRoute.equals("46e") && !busRoute.equals("51x") && !busRoute.equals("68x") && !busRoute.equals("77x") && !busRoute.equals("118")){
			
			// You can change the direction by clicking the Change Direction button on the web page
			driver.findElement(By.xpath("//*[contains(text(), 'Change Direction')]")).click();			
			route.setOutboundStops(readBusStopTable(driver));
		}
		
		driver.quit();
		
		System.out.println("Finished Bus Route " + busRoute);

		return route;
	}
	
	private static ArrayList<Integer> readBusStopTable(WebDriver driver){
		List<WebElement> tr = driver.findElements(By.xpath("//table//tr"));

		ArrayList<Integer> stopList = new ArrayList<Integer>();
		
		// The first 5 rows in the table are table headers, so i = 5
		for (int i = 5; i < tr.size(); i++) {
			List<WebElement> cell = tr.get(i).findElements(By.tagName("td"));
			
			// The bus stop number is located at column 0 for each row of the table
			stopList.add(Integer.parseInt(cell.get(0).getText()));
		}
		
		return stopList;
	}
	
	private static void writeFile(String fileContent){
		BufferedWriter writer = null;
        try {
            File outputFile = new File("BusRouteStopsJSON.json");
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
}
