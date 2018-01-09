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

		iniciarCircuito();
		// launchThreads();
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
		
		asignarPaquetesAEstaciones();
	}

	private void asignarPaquetesAEstaciones() {
		Station station = null;
		Train train = null;
		for (Package pack : packages) {
			System.out.println(pack.getPackageState());

			if (pack.getPackageState() == 0) {
				station = pack.getOrigin();
				station.addNewPackageToSend(pack);
			} else if (pack.getPackageState() == 2) {
				station = pack.getDestination();
				station.addDeliveredPackageList(pack);
			} else {
				train = pack.getTakeTrain();
				train.addPackageList(pack);
			}

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

	public void launchThreads() {
		// packageController = new PackageController(this);
		// packageController.start();

		for (int i = 0; i < TRAINNUMBER; i++) {
			trainThreads.add(new TrainThread(trains.get(i), circuito));
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
