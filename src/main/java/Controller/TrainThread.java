package Controller;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

				pedirRail();
				salirEstacion();
				recorreRail();
				entrarEstacion();
				soltarRail();
				entregarPaquete();
				recogerPaquete();

			}

		}
	}

	private void recogerPaquete() {
	for(Package paquete : train.getPackageList()) {
	    if (paquete.getOrigin() == train.getStation()) {
		paquete.setPackageState(1);
		train.getStation().getSendPackageList().remove(paquete);
		System.out.println("Paquete recogido!");
		try {
		    Thread.sleep(2000);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
    }

	private void entregarPaquete() {

		Set<Package> paquetes = train.getPackageList();
		Package paquete;
		Iterator<Package> it = paquetes.iterator();
		int cont = 0;

		while (it.hasNext()) {
			paquete = it.next();
			if (paquete.getDestination() == train.getStation()) {
				paquete.setPackageState(2);
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
		Station station = train.getStation();
		train.getStation().getParks().remove(this);
		// station.avisarTrenWaitingZone(train);
	}

	private void entrarEstacion() {

		Rail rail = train.getRail();

		try {
			Thread.sleep(1000);
			train.setStation(rail.getNextStation());
			System.out.println("\nEl tren:" + train.getTrainID() + " ha entrado en la estacion: "
					+ train.getStation().getDescription());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void recorreRail() {
		// TODO Auto-generated method stub
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
	}

	private void pedirRail() {
		// Rail rail;
		//
		// if (train.getTrainId()==1){
		//
		// rail = circuito.cogerRail(circuito.getRailes().get(0));
		// train.setUsingRail(rail);
		// }
		// if (train.getTrainId()!=1){
		// rail = circuito.cogerRail(circuito.getRailes().get(7));
		// train.setUsingRail(rail);
		// }

		for (Rail rail : circuito.getRailes()) {
			if (train.getDirection() == 0) {
				if ((train.getStation() == rail.getPreviousStation())
						&& (train.getStation().getNextStation() == rail.getNextStation())) {

					circuito.cogerRail(rail);
					train.setRail(rail);
					System.out
							.println("El tren:" + train.getTrainID() + " esta utilizando el rail: " + rail.getRailID());

				}
			} else {
				if ((train.getStation() == rail.getPreviousStation())
						&& (train.getStation().getPreviousStation() == rail.getNextStation())) {
					circuito.cogerRail(rail);
					train.setRail(rail);
					System.out.println(rail.getRailID());

				}
			}

		}

	}

	private void soltarRail() {
		circuito.soltarRail(train.getRail());
	}

	private boolean moverse() {
		// TODO Auto-generated method stub
		boolean go = false;
		if (train.getPackageList().size() != 0) {
			go = true;
			System.out.println("Tren" + train.getTrainID() + " Go!");
			train.setOnGoing(true);
		} else {
			train.setOnGoing(false);
		}
		return go;
	}
}
