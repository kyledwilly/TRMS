package com.revature.services;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.User;
import com.revature.data.UserDAO;
import com.revature.data.UserDAOFactory;

public class UserServiceImpl implements UserService {

	private UserDAO userDAO;
	
	
	public UserServiceImpl() {
		UserDAOFactory userDAOFactory = new UserDAOFactory();
		userDAO = userDAOFactory.getUserDAOFactory();
	}
	
	@Override
	public Integer addUser(User u) {
		// TODO Auto-generated method stub
		try{
			User newUser = userDAO.create(u);
			
			return newUser.getId();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User getUserById(Integer id) {
		return userDAO.read(id);
	}

	@Override
	public User getUserByName(String username) {
		return userDAO.read(username);
	}

	@Override
	public Integer makeOffer(Integer bikeId, Integer userId, Integer amount) {
		Offer offer = new Offer();
		offer.setAmount(amount);
		offer.setBikeId(bikeId);
		offer.setCustomerId(userId);
		
		return userDAO.makeOffer(offer);
	}
}