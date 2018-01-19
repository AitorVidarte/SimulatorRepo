package Controller;

/**
 * @file ResourcesPool.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief Resources Pool
 */

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

	/** trainThreads */
	List<TrainThread> trainThreads;
	/** trains */
	List<Train> trains;
	/** stations */
	List<Station> stations;
	/** rails */
	List<Rail> rails;
	/** packages */
	List<Package> packages;
	/** circuit */
	Circuito circuito;
	/** packageController */
	PackageController packageController;
	/** stationDao */
	StationDAO stationDao = new StationDAO();
	/** trainDao */
	TrainDAO trainDao = new TrainDAO();
	/** railDao */
	RailDAO railDao = new RailDAO();
	/** packageDao */
	PackageDAO packageDao = new PackageDAO();
	/** packages */

	boolean blockedThread[] = { false, false, false, false, false, false };

	/**
	 * The constructor of Resources Pool.
	 */

	public ResourcesPool() {
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
		rails = new ArrayList<Rail>();
		packages = new ArrayList<Package>();
		trainThreads = new ArrayList<TrainThread>();
		circuito = new Circuito();
	}

	// leyendo los datos de la base de datos y creando objetos( trenes,paquetes,
	// railes y estaciones)

	/**
	 * This method initiates the circuit of the simulator by reading the data from
	 * the database.
	 */

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

		rails.add(new Rail(1, stations.get(0), stations.get(1), false));
		rails.add(new Rail(2, stations.get(1), stations.get(2), false));
		rails.add(new Rail(3, stations.get(2), stations.get(3), false));
		rails.add(new Rail(4, stations.get(3), stations.get(4), false));
		rails.add(new Rail(5, stations.get(4), stations.get(5), false));
		rails.add(new Rail(6, stations.get(5), stations.get(0), false));

		rails.add(new Rail(7, stations.get(1), stations.get(0), false));
		rails.add(new Rail(8, stations.get(0), stations.get(5), false));
		rails.add(new Rail(9, stations.get(5), stations.get(4), false));
		rails.add(new Rail(10, stations.get(4), stations.get(3), false));
		rails.add(new Rail(11, stations.get(3), stations.get(2), false));
		rails.add(new Rail(12, stations.get(2), stations.get(1), false));

		for (Rail rail : rails) {
			railDao.edit(rail);
		}

		circuito.setRailes(rails);

		asignarPaquetesAEstaciones(); // asignados los objetos paquete leidos de la base de datos a los objetos
										// estacion.
		createThreads();// creando los hilos tipo Tren pasandole el tren y el circuito.
		asignarTrenAPaquete();
		launchThreads();// lanzando los hilos!

	}

	/**
	 * This method looks at the origin of each package and enters the package in the
	 * corresponding station.
	 */

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

	/**
	 * This method looks at the origin of each package and introduces the package in
	 * the corresponding station and is used by the Package Controller
	 * 
	 * @param packages
	 * recive the package list
	 */
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

	/**
	 * This method looks for the train with the shortest route to assign which train
	 * has to pick up the package and is used by the Package Controller.
	 * 
	 * @param packages
	 * recived package list
	 */
	public void asignarTrenAPaquetePackageController(List<Package> packages) {
		int elMejor = 6, distancia, dir = 0;
		TrainThread trainMejor = new TrainThread(new Train(), this);
		boolean trenAsignado = false;
		for (Package pack : packages) {
			elMejor = 6;
			trainMejor = new TrainThread(new Train(), this);
			dir = calcularDireccionPaquete(pack);
			if (pack.getTakeTrain() == null && pack.getPackageState() == 0) {
				for (TrainThread trainMoving : getTrenesEnUnaDireccionMoviendo()) {
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

	/**
	 * This method looks for the train with the shortest route to assign which train
	 * has to pick up the package.
	 */
	private void asignarTrenAPaquete() {
		int elMejor = 6, distancia, dir = 0;
		TrainThread trainMejor = new TrainThread(new Train(), this);
		boolean trenAsignado = false;

		for (Package pack : packages) {
			elMejor = 6;
			trainMejor = new TrainThread(new Train(), this);

			dir = calcularDireccionPaquete(pack);

			if (pack.getTakeTrain() == null && pack.getPackageState() == 0) {

				System.out.println("Paquete " + pack.getDescription() + " Dir: " + dir);

				for (TrainThread trainMoving : getTrenesEnUnaDireccionMoviendo()) {
					if (trainMoving.getTrain().getDirection() == dir) {

						distancia = distanciaEntreEstaciones(trainMoving.getTrain().getStation(), pack.getOrigin(),
								trainMoving.getTrain().getDirection());

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
							distancia = distanciaEntreEstaciones(trainStoped.getTrain().getStation(), pack.getOrigin(),
									trainStoped.getTrain().getDirection());

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

	/**
	 * This method calculates which address has to take the package with its origin
	 * and destination.
	 * 
	 * @param paquete
	 * @return return package direction.
	 */
	private int calcularDireccionPaquete(Package paquete) {
		if (distanciaEntreEstaciones(paquete.getOrigin(), paquete.getDestination(),
				0) > distanciaEntreEstaciones(paquete.getOrigin(), paquete.getDestination(), 1)) {
			return 1;
		}
		return 0;
	}

	/**
	 * This method calculates the distance between different stations.
	 * 
	 * @param origin
	 * @param destination
	 * @param dir
	 * @return return distance between different stations.
	 */
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

	/**
	 * This method get stopped train in one direction.
	 * 
	 * @return return the stopped train list in one direction.
	 */
	public ArrayList<TrainThread> getTrenesEnUnaDireccionParados() {
		ArrayList<TrainThread> trainsDirection = new ArrayList<TrainThread>();
		for (TrainThread trainThread : trainThreads) {
			if (!trainThread.getTrain().isOnGoing()) {
				trainsDirection.add(trainThread);
			}
		}
		return trainsDirection;
	}

	/**
	 * * This method get moving train in one direction.
	 * 
	 * @return return the moving train list in one direction.
	 */
	public List<TrainThread> getTrenesEnUnaDireccionMoviendo() {
		List<TrainThread> trainsDirection = new ArrayList<TrainThread>();

		for (TrainThread trainThread : trainThreads) {
			if (trainThread.getTrain().isOnGoing()) {
				trainsDirection.add(trainThread);
			}
		}
		return trainsDirection;
	}

	/**
	 * This method create new threads.
	 */
	public void createThreads() {
		for (int i = 0; i < TRAINNUMBER; i++) {
			trainThreads.add(new TrainThread(trains.get(i), this));
		}
		packageController = new PackageController(this);
	}

	/**
	 * This method launch created threads.
	 */
	public void launchThreads() {
		for (int i = 0; i < TRAINNUMBER; i++) {
			trainThreads.get(i).start();
		}
		packageController.start();
	}

	/**
	 * 
	 * @return return train list.
	 */
	public List<Train> getTrains() {
		return trains;
	}

	/**
	 * 
	 * @return return circuit.
	 */
	public Circuito getCircuito() {
		return circuito;
	}

	/**
	 * 
	 * @return return trainThreads.
	 */
	public List<TrainThread> getTrainThreads() {
		return trainThreads;
	}

	/**
	 * This method update package in data base.
	 * 
	 * @param paquete
	 * get a package
	 */
	public void actualizarPaquete(Package paquete) {
		packageDao.edit(paquete);
	}

	/**
	 * This method update train in data base.
	 * 
	 * @param train
	 * get train to update it.
	 */
	public void acutalizarTren(Train train) {
		trainDao.edit(train);
	}

	/**
	 * This method update station in data base.
	 * 
	 * @param station
	 * get the station parameter
	 */
	public void acutalizarEstacion(Station station) {
		stationDao.edit(station);
	}

	/**
	 * This method update rail in data base.
	 * 
	 * @param rail
	 * get the rail parameter
	 */
	public void actualizarRail(Rail rail) {
		railDao.edit(rail);
	}

	/**
	 * This waking method moves a train from a station, it acts when a train wants
	 * to enter a station and the station is full.
	 * 
	 * @param station
	 * get station
	 */
	public void moverTrenesParados(Station station) {
		boolean movido = false;
		for (TrainThread traTh : trainThreads) {
			if ((traTh.getTrain().getStation().getStationID() == station.getStationID()) && !movido) {
				traTh.getTrain().setOnGoing(true);
				trainDao.edit(traTh.getTrain());
				movido = true;
			}
		}
	}

	/**
	 * Sets the trains
	 * @param trains
	 * the trains
	 */
	public void setTrains(List<Train> trains) {
		this.trains = trains;
	}

	/**
	 * Sets the stations
	 * @param stations
	 * the stations
	 */
	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
	/**
	 * Gets the stations
	 * @return
	 * the stations
	 */
	public List<Station> getStations() {
		return stations;
	}

	/**
	 * Sets the packages
	 * @param packages
	 * the packages
	 */
	public void setPackages(List<Package> packages) {
		this.packages = packages;
	}
	/**
	 * Sets Circuito
	 * @param circuito
	 * the circuit
	 */
	public void setCircuito(Circuito circuito) {
		this.circuito = circuito;
	}
	
}
