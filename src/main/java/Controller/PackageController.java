package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.PackageDAO;
import DAO.StationDAO;
import DAO.TrainDAO;
import Modelo.Station;
import Modelo.Train;
import Modelo.Package;

public class PackageController extends Thread {

	ResourcesPool resourcePool;
	boolean paqueteCreado = false;
	List<Package> listaPaquetes;

	@SuppressWarnings("unused")
	private int nPackages = 0;

	public PackageController(ResourcesPool resourcePool) {
		this.resourcePool = resourcePool;
		listaPaquetes = new ArrayList<Package>();
	}

	public void run() {
		try {
			Thread.sleep(5000);
			ponerTrenEnMarcha();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		while (true) {
//			ponerTrenEnMarcha();
//			if (mirarPaquetesEnBaseDeDatos()) {
//				listaPaquetes = cogerPaqutes();
//				asignarPaquetesAEstacion();
////				asignarPaquetesATrenes();
//				ponerTrenEnMarcha();
//			}
//
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}

	private void asignarPaquetesAEstacion() {
		PackageDAO packageDao = new PackageDAO();
//		StationDAO stationDao = new StationDAO();
		
//		Station station = null;
		for (Package paquete : listaPaquetes) {
			for (Station station : resourcePool.getStations()) {
				if (station.getStationID() == paquete.getOrigin().getStationID()) {
					//System.out.println("Entra!");
					station.addNewPackageToSend(paquete);
				}
			}
//			station = paquete.getOrigin();
//			station.addNewPackageToSend(paquete);
			paquete.setPackageState(1);
			packageDao.edit(paquete, paquete.getPackageID() - 1);
			//stationDao.edit(station);
		}

	}

	private List<Package> cogerPaqutes() {
		PackageDAO packageDao = new PackageDAO();
		return packageDao.toSendPackageListInBBDD();
	}

	private boolean mirarPaquetesEnBaseDeDatos() {
		// TODO Auto-generated method stub
		boolean change = false;
		PackageDAO packageDao = new PackageDAO();
		List<Package> paquetes = packageDao.toSendPackageListInBBDD();
		System.out.println(nPackages);
		if (nPackages != paquetes.size()) {
			System.out.println("ok");
			setnPackages(paquetes.size());
			change = true;
		}
		System.out.println(nPackages);
		return change;

	}

	private void asignarPaquetesATrenes() {
		Train train;
		PackageDAO packageDAO = new PackageDAO();
		for (Station station : resourcePool.getStations()) {
			for (Package paquete : station.getSendPackageList()) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!paquete.isAsignadoTren()) {
					train = buscarTrenParaPaquete(paquete);
					paquete.setTakeTrain(train);
					paquete.setAsignadoTren(true);
					packageDAO.add(paquete);
				}
			}
		}
	}

	private int calcularDireccionPaquete(Package paquete) {
		if (distanciaEntreEstaciones(paquete.getOrigin(), paquete.getDestination(),
				0) > distanciaEntreEstaciones(paquete.getOrigin(), paquete.getDestination(), 1)) {
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
	
	private void ponerTrenEnMarcha() {
		resourcePool.ponThreadenMarcha(1);
		
//		ArrayList<TrainThread> trainThreads;
//		trainThreads = resourcePool.getTrainThreads();
//		trainThreads.get(0).ponerEnMarcha();
		
		
		
	}

	private Train buscarTrenParaPaquete(Package paquete) {
		List<Train> trenes = resourcePool.getTrains();
		TrainDAO trainDao = new TrainDAO();
		TrainThread trainMejor = null;
		int direccion = calcularDireccionPaquete(paquete);
		int elMejor = 6, distancia;
		
		for (TrainThread trainThread : resourcePool.getTrenesEnUnaDireccionMoviendo(direccion)) {
			distancia = distanciaEntreEstaciones(trainThread.getTrain().getStation(), paquete.getOrigin(), trainThread.getTrain().getDirection());
			if (distancia < elMejor) {
				elMejor = distancia;
				trainMejor = trainThread;
			}
		}
		if (trainMejor == null) {
			for (TrainThread trainThread : resourcePool.getTrenesEnUnaDireccion(direccion)) {
				distancia = distanciaEntreEstaciones(trainThread.getTrain().getStation(), paquete.getOrigin(), trainThread.getTrain().getDirection());
				if (distancia < elMejor) {
					elMejor = distancia;
					trainMejor = trainThread;
				}
			}
		}
		trainMejor.getTrain().setOnGoing(true);
		trainDao.edit(trainMejor.getTrain(),trainMejor.getTrain().getTrainID()-1);
		return trainMejor.getTrain();
	}

	public void setnPackages(int nPackages) {
		this.nPackages = nPackages;
	}
}
