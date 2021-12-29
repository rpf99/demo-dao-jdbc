package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	Connection conn = null;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public void insert(Department obj) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("INSERT INTO department(Name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				
				ResultSet rs = ps.getGeneratedKeys();
								
				if(rs.next()) {
					obj.setId(rs.getInt(1));
				}
				
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro Nenhum linha foi afetada");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
	}
	
	
	@Override
	public void update(Department obj) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("UPDATE department  SET Name= ?  WHERE Id = ?");
			
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected == 0) {
				throw new DbException("Departamento Não localizado");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
	}
	
	
	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("DELETE from department WHERE id = ?");
			
			ps.setInt(1,id);
					
			ps.executeUpdate();
		
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
	}
	
	
	@Override
	public Department findById(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT department.* from department  WHERE Id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {				
				return InstantiateDepartment(rs);
			}else {
				throw new DbException("Departamento Não Localizado");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	
	@Override
	public List<Department> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT department.* from department");
			
			rs = ps.executeQuery();
			
			List<Department> department = new ArrayList<>();
			
			while(rs.next()) {
				department.add( InstantiateDepartment(rs) );
			}
			
			return department;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	
	private Department InstantiateDepartment(ResultSet rs) throws SQLException {
		Department d = new Department();
		d.setName( rs.getString("Name") );
		d.setId( rs.getInt("Id") );
		
		return d;
	}
	
}