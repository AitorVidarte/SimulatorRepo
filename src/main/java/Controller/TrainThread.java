package Controller;

import java.util.Iterator;
import java.util.Set;

import DAO.PackageDAO;
import DAO.StationDAO;
import DAO.TrainDAO;
import Modelo.Circuito;
import Modelo.Rail;
import Modelo.Station;
import Modelo.Train;
import Modelo.Package;

public class TrainThread extends Thread {

	Train train;
	Circuito circuito;

	public TrainThread(Train train, Circuito circuito) {
		this.train = train;
		this.circuito = circuito;
	}

	public void run() {

		while (true) {

			if (moverse()) {
				recogerPaquete();
				entregarPaquete();

				if (moverse()) {
					pedirRail();
					salirEstacion();
					recorreRail();
					entrarEstacion();
				}

				// recogerPaquete();
				// entregarPaquete();
			}

		}
	}

	private void pedirRail() {

		for (Rail rail : circuito.getRailes()) {
			if (train.getDirection() == 0) {
				if ((train.getStation().getDescription().equals(rail.getPreviousStation().getDescription()))
						&& (train.getStation().getNextStation().getDescription()
								.equals(rail.getNextStation().getDescription()))) {
					circuito.cogerRail(rail);
					train.setRail(rail);
					// System.out.println("Rail: " + train.getRail().getRailID());
				}
			} else if (train.getDirection() == 1) {
				if ((train.getStation().getDescription().equals(rail.getNextStation().getDescription()))
						&& (train.getStation().getPreviousStation().getDescription()
								.equals(rail.getNextStation().getDescription()))) {
					circuito.cogerRail(rail);
					train.setRail(rail);
				}
			}
		}
	}

	public void ponerEnMarcha() {
		this.getTrain().setOnGoing(true);
		System.out.println("hilo!" + train.isOnGoing());
	}

	private void recogerPaquete() {
		PackageDAO packageDao = new PackageDAO();
		
		try {
			System.out.println("Recogiendo paquetes...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Package paquete : train.getStation().getSendPackageList()) {
			if (paquete.getTakeTrain().getTrainID() == train.getTrainID()) {
				System.out.println("entraPAQUETES!");
				paquete.setPackageState(1);
				// packageDao.edit(paquete, paquete.getPackageID());
				// train.getStation().getSendPackageList().remove(paquete);
				train.addPackageList(paquete);
				System.out.println("Paquete recogido!");
			}
		}
		System.out.println("El tren ha recogido " + train.getPackageList().size() + " paquetes!");
	}

	private void entregarPaquete() {

		PackageDAO packageDao = new PackageDAO();
		Set<Package> paquetes = train.getPackageList();
		Package paquete;
		Iterator<Package> it = paquetes.iterator();
		int cont = 0;

		while (it.hasNext()) {
			paquete = it.next();
			if (paquete.getDestination().getStationID() == train.getStation().getStationID()) {
				paquete.setPackageState(2);
				System.out.println(paquete.getPackageID());
				packageDao.edit(paquete, (paquete.getPackageID() - 1));
				train.getStation().getDeliveredPackageList().add(paquete);
				it.remove();
				System.out.println("Paquete entregado!");
				cont++;
			}

			if (train.getPackageList().size() == 0 && !comprobarSiTengoQueSeguir()) {
				System.out.println("PARANDO EL TREN!");
				this.getTrain().setOnGoing(false);
			}
		}
	}

	private boolean comprobarSiTengoQueSeguir() {
		try {
			System.out.println("Comprobando!!");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean seguir = true;
		for (Station station : circuito.getEstaciones()) {
			if (!station.equals(train.getStation())) {
				System.out.println(station.getStationID());
				for (Package paquete : station.getSendPackageList()) {
					if (paquete.getTakeTrain().getTrainID() == train.getTrainID()) {
						seguir = true;
					}
				}
			}
		}
		try {
			System.out.println("Comprobando!!");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return seguir;
	}

	private void salirEstacion() {

		StationDAO stationDao = new StationDAO();
		Station station = train.getStation();
		System.out.println("Rail: " + train.getRail().getRailID());
		station.quitarTren(train);
		// stationDao.edit(station);

		// station.avisarTrenWaitingZone(train);
	}

	private void entrarEstacion() {

		Rail rail = train.getRail();
		Station station = null;
		TrainDAO trainDao = new TrainDAO();

		StationDAO stationDao = new StationDAO();

		train.setStation(rail.getNextStation());
		soltarRail(rail);
		station = train.getStation();
		station.aparcarTren(train);
		System.out.println(train.getTrainID() - 1);
		trainDao.edit(train, train.getTrainID() - 1);
		// stationDao.edit(train.getStation());
	}

	private void recorreRail() {
		// TODO Auto-generated method stub
		// System.out.println("Rail: "+train.getRail().getRailID());

		System.out.println("Recorriendo: ");

		for (int i = 0; i <= 100; i += 10) {
			if (i == 90) {
				try {
					System.out.println("Pidiendo parking a la estacion!");
					System.out.println(train.getRail().getNextStation().getDescription());
					System.out.println(train.getRail().getNextStation().obtenerPaking());
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
				System.out.print(i + "%  ");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("Rail: " + train.getRail().getRailID());

	}

	private void soltarRail(Rail rail) {

		circuito.soltarRail(rail);

		// circuito.soltarRail(train.getRail());
	}

	private boolean moverse() {
		// TODO Auto-generated method stub
		boolean go = false;
		if (train.isOnGoing()) {
			go = true;
			// System.out.println("Tren" + train.getTrainID() + " Go!");
			// train.setOnGoing(false);
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
