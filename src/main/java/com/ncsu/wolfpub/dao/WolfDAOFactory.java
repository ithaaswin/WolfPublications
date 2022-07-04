package com.ncsu.wolfpub.dao;

/**
 * Client can contact a DAOFactory class to get the concrete DAO.
 * 
 * This Factory is used to prevent client getting multiple instances of a single
 * DAO Class. We need only single DAO Instance of a DAO type, as multiple
 * instance of DAOs create multiple connections and it creates lot of overhead.
 * 
 * @author vamsi
 * 
 */
public class WolfDAOFactory {

	private static class PublicationDAOHolder {
		static final PublicationDAO INSTANCE = new PublicationDAO();
	}

	public PublicationDAO getPublicationDAO() {
		return PublicationDAOHolder.INSTANCE;
	}
	
	private static class AnalyticsDAOHolder {
		static final AnalyticsDAO INSTANCE = new AnalyticsDAO();
	}
	
	public AnalyticsDAO getAnalyticsDAO() {
		return AnalyticsDAOHolder.INSTANCE;
	}
	
	private static class TransactionsDAOHolder {
		static final TransactionsDAO INSTANCE = new TransactionsDAO();
	}
	
	public TransactionsDAO getTransactionsDAO() {
		return TransactionsDAOHolder.INSTANCE;
	}
	
	private static class DistributorDAOHolder {
		static final  DistributorDAO INSTANCE = new DistributorDAO();
	}
	
	public DistributorDAO getDistributorDAO() {
		return DistributorDAOHolder.INSTANCE;
	}

}
