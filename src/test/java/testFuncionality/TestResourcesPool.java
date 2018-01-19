package testFuncionality;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Controller.ResourcesPool;
import Modelo.Package;
import Modelo.Station;
import Modelo.Train;

public class TestResourcesPool {
	ArrayList<Station> stations;
	ArrayList<Package> packages;
	ArrayList<Train> trains;

	
	ResourcesPool resourcesPool;
	Method calcularDireccionPaquete, distanciaEntreEstaciones, asignarPaquetesAEstaciones;

	@Before
	public void setUp() {
		resourcesPool = new ResourcesPool();
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
		packages = new ArrayList<Package>();
		initStations();
		initPackages();
		initPrivateFunctions();
		
		
	}

	@Test
	public void testIniciarCircuitoDesdeBBDD() {
		resourcesPool.iniciarCircuitoDesdeBBDD();
		assertNotEquals(null, resourcesPool.getTrains().get(0));
		assertNotEquals(null, resourcesPool.getStations().get(0));
	}
	
	@Test
	public void testAsignarPaquetesAEstaciones() {
		resourcesPool.setStations(stations);
		resourcesPool.setPackages(packages);
		try {
			asignarPaquetesAEstaciones.invoke(resourcesPool);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotEquals(0, resourcesPool.getStations().get(0).getSendPackageList().size());
	}
	
	@Test
	public void testCalcularDireccionPaquete1() {
		try {
			assertEquals(0, calcularDireccionPaquete.invoke(resourcesPool, packages.get(0)));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCalcularDireccionPaquete2() {
		try {
			assertEquals(1, calcularDireccionPaquete.invoke(resourcesPool, packages.get(1)));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDistanciaEntreEstacionesDir0() {
		try {
			assertEquals(2, distanciaEntreEstaciones.invoke(resourcesPool, stations.get(0), stations.get(2), 0));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDistanciaEntreEstacionesDir1() {
		try {
			assertEquals(4, distanciaEntreEstaciones.invoke(resourcesPool, stations.get(0), stations.get(2), 1));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Station createStation(int ID, String description) {
		Station newStation = new Station();
		newStation.setStationID(ID);
		newStation.setDescription(description);
		return newStation;
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
//		packages.add(new Package(stations.get(1), stations.get(3), "Paquete 3"));
//		packages.add(new Package(stations.get(5), stations.get(0), "Paquete 4"));
//		packages.add(new Package(stations.get(3), stations.get(0), "Paquete 5"));
//		packages.add(new Package(stations.get(4), stations.get(1), "Paquete 6"));
		
		packages.get(0).setPackageState(0);
		packages.get(1).setPackageState(2);
//		packages.get(2).setPackageState(2);
//		packages.get(3).setPackageState(0);
//		packages.get(4).setPackageState(0);
//		packages.get(5).setPackageState(2);
	}
	public void initPrivateFunctions() {
		try {
			calcularDireccionPaquete = resourcesPool.getClass().getDeclaredMethod("calcularDireccionPaquete",
					Package.class);
			distanciaEntreEstaciones = resourcesPool.getClass().getDeclaredMethod("distanciaEntreEstaciones",
					Station.class, Station.class, int.class);
			asignarPaquetesAEstaciones = resourcesPool.getClass().getDeclaredMethod("asignarPaquetesAEstaciones");

		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calcularDireccionPaquete.setAccessible(true);
		distanciaEntreEstaciones.setAccessible(true);
		asignarPaquetesAEstaciones.setAccessible(true);
	}
}