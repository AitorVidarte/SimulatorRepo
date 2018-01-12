package TestFuncionality;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import Modelo.Station;
import Modelo.Train;

public class TestingStation {
	Station station, nextStation, previousStation;
	Train train1, train2, train3, train4, train5, train6;

	@Before
	public void setUp() {
		previousStation = new Station(1, 43.2541730, -1.8474567, "Oiartzun", 2, 6, 1, 1, null, null);
		station = new Station(2, 43.1077539, -2.0800512, "Tolosa", 3, 1, 1, 1, null, null);
		nextStation = new Station(3, 43.1624073, -2.2517858, "Apeitia", 4, 2, 1, 1, null, null);
		station.setNextStation(nextStation);
		station.setPreviousStation(previousStation);

		train1 = new Train(1, previousStation, 0);
		train2 = new Train(2, previousStation, 0);
		train3 = new Train(3, previousStation, 0);
		train4 = new Train(4, nextStation, 1);
		train5 = new Train(5, nextStation, 1);
		train6 = new Train(6, nextStation, 1);

	}

	@Test
	public void testGetParkWhenEmpty() {
		assertEquals(0, station.obtenerPaking());
	}

	@Test(timeout = 1000)
	public void testGetParkWhenFull() {
		station.aparcarTren(train1);
		station.aparcarTren(train2);
		station.aparcarTren(train3);
		station.aparcarTren(train4);
		Thread thread = new Thread(() -> {
			try {
				station.obtenerPaking();
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				System.out.println("Error testGetParkWhenFull!");
			}
		});
		thread.start();
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			System.out.println("Error testGetParkWhenFull!");
		}
		assertEquals(1, station.obtenerPaking());
	}
}
