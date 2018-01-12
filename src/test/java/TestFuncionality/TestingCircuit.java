package TestFuncionality;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Modelo.Circuito;
import Modelo.Rail;
import Modelo.Station;

public class TestingCircuito {
	Circuito circuito;
	ArrayList<Rail> railes;
	ArrayList<Station> stations;
	@Before
	public void setUp(){
		circuito = new Circuito();
		railes = new ArrayList<Rail>();
		stations = new ArrayList<Station>();
		
		stations.add(new Station(1,43.2541730,-1.8474567,"Oiartzun",2,6,1,1,null,null));
		stations.add(new Station(2,43.1077539, -2.0800512,"Tolosa",3,1,1,1,null,null));
		stations.add(new Station(3,43.1624073, -2.2517858,"Apeitia",4,2,1,1,null,null));
		stations.add(new Station(4,43.2876451, -2.2461133,"Zumaia",5,3,1,1,null,null));
		stations.add(new Station(5,43.2809633, -2.1672242,"Zarautz",6,4,1,1,null,null));
		stations.add(new Station(6,43.2918111, -1.9885133,"Donostia",1,5,1,1,null,null));
		
		railes.add(new Rail(1, stations.get(2), stations.get(1), false));
		railes.add(new Rail(2, stations.get(3), stations.get(2), false));
		railes.add(new Rail(3, stations.get(4), stations.get(3), false));
		railes.add(new Rail(4, stations.get(5), stations.get(4), false));
		railes.add(new Rail(5, stations.get(6), stations.get(5), false));
		railes.add(new Rail(6, stations.get(1), stations.get(6), false));
		
		railes.add(new Rail(7, stations.get(1), stations.get(2), false));
		railes.add(new Rail(8, stations.get(6), stations.get(1), false));
		railes.add(new Rail(9, stations.get(5), stations.get(6), false));
		railes.add(new Rail(10, stations.get(4), stations.get(5), false));
		railes.add(new Rail(11, stations.get(3), stations.get(4), false));
		railes.add(new Rail(12, stations.get(2), stations.get(3), false));
		
		circuito.setEstaciones(stations);
		circuito.setRailes(railes);
	}
	
	@Test
	public void testCogerRail1() {
		assertEquals(new Rail(1, stations.get(2), stations.get(1), true),circuito.cogerRail(railes.get(0)));
	}
	@Test
	public void testCogerRail2() {
		railes.get(0).setOccupied(true);
		assertEquals(new Rail(1, stations.get(2), stations.get(1), true),circuito.cogerRail(railes.get(0)));
	}
	@Test
	public void testSoltarRail1() {
		railes.get(0).setOccupied(true);
		circuito.soltarRail(railes.get(0));
		assertEquals(false,railes.get(0).isOccupied());
	}
}