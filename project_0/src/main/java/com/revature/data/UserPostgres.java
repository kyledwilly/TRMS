package com.revature.data;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Offer;
import com.revature.beans.Role;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtil;

public class UserPostgres implements UserDAO {

	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

	@Override
	public User create(User u) throws Exception {
		User user = null;
		
		try(Connection con = cu.getConnection()){
			con.setAutoCommit(false);

			String sql = "insert into users(id, username, passwd, user_role_id) values (default, ?, ? , ?)";
	
			String[] keys = {"id"};
			PreparedStatement pstmt = con.prepareStatement(sql, keys);
			pstmt.setString(1, u.getName());
			pstmt.setString(2, u.getPassword());
			pstmt.setInt(3, u.getRole().getId());
			
			pstmt.executeUpdate();
			
			ResultSet results = pstmt.getGeneratedKeys();
			if(results.next()) {
				user = u;
				u.setId(results.getInt(1));
				con.commit();	
			}
			else {
				con.rollback();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	@Override
	public User read(Integer id) {
		User user = null;
		
		try (Connection con = cu.getConnection()) {
			String sql = "select users.id as user_id, users.username as username, users.passwd as passwd, users.user_role_id as user_role_id, user_role.\"name\" as user_role from users join user_role on users.user_role_id = user_role.id where users.id = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet results = pstmt.executeQuery();
			
			if (results.next()) {
				user = new User();
				user.setId(results.getInt("user_id"));
				user.setName(results.getString("username"));
				
				user.setPassword(results.getString("passwd"));
				Role role = new Role(results.getInt("user_role_id"));
				user.setRole(role);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public User read(String username) {
		User user = null;
		
		try (Connection con = cu.getConnection()) {
			String sql = "select users.id as user_id, users.username as username, users.passwd as passwd, users.user_role_id as user_role_id, user_role.\"name\" as user_role from users join user_role on users.user_role_id = user_role.id where users.username = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet results = pstmt.executeQuery();
			
			if (results.next()) {
				user = new User();
				user.setId(results.getInt("user_id"));
				user.setName(results.getString("username"));
				
				user.setPassword(results.getString("passwd"));
				Role role = new Role(results.getInt("user_role_id"));
				user.setRole(role);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;	
	}
	
	@Override
	public void update(User u) {
		// TODO Auto-generated method stub

	}
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}
	@Override
	public Set<User> List() {
		Set<User> users = new HashSet<>();
		
		try (Connection con = cu.getConnection()) {
			String sql = "select users.id as user_id, users.username as username, users.passwd as passwd, users.user_role_id as user_role_id, user_role.\"name\" as user_role from users join user_role on users.user_role_id = user_role.id";

			Statement statement = con.createStatement();
			ResultSet results = statement.executeQuery(sql);
			
			while (results.next()) {
				User user = new User();
				user.setId(results.getInt("person_id"));
				user.setName(results.getString("username"));
				user.setPassword(results.getString("passwd"));
				Role role = new Role(results.getInt("role_id"));
				user.setRole(role);
				
				//human.setCats(getCatsByPersonId(human.getId(), conn));
				
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return users;
	}

	@Override
	public Integer makeOffer(Offer offer) {
		
		try(Connection con = cu.getConnection()){
			con.setAutoCommit(false);

			String sql = "insert into offer(id, user_id, bike_id, amount) values(default, ?, ?, ?)";
	
			String[] keys = {"id"};
			PreparedStatement pstmt = con.prepareStatement(sql, keys);
			pstmt.setInt(1, offer.getCustomerId());
			pstmt.setInt(2, offer.getBikeId());
			pstmt.setInt(3, offer.getAmount());
			
			pstmt.executeUpdate();
			
			ResultSet results = pstmt.getGeneratedKeys();
			if(results.next()) {
				con.commit();
				return results.getInt(1);
				
			}
			else {
				con.rollback();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
