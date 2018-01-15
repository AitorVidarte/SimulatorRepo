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
				asignarPaquetes();
				//ponerTrenEnMarcha()
			}
		}
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
	
private void asignarPaquetes() {
		listaPaquetes.size();
		for (Package pack : listaPaquetes) {
				pack.setPackageState(0);
				resourcePool.actualizarPaquete(pack);
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resourcePool.asignarPaquetesAEstacionesPackageController(listaPaquetes);
		resourcePool.asignarTrenAPaquetePackageController(listaPaquetes);

	}

	private List<Package> cogerPaqutes() {
		PackageDAO packageDao = new PackageDAO();
		return packageDao.toSendPackageListInBBDD();
	}

	public void setnPackages(int nPackages) {
		this.nPackages = nPackages;
	}
}
