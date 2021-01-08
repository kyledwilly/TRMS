package com.revature.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.beans.Role;
import com.revature.beans.User;
import com.revature.data.UserDAO;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	static UserDAO userDao;
	
	@InjectMocks
	static UserServiceImpl userServ;
	
	static Integer userSequenceMock = 1;
	
	@Test
	public void testAddUser() throws Exception {
		User user = new User();
		Role role = new Role();
		user.setId(1);
		user.setName("MockitoTestName");
		user.setPassword("MockitoTestPassword");
		user.setRole(role);
		
		User user2 = new User();
		user2.setId(2);
		user2.setName("MockitoTestName2");
		user2.setPassword("MockitoTestPassword2");
		user2.setRole(role);

		when(userDao.create(user)).thenReturn(user2);
		assertNotEquals(user.getId().intValue(), userServ.addUser(user).intValue());
		
		verify(userDao).create(user);
	}
	
	@Test
	public void testNewUser() {
		User user = new User();
		user.setName("Kyall");
		assertEquals("Kyall", user.getName());
	}
	
	
}
