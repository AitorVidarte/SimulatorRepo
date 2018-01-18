package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.PackageDAO;
import Modelo.Package;

public class PackageController extends Thread {

	ResourcesPool resourcePool;
	boolean paqueteCreado = false;
	List<Package> listaPaquetes;
	PackageDAO packageDao = new PackageDAO();
	
	private int nPackages = 0;

	public PackageController(ResourcesPool resourcePool) {
		this.resourcePool = resourcePool;
		listaPaquetes = new ArrayList<Package>();
	}

	public void run() {

		while (true) {
			descansar();
			if (mirarPaquetesEnBaseDeDatos()) {
				listaPaquetes = cogerPaqutes();
				asignarPaquetes();
			}
		}
	}

	private void descansar() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}

	}

	private boolean mirarPaquetesEnBaseDeDatos() {
		boolean change = false;
		List<Package> paquetes = packageDao.toSendPackageListInBBDD();
		if (nPackages != paquetes.size()) {
			setnPackages(paquetes.size());
			change = true;
		}
		return change;

	}

	private void asignarPaquetes() {
		for (Package pack : listaPaquetes) {
			pack.setPackageState(0);
			resourcePool.actualizarPaquete(pack);
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
