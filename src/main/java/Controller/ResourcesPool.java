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
	Circuito circuito;
	PackageController packageController;

	public ResourcesPool() {
		stations = new ArrayList<Station>();
		trains = new ArrayList<Train>();
		trainThreads = new ArrayList<TrainThread>();
		circuito = new Circuito();

		iniciarCircuito();

		launchThreads();
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

	private void iniciarCircuito() {

		stations.add(new Station(1, 1.1, 1.1, "Station1", 1, 1, 1, 1, null, null));
		stations.add(new Station(2, 2.1, 2.1, "Station2", 1, 1, 1, 1, null, null));
		stations.add(new Station(3, 3.1, 3.1, "Station3", 1, 1, 1, 1, null, null));
		stations.add(new Station(4, 4.1, 4.1, "Station4", 1, 1, 1, 1, null, null));
		stations.add(new Station(5, 5.1, 5.1, "Station5", 1, 1, 1, 1, null, null));
		stations.add(new Station(6, 6.1, 6.1, "Station6", 1, 1, 1, 1, null, null));

		trains.add(new Train(1, stations.get(0), RIGHTDIRECTION));
		trains.add(new Train(2, stations.get(1), RIGHTDIRECTION));
		trains.add(new Train(3, stations.get(1), RIGHTDIRECTION));
		trains.add(new Train(4, stations.get(1), LEFTDIRECTION));
		trains.add(new Train(5, stations.get(1), LEFTDIRECTION));
		trains.add(new Train(6, stations.get(5), LEFTDIRECTION));
		
		circuito.setEstaciones(stations);

		circuito.getRailes().add(new Rail(1, stations.get(0), stations.get(1), false));
		circuito.getRailes().add(new Rail(2, stations.get(1), stations.get(2), false));
		circuito.getRailes().add(new Rail(3, stations.get(2), stations.get(3), false));
		circuito.getRailes().add(new Rail(4, stations.get(3), stations.get(4), false));
		circuito.getRailes().add(new Rail(5, stations.get(4), stations.get(5), false));
		circuito.getRailes().add(new Rail(6, stations.get(5), stations.get(0), false));

		circuito.getRailes().add(new Rail(7, stations.get(1), stations.get(0), false));
		circuito.getRailes().add(new Rail(8, stations.get(0), stations.get(5), false));
		circuito.getRailes().add(new Rail(9, stations.get(5), stations.get(4), false));
		circuito.getRailes().add(new Rail(10, stations.get(4), stations.get(3), false));
		circuito.getRailes().add(new Rail(11, stations.get(3), stations.get(2), false));
		circuito.getRailes().add(new Rail(12, stations.get(2), stations.get(1), false));

		StationDAO stationDAO = new StationDAO();
		for (Station station : stations) {
			stationDAO.add(station);
		}

		TrainDAO trainDAO = new TrainDAO();
		for (Train train : trains) {
			trainDAO.add(train);
		}
		
		RailDAO railDAO = new RailDAO();
		for (Rail rail : circuito.getRailes()) {
			railDAO.add(rail);
		}
		
		stations.get(0).setNextStation(stations.get(1));
		stations.get(0).setPreviousStation(stations.get(5));

		stations.get(1).setNextStation(stations.get(2));
		stations.get(1).setPreviousStation(stations.get(0));

		stations.get(2).setNextStation(stations.get(3));
		stations.get(2).setPreviousStation(stations.get(1));

		stations.get(3).setNextStation(stations.get(4));
		stations.get(3).setPreviousStation(stations.get(2));

		stations.get(4).setNextStation(stations.get(5));
		stations.get(4).setPreviousStation(stations.get(3));

		stations.get(5).setNextStation(stations.get(0));
		stations.get(5).setPreviousStation(stations.get(4));
		
		stations.get(0).getParks().add(trains.get(0));
		stations.get(1).getParks().add(trains.get(1));
		stations.get(1).getParks().add(trains.get(2));
		stations.get(1).getParks().add(trains.get(3));
		stations.get(2).getParks().add(trains.get(4));
		stations.get(2).getParks().add(trains.get(5));

		for (Station station : stations) {
			stationDAO.edit(station);
		}
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
		packageController = new PackageController(this);
		packageController.start();
		for (int i = 0; i < TRAINNUMBER; i++) {
			trainThreads.add(new TrainThread(trains.get(i), circuito));
			trainThreads.get(i).start();
		}
	}
}
