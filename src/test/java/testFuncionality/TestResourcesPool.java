package testFuncionality;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import Controller.ResourcesPool;
import Modelo.Package;
import Modelo.Station;

public class TestResourcesPool {
	Station oiartzun, tolosa, azpeitia, zumaia, zarautz, donostia;
	Package paquete1, paquete2;
	ResourcesPool resourcesPool;
	Method calcularDireccionPaquete, distanciaEntreEstaciones;
	@Before
	public void setUp() {
		oiartzun = new Station();
		resourcesPool = new ResourcesPool();

		oiartzun = createStation(1, "Oiartzun");
		tolosa = createStation(2, "Tolosa");
		azpeitia = createStation(3, "Azpeitia");
		zumaia = createStation(4, "Zumaia");
		zarautz = createStation(5, "Zarautz");
		donostia = createStation(6, "Donostia");

		oiartzun.setNextStation(tolosa);
		oiartzun.setPreviousStation(donostia);
		tolosa.setNextStation(azpeitia);
		tolosa.setPreviousStation(oiartzun);
		azpeitia.setNextStation(zumaia);
		azpeitia.setPreviousStation(tolosa);
		zumaia.setNextStation(zarautz);
		zumaia.setPreviousStation(azpeitia);
		zarautz.setNextStation(donostia);
		zarautz.setPreviousStation(zumaia);
		donostia.setNextStation(oiartzun);
		donostia.setPreviousStation(zarautz);

		paquete1 = new Package(oiartzun, azpeitia, "Paquete 1");
		paquete2 = new Package(azpeitia, oiartzun, "Paquete 2");
		
		try {
			calcularDireccionPaquete = resourcesPool.getClass().getDeclaredMethod("calcularDireccionPaquete",
					Package.class);
			distanciaEntreEstaciones = resourcesPool.getClass().getDeclaredMethod("distanciaEntreEstaciones",
					Station.class, Station.class, int.class);

		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calcularDireccionPaquete.setAccessible(true);
		distanciaEntreEstaciones.setAccessible(true);
	}

	@Test
	public void testCalcularDireccionPaquete1() {
		try {
			assertEquals(0, calcularDireccionPaquete.invoke(resourcesPool, paquete1));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCalcularDireccionPaquete2() {
		try {
			assertEquals(1, calcularDireccionPaquete.invoke(resourcesPool, paquete2));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDistanciaEntreEstacionesDir0() {
			try {
				assertEquals(2,distanciaEntreEstaciones.invoke(resourcesPool, oiartzun, azpeitia,0));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Test
	public void testDistanciaEntreEstacionesDir1() {
			try {
				assertEquals(4,distanciaEntreEstaciones.invoke(resourcesPool, oiartzun, azpeitia,1));
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
}