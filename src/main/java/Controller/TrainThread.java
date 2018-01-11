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
	PackageDAO packageDao = new PackageDAO();
	TrainDAO trainDao = new TrainDAO();

	public TrainThread(Train train, Circuito circuito) {
		System.out.println("Tren creado en la estacion:" + train.getStation().getDescription()+train.isOnGoing());
		this.train = train;
		this.circuito = circuito;
	}

	public void run() {

		while (true) {

			if (moverse()) {
				
				recogerPaquete();
				entregarPaquete();
				System.out.println("El tren tiene: "+train.getPackageList().size()+" paquetes y la estacion :"+train.getStation().getSendPackageList().size());
				comprobarSiTieneQueParar();
				if (moverse()) {
					pedirRail();
					System.out.println("El tren esta en "+train.getStation().getDescription());
					salirEstacion();
					recorreRail();
					System.out.println("El tren esta en"+train.getStation().getDescription());
					entrarEstacion();
				}

				// recogerPaquete();
				// entregarPaquete();
			}

		}
	}

	private void comprobarSiTieneQueParar() {
		
		if (train.getPackageList().size() == 0) {
			train.setOnGoing(false);
		}
		
	}

	private void recogerPaquete() {

		Iterator<Package> itStationPackages = train.getStation().getSendPackageList().iterator();
		Package paquete = null;

		while (itStationPackages.hasNext()) {
			paquete = itStationPackages.next();
			try {
				System.out.println("El tren tiene: "+train.getPackageList().size()+" paquetes y la estacion :"+train.getStation().getSendPackageList().size());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (paquete.getTakeTrain().getTrainID() == train.getTrainID()) {
				paquete.setPackageState(1);
				packageDao.edit(paquete);
				train.addPackageList(paquete);
				this.getTrain().addPackageList(paquete);
				itStationPackages.remove();
				System.out.println("Paquete recogido!");
			}

		}
	}


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
				
				train.getStation().addNewPackageToSend(paquete);
				
				packageDao.edit(paquete);
				
				itTrainPackages.remove();
				System.out.println("Paquete entregado!");
				
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

	private void salirEstacion() {
		Station station = train.getStation();
		station.quitarTren(train);
		this.getTrain().setStation(new Station(" en el rail: "+train.getRail().getRailID()));
	}

	private void entrarEstacion() {

		Station station = train.getStation();
		station.quitarTren(train);
		Train train = this.getTrain();
		train.setStation(train.getRail().getNextStation());
		trainDao.edit(train);
		
		
	}

	private void recorreRail() {
		System.out.println("Recorriendo: ");
		for (int i = 0; i <= 100; i += 10) {
			if (i == 90) {
				try {
					System.out.println("Pidiendo parking a la estacion!");
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
	}

	private void soltarRail(Rail rail) {
		circuito.soltarRail(rail);
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

	public Circuito getCircuito() {
		return circuito;
	}

	public void setCircuito(Circuito circuito) {
		this.circuito = circuito;
	}
}
