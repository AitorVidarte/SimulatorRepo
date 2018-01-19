package Controller;

/**
 * @file TrainThread.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief Train Thread
 */


import java.util.Iterator;
import Modelo.Rail;
import Modelo.Station;
import Modelo.Train;
import Modelo.Package;

public class TrainThread extends Thread {

	/** train */
	Train train;
	/** resourcePool */
	ResourcesPool resourcePool;

	/**
	 * TrainThread constructor.
	 * @param train
	 * recive train
	 * @param resource
	 * recive resource
	 */
	
	public TrainThread(Train train, ResourcesPool resource) {
		this.train = train;
		this.resourcePool = resource;

	}

	/**
	 * The thread runnable function. This thread is always doing the thread methods.
	 */
	
	public void run() {

		while (true) {

			if (moverse()) {
				recogerPaquete();
				entregarPaquete();
				pedirRail();
				salirEstacion();
				recorreRail();
				entrarEstacion();
				soltarRail();
				comprobarSiTieneQueParar();
			} else {
				comprobarEstaciones();
				descansar();
			}

		}
	}
	
	/**
	 * This method is used to sleep the thread.
	 */
	private void descansar() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

	}
	/**
	 * This method is used to check if a package has arrived at any station.
	 */
	private void comprobarEstaciones() {

		for (Station station : resourcePool.getCircuito().getEstaciones()) {
			for (Package package1 : station.getSendPackageList()) {
				if (package1.getTakeTrain().getTrainID() == train.getTrainID()) {
					train.setOnGoing(true);
				}
			}
		}

	}
	/**
	 * This method checks if the train has to stop.
	 */
	private void comprobarSiTieneQueParar() {

		if (train.paquetesEntregados() && tengoPaquetesPorRecoger()) {
			train.setOnGoing(false);
			resourcePool.acutalizarTren(train);
		}

	}
	
	/**
	 * This method checks if the train has to pick up a package.
	 * @return
	 * return stop.
	 */
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
   /**
    * This method is used for a train to pick up the packages of a station.
    */
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
	  /**
	    * This method is used for a train to deliver the packages of a station.
	    */
	private void entregarPaquete() {

		Iterator<Package> itTrainPackages = train.getPackageList().iterator();
		Package paquete = null;

		while (itTrainPackages.hasNext()) {
			paquete = itTrainPackages.next();
			
			try {
				System.out.println("El tren tiene: "+train.getPackageList().size()+" paquetes y la estacion :"+train.getStation().getSendPackageList().size());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (paquete.getDestination().getStationID() == train.getStation().getStationID()) {
				paquete.setPackageState(2);
				train.getStation().addDeliveredPackageList(paquete);
				resourcePool.actualizarPaquete(paquete);
				resourcePool.acutalizarTren(train);
			}
		}
	}
	/**
	 * This method is used for the train to ask for a rail.
	 */
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
	/**
	 * This method is used to leave a station and the train warns trains that you may be blocked.
	 */
	private void salirEstacion() {

		for (Station station : resourcePool.getCircuito().getEstaciones()) {

			if (station.getStationID() == train.getTrainID()) {
				train.getStation().despertarTren();
				station.quitarTren(train);
				resourcePool.acutalizarTren(train);
			}
		}
	}
	/**
	 * This method is used for the train to enter a station.
	 */
	private void entrarEstacion() {
		for (Station station : resourcePool.getCircuito().getEstaciones()) {
			if (station.getStationID() == train.getTrainID()) {
				station.quitarTren(train);
				Train train = this.getTrain();
				train.setStation(train.getRail().getNextStation());
				resourcePool.acutalizarTren(train);
			}
		}
	}
	/**
	 * This method is used for the train to travel the rail and ask for a parking to the next station.
	 */
	private void recorreRail() {

		for (int i = 0; i <= 100; i += 10) {
			pidiendoRail();

			if (i == 90) {

				pedirParking();
				pidiendoRail();
			}
		}
	}
	/**
	 * This method is used for the train to request a parking to the next station in case the station is full the train will be blocked.
	 */
	private void pedirParking() {
		int index = resourcePool.getCircuito().getEstaciones().indexOf(train.getRail().getNextStation());
		resourcePool.getCircuito().getEstaciones().get(index).obtenerPaking(resourcePool);
	}

	/**
	 * This method is used to simulate the thread movement.
	 */
	private void pidiendoRail() {
		try {

			Thread.sleep(6000);
		} catch (InterruptedException e) {
		}
	}
	/**
	 * This method is used to drop a rail and notify trains that you may be blocked.
	 */
	private void soltarRail() {
		Rail rail = train.getRail();
		resourcePool.getCircuito().soltarRail(rail);
		resourcePool.actualizarRail(rail);
		train.setRail(null);
		resourcePool.acutalizarTren(train);

	}

	/**
	 * This method is used to check if a train has to move.
	 * @return
	 * return true or false
	 */
	private boolean moverse() {
		boolean go = false;
		if (train.isOnGoing()) {
			go = true;
		}
		return go;
	}
	/**
	 * This method returns train of TrainThread.
	 * @return return train
	 */
	public Train getTrain() {
		return train;
	}

}
