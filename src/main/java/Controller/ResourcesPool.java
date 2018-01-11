package Controller;

import java.util.ArrayList;
import java.util.List;

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
	
	List<TrainThread> trainThreads;
	List<Train> trains;
	List<Station> stations;
	List<Rail> rails;
	List<Package> packages;
	
	Circuito circuito;
	PackageController packageController;
	
	StationDAO stationDao = new StationDAO();
	TrainDAO trainDao = new TrainDAO();
	RailDAO railDao = new RailDAO();
	PackageDAO packageDao = new PackageDAO();
	
	public ResourcesPool() {
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
		rails = new ArrayList<Rail>();
		packages = new ArrayList<Package>();
		trainThreads = new ArrayList<TrainThread>();
		circuito = new Circuito();
	}

	// leyendo los datos de la base de datos y creando objetos( trenes,paquetes, railes y estaciones)
	public void iniciarCircuito() {

		

		this.stations = stationDao.list();
		this.packages = packageDao.packageListInBBDD();
		this.trains = trainDao.list();

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
		
		 
		asignarPaquetesAEstaciones(); // asignados los objetos paquete leidos de la base de datos a los objetos estacion.
		createThreads();// creando los hilos tipo Tren pasandole el tren y el circuito.
		asignarTrenAPaquete();
		launchThreads();// lanzando los hilos!

	}
	
	private void asignarPaquetesAEstaciones() {

		Station station = null;
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
	}
	
	private void asignarTrenAPaquete() {
		int elMejor = 6, distancia;
		TrainThread trainMejor = null;
		
		for (Package pack : packages) {
			elMejor = 6;
			trainMejor = null;
			if (pack.getTakeTrain() == null) {
				
				System.out.println("Paquete "+pack.getDescription()+" Dir: "+calcularDireccionPaquete(pack));
				for ( TrainThread trainsMoving : getTrenesEnUnaDireccionMoviendo(calcularDireccionPaquete(pack))){
					distancia = distanciaEntreEstaciones(trainsMoving.getTrain().getStation(), pack.getOrigin(),trainsMoving.getTrain().getDirection());
					
					if (distancia < elMejor) {
						elMejor = distancia;
						trainMejor = trainsMoving;
						System.out.println("###Mejor tren: "+trainMejor.getTrain().getTrainID()+" para paquete "+ pack.getDescription());
						pack.setTakeTrain(trainMejor.getTrain());
						packageDao.edit(pack, pack.getPackageID()-1);
						
						
					}
					
				}
				if (trainMejor == null) {
					for (TrainThread trainStoped : getTrenesEnUnaDireccion(calcularDireccionPaquete(pack))) {
						distancia = distanciaEntreEstaciones(trainStoped.getTrain().getStation(), pack.getOrigin(),trainStoped.getTrain().getDirection());
						
						System.out.println("###"+trainStoped.getTrain().getStation().getStationID()+pack.getOrigin().getStationID());
						
						if (trainStoped.getTrain().getStation().getStationID() == pack.getOrigin().getStationID()) {
							System.out.println(distancia+" dis ###Mejor tren: "+trainMejor.getTrain().getTrainID()+" para paquete "+ pack.getDescription());
							trainMejor = trainStoped;
						}
						else {
							if (distancia < elMejor) {
								elMejor = distancia;
								trainMejor = trainStoped;
								pack.setTakeTrain(trainMejor.getTrain());
								packageDao.edit(pack, pack.getPackageID()-1);
							}
						}
						
					}
					trainMejor.getTrain().setOnGoing(true);
					trainDao.edit(trainMejor.getTrain());
				}
				
			}
			
		}	
	}
	
	private int calcularDireccionPaquete(Package paquete) {
		if (distanciaEntreEstaciones(paquete.getOrigin(), paquete.getDestination(),0) >
		distanciaEntreEstaciones(paquete.getOrigin(), paquete.getDestination(), 1)) {
			return 1;
		}
		return 0;
	}

	private int distanciaEntreEstaciones(Station origin, Station destination, int dir) {
		Station station = origin;
		int i = 0;
		while (station.getStationID() != destination.getStationID()) {
			if (dir == 0) {
				station = station.getNextStation();
			} else {
				station = station.getPreviousStation();
			}
			i++;
		}
		return i;
	}
	
//	private void mostrarDatos() {
//		
//		for (TrainThread trains : trainThreads) {
//			System.out.println("El tren " + trains.getTrain().getTrainID() + " tiene "
//					+ trains.getTrain().getPackageList().size() + " paquetes!");
//		}
//		
//		for (Station station1 : stations) {
//			for (Package pack : station1.getDeliveredPackageList()) {
//			System.out.println("La estacion " + station1.getDescription() + " tiene "
//					+ pack.getDescription() + " paquetes entregados!");
//			}
//		}
//		
//	}

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
			trainThreads.add(new TrainThread(trains.get(i), circuito));
//			System.out.println("El Tren:" + trains.get(i).getTrainID() + " esta en la estacion: "
//					+ trains.get(i).getStation().getDescription() + "" + " y la estacion tiene "
//					+ +trains.get(i).getStation().getSendPackageList().size() + " paquetes para recoger!");
//
//			for (Package pack : trains.get(i).getStation().getSendPackageList()) {
//				System.out.println("Paquete: " + pack.getDescription() + pack.getPackageID()
//						+ " tiene que ser secogido por el tren: " + pack.getTakeTrain().getTrainID());
//			}
		}
		//packageController = new PackageController(this,trainThreads);
	}

	public void launchThreads() {
		// packageController.start();
		for (int i = 0; i < TRAINNUMBER; i++) {
			trainThreads.get(i).start();
		}
		//packageController.start();
	}

	public List<Station> getStations() {
		return stations;
	}

	public List<Train> getTrains() {
		return trains;
	}

	public Circuito getCircuito() {
		return circuito;
	}

	public List<TrainThread> getTrainThreads() {
		// TODO Auto-generated method stub
		return trainThreads;
	}

	public void ponThreadenMarcha(int i) {
		Train train = trainThreads.get(i-1).getTrain();
		train.setOnGoing(true);
		//trainDao.edit(train, train.getTrainID()-1);
		
	}
	
	public void pararThreadenMarcha(int i) {
		Train train = trainThreads.get(i-1).getTrain();
		train.setOnGoing(false);
		//trainDao.edit(train, train.getTrainID()-1);
		
	}
}
