import java.util.ArrayList;

public class DublinBusRoute {
	private String routeNumber;
	private ArrayList<Integer> outboundStops;
	private ArrayList<Integer> inboundStops;
	
	public String getRouteNumber() {
		return routeNumber;
	}
	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}
	public ArrayList<Integer> getOutboundStops() {
		return outboundStops;
	}
	public void setOutboundStops(ArrayList<Integer> outboundStops) {
		this.outboundStops = outboundStops;
	}
	public ArrayList<Integer> getInboundStops() {
		return inboundStops;
	}
	public void setInboundStops(ArrayList<Integer> inboundStops) {
		this.inboundStops = inboundStops;
	}
}
