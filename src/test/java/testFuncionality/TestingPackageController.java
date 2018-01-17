package testFuncionality;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Controller.PackageController;
import Controller.ResourcesPool;
import Modelo.Station;
import Modelo.Package;

public class TestingPackageController {

	ResourcesPool resourcesPool;
	Package paquete1, paquete2;
	@Before
	public void setUp(){
		resourcesPool = new ResourcesPool();
		resourcesPool.iniciarCircuito();
		paquete1 = new Package(resourcesPool.getStations().get(0), resourcesPool.getStations().get(2), "Paquete 1");
		paquete2 = new Package(resourcesPool.getStations().get(2), resourcesPool.getStations().get(0), "Paquete 2");
	}
	
	@Test
	public void testCalcularDireccionPaquete1() {
		assertEquals(0, resourcesPool.calcularDireccionPaquete(paquete1));
	}
	
	@Test
	public void testCalcularDireccionPaquete2() {
		Package paquete = new Package(destination, origin, "Paquete 2");
		assertEquals(1, packageController.calcularDireccionPaquete(paquete));
	}
	
	@Test
	public void testDistanciaEntreEstaciones1() {
		assertEquals(1, packageController.distanciaEntreEstaciones(origin, destination, 0));
	}
	
	@Test
	public void testDistanciaEntreEstaciones2() {
		assertEquals(5, packageController.distanciaEntreEstaciones(origin, destination, 1));
	}
}