package testFuncionality;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Controller.ResourcesPool;
import Modelo.Circuito;
import Modelo.Package;
import Modelo.Rail;
import Modelo.Station;
import Modelo.Train;

public class TestTrainThread {
	ArrayList<Station> stations;
	ArrayList<Package> packages;
	ArrayList<Train> trains;
	ArrayList<Rail> rails;
	Circuito circuito;
	ResourcesPool resourcesPool;
	Method recorreRail;
	@Before
	public void setUp() {
		resourcesPool = new ResourcesPool();
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
		rails = new ArrayList<Rail>();
		packages = new ArrayList<Package>();
		circuito = new Circuito();
		initStations();
		initPackages();
		initTrains();
		initRails();
		
	}

	@Test
	public void testRecorrerRail() {
		trains.get(0).setStation(stations.get(0));
		trains.get(1).setStation(stations.get(0));
		trains.get(2).setStation(stations.get(0));
		trains.get(3).setStation(stations.get(0));
		trains.get(4).setStation(stations.get(1));
		
		
		circuito.setEstaciones(stations);
		circuito.setRailes(rails);
		
		resourcesPool.setTrains(trains);
		resourcesPool.setStations(stations);

		resourcesPool.setCircuito(circuito);
		resourcesPool.createThreads();
		resourcesPool.launchThreads();
		
		try {
			recorreRail = resourcesPool.getTrainThreads().get(4).getClass().getDeclaredMethod("recorreRail");
			recorreRail.setAccessible(true);
			recorreRail.invoke(resourcesPool.getTrainThreads().get(4));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(resourcesPool.getTrainThreads().get(4).isInterrupted());
	}

	public void initStations() {
		stations.add(createStation(1, "Oiartzun"));
		stations.add(createStation(2, "Tolosa"));
		stations.add(createStation(3, "Azpeitia"));
		stations.add(createStation(4, "Zumaia"));
		stations.add(createStation(5, "Zarautz"));
		stations.add(createStation(6, "Donostia"));

		stations.get(0).setNextStation(stations.get(1));
		stations.get(0).setPreviousStation(stations.get(5));
		stations.get(1).setNextStation(stations.get(2));
		stations.get(1).setPreviousStation(stations.get(0));
		stations.get(2).setNextStation(stations.get(3));
		stations.get(2).setPreviousStation(stations.get(1));
		stations.get(3).setNextStation(stations.get(4));
		stations.get(3).setPreviousStation(stations.get(2));
		stations.get(4).setNextStation(stations.get(5));
		stations.get(4).setPreviousStation(stations.get(3));
		stations.get(5).setNextStation(stations.get(0));
		stations.get(5).setPreviousStation(stations.get(4));
	}

	public void initPackages() {
		packages.add(new Package(stations.get(0), stations.get(2), "Paquete 1"));
		packages.add(new Package(stations.get(2), stations.get(0), "Paquete 2"));

		packages.get(0).setPackageState(0);
		packages.get(1).setPackageState(2);
	}

	public void initTrains() {
		trains.add(createTrain(0, false, 1, 1));
		trains.add(createTrain(0, false, 2, 2));
		trains.add(createTrain(0, false, 3, 3));
		trains.add(createTrain(1, false, 4, 4));
		trains.add(createTrain(1, false, 5, 5));
		trains.add(createTrain(1, false, 6, 6));
	}

	public void initRails() {
		rails.add(createRail(1, stations.get(0), stations.get(1)));
		rails.add(createRail(1, stations.get(1), stations.get(2)));
		rails.add(createRail(1, stations.get(2), stations.get(3)));
		rails.add(createRail(1, stations.get(3), stations.get(4)));
		rails.add(createRail(1, stations.get(4), stations.get(5)));
		rails.add(createRail(1, stations.get(5), stations.get(0)));
		rails.add(createRail(1, stations.get(1), stations.get(0)));
		rails.add(createRail(1, stations.get(0), stations.get(5)));
		rails.add(createRail(1, stations.get(5), stations.get(4)));
		rails.add(createRail(1, stations.get(4), stations.get(3)));
		rails.add(createRail(1, stations.get(3), stations.get(2)));
		rails.add(createRail(1, stations.get(2), stations.get(1)));
	}

	public Train createTrain(int dir, boolean onGoin, int id, int stationID) {
		Train train = new Train();
		train.setOnGoing(onGoin);
		train.setDirection(dir);
		train.setTrainID(id);
		train.setStation(stations.get(stationID-1));
		return train;
	}

	public Station createStation(int ID, String description) {
		Station newStation = new Station();
		newStation.setStationID(ID);
		newStation.setDescription(description);
		return newStation;
	}

	public Rail createRail(int id, Station previousStation, Station nextStation) {
		Rail rail = new Rail();
		rail.setRailID(id);
		rail.setOccupied(false);
		rail.setNextStation(nextStation);
		rail.setPreviousStation(previousStation);
		return rail;
	}
}
