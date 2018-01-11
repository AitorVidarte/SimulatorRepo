package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.PackageDAO;
import DAO.StationDAO;
import Modelo.Station;
import Modelo.Train;
import Modelo.Package;

public class PackageController extends Thread {

	ResourcesPool resourcePool;
	boolean paqueteCreado = false;
	List<Package> listaPaquetes;
	List<TrainThread> trainThreads;
	PackageDAO packageDao = new PackageDAO();

	@SuppressWarnings("unused")
	private int nPackages = 0;

	public PackageController(ResourcesPool resourcePool,List<TrainThread> trainThreads) {
		this.resourcePool = resourcePool;
		listaPaquetes = new ArrayList<Package>();
		this.trainThreads = trainThreads;
	}

	public void run() {

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (true) {
			if (mirarPaquetesEnBaseDeDatos()) {
				listaPaquetes = cogerPaqutes();
				asignarPaquetesAEstacion();
				//ponerTrenEnMarcha()
			}
		}
	}

	private void asignarPaquetesAEstacion() {
		StationDAO stationDao = new StationDAO();
		System.out.println(listaPaquetes.size());
		for (Package paquete : listaPaquetes) {
			paquete.setPackageState(0);
			packageDao.edit(paquete);
			resourcePool.getCircuito().getEstaciones().get(0).addNewPackageToSend(paquete);
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
		if (nPackages != paquetes.size()) {
			System.out.println("ok");
			setnPackages(paquetes.size());
			change = true;
		}
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		// ArrayList<TrainThread> trainThreads;
		// trainThreads = resourcePool.getTrainThreads();
		// trainThreads.get(0).ponerEnMarcha();

	}

	private Train buscarTrenParaPaquete(Package paquete) {
		//List<Train> trenes = resourcePool.getTrains();
		//TrainDAO trainDao = new TrainDAO();
		TrainThread trainMejor = null;
		int direccion = calcularDireccionPaquete(paquete);
		int elMejor = 6, distancia;

		for (TrainThread trainThread : resourcePool.getTrenesEnUnaDireccionMoviendo(direccion)) {
			distancia = distanciaEntreEstaciones(trainThread.getTrain().getStation(), paquete.getOrigin(),
					trainThread.getTrain().getDirection());
			if (distancia < elMejor) {
				elMejor = distancia;
				trainMejor = trainThread;
			}
		}
		if (trainMejor == null) {
			for (TrainThread trainThread : resourcePool.getTrenesEnUnaDireccion(direccion)) {
				distancia = distanciaEntreEstaciones(trainThread.getTrain().getStation(), paquete.getOrigin(),
						trainThread.getTrain().getDirection());
				if (distancia < elMejor) {
					elMejor = distancia;
					trainMejor = trainThread;
				}
			}
		}
		trainMejor.getTrain().setOnGoing(true);
		//trainDao.edit(trainMejor.getTrain(), trainMejor.getTrain().getTrainID() - 1);
		return trainMejor.getTrain();
	}

	public void setnPackages(int nPackages) {
		this.nPackages = nPackages;
	}
}
