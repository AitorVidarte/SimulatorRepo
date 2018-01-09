package Controller;

import java.util.ArrayList;

import DAO.PackageDAO;
import DAO.RailDAO;
import DAO.StationDAO;
import DAO.TrainDAO;
import Modelo.Circuito;
import Modelo.Rail;
import Modelo.Station;
import Modelo.Train;
import Modelo.Package;

public class ResourcesPool {

	final static int RIGHTDIRECTION = 0;
	final static int LEFTDIRECTION = 1;
	final static int TRAINNUMBER = 6;
	ArrayList<TrainThread> trainThreads;
	ArrayList<Train> trains;
	ArrayList<Station> stations;
	ArrayList<Rail> rails;
	ArrayList<Package> packages;
	Circuito circuito;
	PackageController packageController;

	public ResourcesPool() {
		
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
		rails = new ArrayList<Rail>();
		packages = new ArrayList<Package>();

		trainThreads = new ArrayList<TrainThread>();
		circuito = new Circuito();

		iniciarCircuito(); //leyendo los datos de la base de datos y creando objetos.
		asignarPaquetesAEstaciones(); //asignados los objetos paquete leidos de la base de datos a los objetos estacion.
		createThreads();// creando los hilos tipo Tren pasandole el tren y el circuito.
		
		//launchThreads();
		//asignarPaquetesAEstacionesYtrenes();
		
		
		
	}

	private void iniciarCircuito() {

		StationDAO stationDao = new StationDAO();
		TrainDAO trainDao = new TrainDAO();
		RailDAO railDao = new RailDAO();
		PackageDAO packageDao = new PackageDAO();

		this.stations = (ArrayList<Station>) stationDao.list();
		this.packages = packageDao.packageListInBBDD();
		
		this.trains = (ArrayList<Train>) trainDao.list();
		
		for (Station station : stations) {
			for (Train train : trains) {
				if (station.getDescription().equals(train.getStation().getDescription())) {
					station.aparcarTren(train);
					train.setStation(station);
					stationDao.edit(station);
				}
			}
		}
		

		this.rails = (ArrayList<Rail>) railDao.list();
		
		circuito.setEstaciones(stations);
		circuito.setRailes(rails);

	}

	private void asignarPaquetesAEstacionesYtrenes() {

//		Station station = null;
//		Train train = null;
//		for (Package pack : packages) {
//
//			if (pack.getPackageState() == 0) {
//
//				station = pack.getOrigin();
//				for (Station stat : stations) {
//					if (stat.getStationID() == station.getStationID()) {
//						stat.addNewPackageToSend(pack);
//					}
//				}
//			} else if (pack.getPackageState() == 2) {
//				station = pack.getDestination();
//				for (Station stat : stations) {
//					if (stat.getStationID() == station.getStationID()) {
//						stat.addDeliveredPackageList(pack);
//					}
//				}
//			} else {
//				train = pack.getTakeTrain();
//				for (TrainThread trains : trainThreads) {
//					if (train.getTrainID() == trains.getTrain().getTrainID()) {
//						trains.getTrain().addPackageList(pack);
//					}
//				}
//				train.addPackageList(pack);
//			}

		}
		
		private void asignarPaquetesAEstaciones() {

			Station station = null;
			Train train = null;
			for (Package pack : packages) {

				if (pack.getPackageState() == 0) {

					station = pack.getOrigin();
					for (Station stat : stations) {
						if (stat.getStationID() == station.getStationID()) {
							stat.addNewPackageToSend(pack);
						}
					}
				} else if (pack.getPackageState() == 2) {
					station = pack.getDestination();
					for (Station stat : stations) {
						if (stat.getStationID() == station.getStationID()) {
							stat.addDeliveredPackageList(pack);
						}
					}
				} 
				
			}	
		circuito.setEstaciones(stations);
		for (Station stat : stations) {
			System.out.println("Estacion: " + stat.getDescription() + " tiene " + stat.getSendPackageList().size()
					+ " para enviar!");
			System.out.println("Estacion: " + stat.getDescription() + " tiene " + stat.getDeliveredPackageList().size()
					+ " entregados!");
		}
		
		for (TrainThread trains : trainThreads) {
			System.out.println("El tren "+trains.getTrain().getTrainID()+" tiene "+trains.getTrain().getPackageList().size()+" paquetes!");
		}
	}

	public ArrayList<TrainThread> getTrenesEnUnaDireccion(int direccion) {
		ArrayList<TrainThread> trainsDirection = new ArrayList<TrainThread>();
		for (TrainThread trainThread : trainThreads) {
			if (trainThread.getTrain().getDirection() == direccion) {
				trainsDirection.add(trainThread);
			}
		}
		return trainsDirection;
	}

	public ArrayList<TrainThread> getTrenesEnUnaDireccionMoviendo(int direccion) {
		ArrayList<TrainThread> trainsDirection = new ArrayList<TrainThread>();
		for (TrainThread trainThread : trainThreads) {
			if (trainThread.getTrain().getDirection() == direccion && trainThread.getTrain().isOnGoing()) {
				trainsDirection.add(trainThread);
			}
		}
		return trainsDirection;
	}

	public void createThreads() {
		// packageController = new PackageController(this);
		for (int i = 0; i < TRAINNUMBER; i++) {
			System.out.println("El Tren:"+trains.get(i).getTrainID()+" esta en la estacion: " +trains.get(i).getStation().getDescription()+""
					+ "y la estacion tiene "+ +trains.get(i).getStation().getSendPackageList().size()+" paquetes para recoger!");
			trainThreads.add(new TrainThread(trains.get(i), circuito));
		}
		
	}
	
	public void launchThreads() {
		// packageController.start();
		for (int i = 0; i < TRAINNUMBER; i++) {
			trainThreads.get(i).start();
		}
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	public ArrayList<Train> getTrains() {
		return trains;
	}

	public Circuito getCircuito() {
		return circuito;
	}

	public ArrayList<TrainThread> getTrainThreads() {
		// TODO Auto-generated method stub
		return trainThreads;
	}
}
