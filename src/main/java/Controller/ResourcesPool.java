package Controller;

import java.util.ArrayList;

import DAO.RailDAO;
import DAO.StationDAO;
import DAO.TrainDAO;
import Modelo.Circuito;
import Modelo.Rail;
import Modelo.Station;
import Modelo.Train;

public class ResourcesPool {

	final static int RIGHTDIRECTION = 0;
	final static int LEFTDIRECTION = 1;
	final static int TRAINNUMBER = 6;
	ArrayList<TrainThread> trainThreads;
	ArrayList<Train> trains;
	ArrayList<Station> stations;
	ArrayList<Rail> rails;
	Circuito circuito;
	PackageController packageController;

	public ResourcesPool() {
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
		rails = new ArrayList<Rail>();
		
		trainThreads = new ArrayList<TrainThread>();
		circuito = new Circuito();

		iniciarCircuito();
		launchThreads();
	}


	private void iniciarCircuito() {
		
		StationDAO stationDao = new StationDAO();
		TrainDAO trainDao = new TrainDAO();
		RailDAO railDao = new RailDAO();
		
		
		this.stations = (ArrayList<Station>) stationDao.list();
		
		this.trains = (ArrayList<Train>) trainDao.list();
		
		for (Station station: stations) {
			for (Train train : trains) {
				if (station.getDescription().equals(train.getStation().getDescription())) {
					station.aparcarTren(train);
					train.setStation(station);
				}
			}
		}
		
		this.rails = (ArrayList<Rail>) railDao.list();
		
		circuito.setEstaciones(stations);
		circuito.setRailes(rails);
	}

	public ArrayList<Train> getTrenesEnUnaDireccion(int direccion) {
		ArrayList<Train> trainsDirection = new ArrayList<Train>();
		for (Train train : trains) {
			if (train.getDirection() == direccion) {
				trainsDirection.add(train);
			}
		}
		return trainsDirection;
	}

	public ArrayList<Train> getTrenesEnUnaDireccionMoviendo(int direccion) {
		ArrayList<Train> trainsDirection = new ArrayList<Train>();
		for (Train train : trains) {
			if (train.getDirection() == direccion && train.isOnGoing()) {
				trainsDirection.add(train);
			}
		}
		return trainsDirection;
	}

	public void launchThreads() {
//		packageController = new PackageController(this);
//		packageController.start();
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
}
