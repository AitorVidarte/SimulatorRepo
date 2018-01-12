package testFuncionality;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Controller.PackageController;
import Controller.ResourcesPool;
import Modelo.Station;
import Modelo.Package;

public class TestingPackageController {

	Package paquete;
	Station origin, destination;
	PackageController packageController;
	ResourcesPool resourcesPool;
	
	@Before
	public void setUp(){
		origin = new Station(1, 43.2541730, -1.8474567, "Oiartzun", 2, 6, 1, 1, null, null);
		destination = new Station(2, 43.1077539, -2.0800512, "Tolosa", 3, 1, 1, 1, null, null);
		paquete = new Package(origin, destination, "Paquete 1");
		resourcesPool = new ResourcesPool();
		packageController = new PackageController(resourcesPool);
	}
	
	@Test
	public void testCalcularDireccionPaquete1() {
		assertEquals(0, packageController.calcularDireccionPaquete(paquete));
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