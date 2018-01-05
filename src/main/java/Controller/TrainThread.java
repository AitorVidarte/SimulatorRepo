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
		if (train.getDirection() == 0){
		System.out.println(train.getTrainID() + " Dir " + train.getDirection());
		}   
		while (true) {

			if (moverse()) {

				pedirRail();
				salirEstacion();
				recorreRail();
				entrarEstacion();
				//entregarPaquete();
				//recogerPaquete();
			}

		}
	}
	
	private void pedirRail() {

		
		for (Rail rail : circuito.getRailes()) {
			
			if (train.getDirection() == 0) {
		
				if ((train.getStation().getDescription().equals(rail.getPreviousStation().getDescription()))&&(train.getStation().getNextStation().getDescription().equals(rail.getNextStation().getDescription()))) {
					circuito.cogerRail(rail);
					train.setRail(rail);
					System.out.println("Rail: "+train.getRail().getRailID());
					//System.out.println("El tren:" + train.getTrainID() + " esta utilizando el rail: " + rail.getRailID());

				}
				
			} else if (train.getDirection() == 1) {
				
				if ((train.getStation().getDescription().equals(rail.getNextStation().getDescription()))&&(train.getStation().getPreviousStation().getDescription().equals(rail.getNextStation().getDescription()))) {
					circuito.cogerRail(rail);
					train.setRail(rail);
					//System.out.println(rail.getRailID());

				}
			}

		}

	}

	private void recogerPaquete() {
		PackageDAO packageDao = new PackageDAO();
		for (Package paquete : train.getPackageList()) {
			if (paquete.getOrigin() == train.getStation()) {
				paquete.setPackageState(1);
				train.getStation().getSendPackageList().remove(paquete);
				packageDao.edit(paquete, paquete.getPackageID());
				System.out.println("Paquete recogido!");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void entregarPaquete() {
		PackageDAO packageDao = new PackageDAO();
		Set<Package> paquetes = train.getPackageList();
		Package paquete;
		Iterator<Package> it = paquetes.iterator();
		int cont = 0;

		while (it.hasNext()) {
			paquete = it.next();
			if (paquete.getDestination() == train.getStation()) {
				paquete.setPackageState(2);
				System.out.println(paquete.getPackageID());
				packageDao.edit(paquete, (paquete.getPackageID() - 1));
				train.getStation().getDeliveredPackageList().add(paquete);
				it.remove();
				System.out.println("Paquete entregado!");
				cont++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (cont == 0) {
			System.out.println("No hay paquetes a entregar.");
		}
	}

	private void salirEstacion() {
		
		StationDAO stationDao = new StationDAO();
		Station station = train.getStation();
		System.out.println("Rail: "+train.getRail().getRailID());
		station.quitarTren(train);
		stationDao.edit(station);
		
		// station.avisarTrenWaitingZone(train);
	}

	private void entrarEstacion() {

		Rail rail = train.getRail();
		Station station = null;
		TrainDAO trainDao = new TrainDAO();
		StationDAO stationDao = new StationDAO();

		try {
			Thread.sleep(1000);
			train.setStation(rail.getNextStation());
			soltarRail(rail);
			station = train.getStation();
			station.aparcarTren(train);
			System.out.println("\nEl tren:" + train.getTrainID() + " ha entrado en la estacion: "+ train.getStation().getDescription());
			trainDao.edit(train, train.getTrainID()-1);
			stationDao.edit(train.getStation());
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void recorreRail() {
		// TODO Auto-generated method stub
		System.out.println("Rail: "+train.getRail().getRailID());
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
		System.out.println("Rail: "+train.getRail().getRailID());
		
	}



	private void soltarRail(Rail rail) {
		
		circuito.soltarRail(rail);
		
//		circuito.soltarRail(train.getRail());
	}

	private boolean moverse() {
		// TODO Auto-generated method stub
		boolean go = false;
		if (train.isOnGoing()) {
			go = true;
			//System.out.println("Tren" + train.getTrainID() + " Go!");
			//train.setOnGoing(false);
		}
		return go;
	}
}
