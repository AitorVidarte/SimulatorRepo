package Controller;
/**
 * @file PackageController.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief Package Controller
 */

import java.util.ArrayList;
import java.util.List;

import DAO.PackageDAO;
import Modelo.Package;

public class PackageController extends Thread {

	/** ResourcePool	 */
	ResourcesPool resourcePool;
	/** PackageCreated */
	boolean paqueteCreado = false;
	/** Package list */
	List<Package> listaPaquetes;
	/** Package Dao*/
	PackageDAO packageDao = new PackageDAO();
	/** packageDao */
	private int nPackages = 0;
	
	/**
	 * The constructor of Package Controller.
	 * @param resourcePool
	 * The origin
	 */

	public PackageController(ResourcesPool resourcePool) {
		this.resourcePool = resourcePool;
		listaPaquetes = new ArrayList<Package>();
	}
	
	/**
	 * The thread runnable function. This thread is always doing the thread methods.
	 */
	public void run() {

		while (true) {
			descansar();
			if (mirarPaquetesEnBaseDeDatos()) {
				listaPaquetes = cogerPaqutes();
				asignarPaquetes();
			}
		}
	}
	/**
	 * This method is used to sleep the thread.
	 */
	private void descansar() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}

	}
	/**
	 * This method collects the list of packages from the database.
	 * @return
	 * return package list.
	 */
	private boolean mirarPaquetesEnBaseDeDatos() {
		boolean change = false;
		List<Package> paquetes = packageDao.toSendPackageListInBBDD();
		if (nPackages != paquetes.size()) {
			setnPackages(paquetes.size());
			change = true;
		}
		return change;

	}
	/**
	 * This method assigns the collected packets to the stations and assigns the packet that train has to collect it.
	 */
	private void asignarPaquetes() {
		for (Package pack : listaPaquetes) {
			pack.setPackageState(0);
			resourcePool.actualizarPaquete(pack);
		}
		resourcePool.asignarPaquetesAEstacionesPackageController(listaPaquetes);
		resourcePool.asignarTrenAPaquetePackageController(listaPaquetes);
	}
	
	/**
	 * This method collects the list of packages from the database with the condition that it is package state be 3 "new package".
	 * @return
	 */
	private List<Package> cogerPaqutes() {
		PackageDAO packageDao = new PackageDAO();
		return packageDao.toSendPackageListInBBDD();
	}
	/**
	 * This method set the nPackages value
	 * @param nPackages
	 *	this param changed for check new packages.
	 */
	public void setnPackages(int nPackages) {
		this.nPackages = nPackages;
	}
}
