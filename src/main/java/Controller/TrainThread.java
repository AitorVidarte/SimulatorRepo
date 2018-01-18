package Controller;

import java.util.Iterator;
import Modelo.Rail;
import Modelo.Station;
import Modelo.Train;
import Modelo.Package;

public class TrainThread extends Thread {

	Train train;
	ResourcesPool resourcePool;

	public TrainThread(Train train, ResourcesPool resource) {
		this.train = train;
		this.resourcePool = resource;
		
	}

	public void run() {

		while (true) {

			if (moverse()) {
				recogerPaquete();
				entregarPaquete();
				comprobarSiTieneQueParar();
				if (moverse()) {
					pedirRail();
					salirEstacion();
					recorreRail();
					entrarEstacion();
					soltarRail();
				}
			} else {
				comprobarEstaciones();
				descansar();
			}

		}
	}

	private void descansar() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

	}

	private void comprobarEstaciones() {

		for (Station station : resourcePool.getCircuito().getEstaciones()) {
			for (Package package1 : station.getSendPackageList()) {
				if (package1.getTakeTrain().getTrainID() == train.getTrainID()) {
					train.setOnGoing(true);
				}
			}
		}

	}

	private void comprobarSiTieneQueParar() {

		if (train.paquetesEntregados() && tengoPaquetesPorRecoger()) {
			train.setOnGoing(false);
			resourcePool.acutalizarTren(train);
		}

	}

	private boolean tengoPaquetesPorRecoger() {
		boolean parar = true;
		for (Station station : resourcePool.getCircuito().getEstaciones()) {
			for (Package stationPackage : station.getSendPackageList()) {
				if (stationPackage.getTakeTrain().getTrainID() == train.getTrainID()) {
					parar = false;
				}
			}
		}
		return parar;
	}

	private void recogerPaquete() {
		Station stations = resourcePool.getCircuito().reservarEstacion(train.getStation().getStationID() - 1, true);
		Iterator<Package> itStationPackages = stations.getSendPackageList().iterator();
		Package paquete = null;

		while (itStationPackages.hasNext()) {
			paquete = itStationPackages.next();

			if (paquete.getTakeTrain().getTrainID() == train.getTrainID()) {
				paquete.setPackageState(1);
				resourcePool.actualizarPaquete(paquete);
				train.addPackageList(paquete);
				resourcePool.acutalizarTren(train);
				this.getTrain().addPackageList(paquete);
				itStationPackages.remove();
			}
		}
		resourcePool.getCircuito().despertarTrenes(train.getStation().getStationID() - 1);
	}

	private void entregarPaquete() {

		Iterator<Package> itTrainPackages = train.getPackageList().iterator();
		Package paquete = null;

		while (itTrainPackages.hasNext()) {
			paquete = itTrainPackages.next();

			if (paquete.getDestination().getStationID() == train.getStation().getStationID()) {
				paquete.setPackageState(2);
				train.getStation().addDeliveredPackageList(paquete);
				resourcePool.actualizarPaquete(paquete);
				resourcePool.acutalizarTren(train);
			}
		}
	}

	private void pedirRail() {

		for (Rail rail : resourcePool.getCircuito().getRailes()) {
			if (train.getDirection() == 0) {
				if ((train.getStation().getDescription().equals(rail.getPreviousStation().getDescription()))
						&& (train.getStation().getNextStation().getDescription()
								.equals(rail.getNextStation().getDescription()))) {
					resourcePool.getCircuito().cogerRail(rail);
					resourcePool.actualizarRail(rail);
					train.setRail(rail);
				}
			} else if (train.getDirection() == 1) {
				if ((train.getStation().getDescription().equals(rail.getPreviousStation().getDescription()))
						&& (train.getStation().getPreviousStation().getDescription()
								.equals(rail.getNextStation().getDescription()))) {
					resourcePool.getCircuito().cogerRail(rail);
					train.setRail(rail);
				}
			}
		}
	}

	private void salirEstacion() {

		for (Station station : resourcePool.getCircuito().getEstaciones()) {

			if (station.getStationID() == train.getTrainID()) {
				station.quitarTren(train);
				resourcePool.acutalizarTren(train);
				}
		}
	}

	private void entrarEstacion() {
		for (Station station : resourcePool.getCircuito().getEstaciones()) {
			if (station.getStationID() == train.getTrainID()) {
				station.quitarTren(train);
				Train train = this.getTrain();
				train.setStation(train.getRail().getNextStation());
				resourcePool.acutalizarTren(train);
				resourcePool.despertarTrenesEstacion(train.getStation());
			}
		}
	}

	private void recorreRail() {
		
		for (int i = 0; i <= 100; i += 10) {
			pidiendoRail();
			pedirParking();
			if (i == 90) {
				pidiendoRail();
			}
		}
	}

	private void pedirParking() {
		resourcePool.pedirParkingAEstacion(train.getRail().getNextStation());
	}

	private void pidiendoRail() {
		try {
			
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}

	private void soltarRail() {
		Rail rail = train.getRail();
		resourcePool.getCircuito().soltarRail(rail);
		resourcePool.actualizarRail(rail);
		train.setRail(null);
		resourcePool.acutalizarTren(train);

	}

	private boolean moverse() {
		boolean go = false;
		if (train.isOnGoing()) {
			go = true;
		}
		return go;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}
}
