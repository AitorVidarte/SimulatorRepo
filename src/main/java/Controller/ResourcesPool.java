package Controller;

import java.util.ArrayList;
import java.util.List;

import org.sonarsource.scanner.api.internal.cache.Logger;

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
	Logger logger;
	
	public ResourcesPool() {
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
		rails = new ArrayList<Rail>();
		packages = new ArrayList<Package>();
		trainThreads = new ArrayList<TrainThread>();
		circuito = new Circuito();
	}

	// leyendo los datos de la base de datos y creando objetos( trenes,paquetes, railes y estaciones)
	public void iniciarCircuitoDesdeBBDD() {

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

		rails.add(new Rail(1,stations.get(0),stations.get(1),false));
		rails.add(new Rail(2,stations.get(1),stations.get(2),false));
		rails.add(new Rail(3,stations.get(2),stations.get(3),false));
		rails.add(new Rail(4,stations.get(3),stations.get(4),false));
		rails.add(new Rail(5,stations.get(4),stations.get(5),false));
		rails.add(new Rail(6,stations.get(5),stations.get(0),false));
		
		rails.add(new Rail(7,stations.get(1),stations.get(0),false));
		rails.add(new Rail(8,stations.get(0),stations.get(5),false));
		rails.add(new Rail(9,stations.get(5),stations.get(4),false));
		rails.add(new Rail(10,stations.get(4),stations.get(3),false));
		rails.add(new Rail(11,stations.get(3),stations.get(2),false));
		rails.add(new Rail(12,stations.get(2),stations.get(1),false));
		
		for (Rail rail: rails) {
			railDao.edit(rail);
		}

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
						stat.addDeliveredPackageList(new Package());
						stationDao.edit(station);
					}
				}
			} else if (pack.getPackageState() == 2) {
				station = pack.getDestination();
				for (Station stat : stations) {
					if (stat.getStationID() == station.getStationID()) {
						stat.addDeliveredPackageList(pack);
						stationDao.edit(station);
					}
				}
			}

		}
		circuito.setEstaciones(stations);
	}

	public void asignarPaquetesAEstacionesPackageController(List<Package> packages) {

		Station station = null;
		for (Package pack : packages) {

			if (pack.getPackageState() == 0) {

				station = pack.getOrigin();
				for (Station stat : stations) {
					if (stat.getStationID() == station.getStationID()) {
						stat.addNewPackageToSend(pack);
						stat.addDeliveredPackageList(new Package());
						stationDao.edit(station);
					}
				}
			} else if (pack.getPackageState() == 2) {
				station = pack.getDestination();
				for (Station stat : stations) {
					if (stat.getStationID() == station.getStationID()) {
						stat.addDeliveredPackageList(pack);
						stationDao.edit(station);
					}
				}
			}

		}
		circuito.setEstaciones(stations);
	}
	
//Cambiar funcion
	
	public void asignarTrenAPaquetePackageController(List<Package> packages) {
		int elMejor = 6, distancia,dir=0;
		TrainThread trainMejor = new TrainThread(new Train(),this);
		boolean trenAsignado = false;
		
		for (Package pack : packages) {
			elMejor = 6;
			trainMejor = new TrainThread(new Train(),this);
			
			dir = calcularDireccionPaquete(pack);
			
			if (pack.getTakeTrain() == null && pack.getPackageState() == 0) {
				
				System.out.println("Paquete "+pack.getDescription()+" Dir: "+dir);
				
				for ( TrainThread trainMoving : getTrenesEnUnaDireccionMoviendo()){
					if (trainMoving.getTrain().getDirection() == dir) {
						
						distancia = distanciaEntreEstaciones(trainMoving.getTrain().getStation(), pack.getOrigin(),trainMoving.getTrain().getDirection());
						
						if (distancia < elMejor) {
							elMejor = distancia;
							trainMejor = trainMoving;
							pack.setTakeTrain(trainMejor.getTrain());
							packageDao.edit(pack);	
							trenAsignado = true;
						}
						
					}
					
					
				}
				if (!trenAsignado) {
					for (TrainThread trainStoped : getTrenesEnUnaDireccionParados()) {
						if (trainStoped.getTrain().getDirection() == dir) {
							distancia = distanciaEntreEstaciones(trainStoped.getTrain().getStation(), pack.getOrigin(),trainStoped.getTrain().getDirection());

							if (distancia < elMejor) {
								elMejor = distancia;
								trainMejor = trainStoped;
								pack.setTakeTrain(trainMejor.getTrain());
								packageDao.edit(pack);
							}
							
						}
					}
					trainMejor.getTrain().setOnGoing(true);
					trainDao.edit(trainMejor.getTrain());
				}
				
			}
			
		}	
	}
	
	private void asignarTrenAPaquete() {
		int elMejor = 6, distancia,dir=0;
		TrainThread trainMejor = new TrainThread(new Train(),this);
		boolean trenAsignado = false;
		
		for (Package pack : packages) {
			elMejor = 6;
			trainMejor = new TrainThread(new Train(),this);
			
			dir = calcularDireccionPaquete(pack);
			
			if (pack.getTakeTrain() == null && pack.getPackageState() == 0) {
				
				System.out.println("Paquete "+pack.getDescription()+" Dir: "+dir);
				
				for ( TrainThread trainMoving : getTrenesEnUnaDireccionMoviendo()){
					if (trainMoving.getTrain().getDirection() == dir) {
						
						distancia = distanciaEntreEstaciones(trainMoving.getTrain().getStation(), pack.getOrigin(),trainMoving.getTrain().getDirection());
						
						if (distancia < elMejor) {
							elMejor = distancia;
							trainMejor = trainMoving;
							pack.setTakeTrain(trainMejor.getTrain());
							packageDao.edit(pack);	
							trenAsignado = true;
						}
						
					}
					
					
				}
				if (!trenAsignado) {
					for (TrainThread trainStoped : getTrenesEnUnaDireccionParados()) {
						if (trainStoped.getTrain().getDirection() == dir) {
							distancia = distanciaEntreEstaciones(trainStoped.getTrain().getStation(), pack.getOrigin(),trainStoped.getTrain().getDirection());

							if (distancia < elMejor) {
								elMejor = distancia;
								trainMejor = trainStoped;
								pack.setTakeTrain(trainMejor.getTrain());
								packageDao.edit(pack);
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

	public ArrayList<TrainThread> getTrenesEnUnaDireccionParados() {
		ArrayList<TrainThread> trainsDirection = new ArrayList<TrainThread>();
		for (TrainThread trainThread : trainThreads) {
			if (!trainThread.getTrain().isOnGoing()) {
				trainsDirection.add(trainThread);
			}
		}
		return trainsDirection;
	}

	public List<TrainThread> getTrenesEnUnaDireccionMoviendo() {
		List<TrainThread> trainsDirection = new ArrayList<TrainThread>();
		
		for (TrainThread trainThread : trainThreads) {
			if (trainThread.getTrain().isOnGoing()) {
				trainsDirection.add(trainThread);
			}
		}
		return trainsDirection;
	}

	public void createThreads() {
		for (int i = 0; i < TRAINNUMBER; i++) {
			trainThreads.add(new TrainThread(trains.get(i),this));
		}
		packageController = new PackageController(this);
	}

	public void launchThreads() {
		for (int i = 0; i < TRAINNUMBER; i++) {
			trainThreads.get(i).start();
		}
		packageController.start();
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
		return trainThreads;
	}

	public void actualizarPaquete(Package paquete) {
		packageDao.edit(paquete);
	}

	public void acutalizarTren(Train train) {
		trainDao.edit(train);	
	}

	public void acutalizarEstacion(Station station) {
		stationDao.edit(station);	
	}

	public void actualizarRail(Rail rail) {
		railDao.edit(rail);
	}
}
