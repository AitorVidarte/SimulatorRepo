package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.PackageDAO;
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

		while (true) {

			if (mirarPaquetesEnBaseDeDatos()) {
				listaPaquetes = cogerPaqutes();
				asignarPaquetesAEstacion();
				asignarTrenAPaquetes();
				asignarPaquetesATrenes();
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void asignarTrenAPaquetes() {
		// TODO Auto-generated method stub

	}

	private void asignarPaquetesAEstacion() {
		PackageDAO packageDao = new PackageDAO();

		for (Package paquete : listaPaquetes) {
			paquete.getOrigin().addNewPackageToSend(paquete);
			paquete.setPackageState(1);
			packageDao.edit(paquete, paquete.getPackageID() - 1);
			System.out.println(paquete.getOrigin().getDescription() + " introduce "
					+ paquete.getOrigin().getSendPackageList().size());
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

	private Train buscarTrenParaPaquete(Package paquete) {
		Train trainMejor = null;
		int direccion = calcularDireccionPaquete(paquete);
		int elMejor = 6, distancia;
		for (Train train : resourcePool.getTrenesEnUnaDireccionMoviendo(direccion)) {
			distancia = distanciaEntreEstaciones(train.getStation(), paquete.getOrigin(), train.getDirection());
			if (distancia < elMejor) {
				elMejor = distancia;
				trainMejor = train;
			}
		}
		if (trainMejor == null) {
			for (Train train : resourcePool.getTrenesEnUnaDireccion(direccion)) {
				distancia = distanciaEntreEstaciones(train.getStation(), paquete.getOrigin(), train.getDirection());
				if (distancia < elMejor) {
					elMejor = distancia;
					trainMejor = train;
				}
			}
		}
		return trainMejor;
	}

	public void setnPackages(int nPackages) {
		this.nPackages = nPackages;
	}
}
