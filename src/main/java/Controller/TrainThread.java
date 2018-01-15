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
		System.out.println("Tren creado en la estacion:" + train.getStation().getDescription()+train.isOnGoing());
		this.train = train;
		this.resourcePool = resource;
	}

	public void run() {

		while (true) {
			
			if (moverse()) {
				try {
					System.out.println("Estacion:" +train.getStation().getDescription());
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				recogerPaquete();
				entregarPaquete();
				System.out.println("El tren :"+ train.getTrainID() +" tiene: "+train.getPackageList().size()+" paquetes y ha dejado :"+train.getStation().getSendPackageList().size()+" paquetes en la estacion.");
				comprobarSiTieneQueParar();
				if (moverse()) {
					pedirRail();
					System.out.println("El tren esta en "+train.getStation().getDescription());
					salirEstacion();
					recorreRail();
					System.out.println("El tren esta en"+train.getStation().getDescription());
					entrarEstacion();
					soltarRail(train.getRail());
				}
			}else {
				comprobarEstaciones();
				descansar();
			}

		}
	}

	private void descansar() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void comprobarEstaciones() {
		
		for (Station station : resourcePool.getCircuito().getEstaciones()) {
//			System.out.println("El tren: "+train.getTrainID()+" esta mirando en la estacion:"
//					+station.getStationID()+" y tiene "+station.getSendPackageList().size()+" para enviar.");
			
			for (Package package1 : station.getSendPackageList()) {
				if (package1.getTakeTrain().getTrainID() == train.getTrainID()) {
					train.setOnGoing(true);
					//System.out.println(package1.getTakeTrain().getTrainID()+" "+train.getTrainID()+"############ y es para el!");
				}
				else {
					//System.out.println(package1.getTakeTrain().getTrainID()+" "+train.getTrainID()+"############ y no es para el!");
				}
			}
		}
		
	}

	private void comprobarSiTieneQueParar() {
		
		if (train.getPackageList().size() == 0 && tengoPaquetesPorRecoger()) {
			train.setOnGoing(false);
			resourcePool.acutalizarTren(train);
		}
		
	}

	private boolean tengoPaquetesPorRecoger() {
		boolean parar=true;
		for (Station station : resourcePool.getCircuito().getEstaciones()) {
			System.out.println("Estacion: "+station.getDescription()+" tiene :"+station.getSendPackageList().size()+"paquetes ha enviar");
			for (Package stationPackage : station.getSendPackageList()) {
				System.out.println("el paquete lo titene que recoger el tren:"+stationPackage.getTakeTrain().getTrainID() +" y soy el tren: " +train.getTrainID() );
				if(stationPackage.getTakeTrain().getTrainID() == train.getTrainID()) {
					parar=false;
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return parar;
	}

	private void recogerPaquete() {
		Station stations = resourcePool.getCircuito().reservarEstacion(train.getStation().getStationID()-1,true);
		Iterator<Package> itStationPackages = stations.getSendPackageList().iterator();
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
				paquete.setTakeTrain(null);
				resourcePool.actualizarPaquete(paquete);
				train.addPackageList(paquete);
				resourcePool.acutalizarTren(train);
				
				this.getTrain().addPackageList(paquete);
				itStationPackages.remove();
				System.out.println("Paquete recogido!");
			}

		}
		
		resourcePool.getCircuito().despertarTrenes(train.getStation().getStationID()-1);
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
				train.getStation().addDeliveredPackageList(paquete);
				resourcePool.actualizarPaquete(paquete);
				itTrainPackages.remove();
				System.out.println("Paquete entregado!");
				
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
					train.setRail(rail);
					// System.out.println("Rail: " + train.getRail().getRailID());
				}
			} else if (train.getDirection() == 1) {
				if ((train.getStation().getDescription().equals(rail.getPreviousStation().getDescription())) && (train.getStation().getPreviousStation().getDescription().equals(rail.getNextStation().getDescription()))) {
					resourcePool.getCircuito().cogerRail(rail);
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

		for (Station station : resourcePool.getCircuito().getEstaciones() ) {
			
			if (station.getStationID() == train.getTrainID()) {

				station.quitarTren(train);
				resourcePool.acutalizarTren(train);
				//resourcePool.actualizarTodo();
				//resourcePool.acutalizarEstacion(station);
				//stationDao.edit(station);	
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
			}
		}
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
		resourcePool.getCircuito().soltarRail(rail);
		train.setRail(null);
		resourcePool.acutalizarTren(train);
	}

	private boolean moverse() {
		// TODO Auto-generated method stub
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
