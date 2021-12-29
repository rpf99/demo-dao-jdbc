package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.SellerDao; // o Dao terá uma dependência com a conexão do banco de dados //
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	
	private Connection conn; // dependencia //
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public void insert(Seller obj) {	
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("INSERT INTO seller(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
			                           "VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				
				DB.closeResultSet(rs);
				
			}else {
				throw new DbException("Erro Inesperado, nenhuma linha foi afetada");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
	}
	
	
	@Override
	public void update(Seller obj) {	
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("UPDATE seller " + 
					                   "SET Name = ?, Email = ? , BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
			                           "WHERE Id = ?");
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			
			ps.executeUpdate();
			
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
			ps = conn.prepareStatement("DELETE FROM SELLER  WHERE Id= ?");
			
			ps.setInt(1, id);
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected == 0) {
				throw new DbException("Vendedor Inexistente");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
	}
	
	
	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName " +
			                           "FROM seller  JOIN department " +
					                   "ON seller.DepartmentId = department.Id " +
			                           "WHERE seller.Id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
						
				return instantiateSeller(rs, dep);
				
			}else {
				return null;
			}
		
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
			/* Não precisa continuar a conexão
			   o objeto DAO permite realizar mais de uma operação */
		}
	}
	
	
	@Override
	public List<Seller> findAll() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.name as DepName " +
		                               "FROM seller  JOIN department " + 
		                               "ON department.Id = seller.DepartmentId  ORDER BY Name");
			
			rs = ps.executeQuery();
			
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			
			while(rs.next()) {
				Department dep = map.get( rs.getInt("DepartmentId") );
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(dep.getId(), dep);
				}
				
				Seller s = instantiateSeller(rs, dep);
				list.add(s);
			}
			
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	
	@Override
	public List<Seller> findByDepartment(Department dep) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName  " +
			                           "FROM seller  JOIN department  " +
			                           "ON seller.DepartmentId = department.Id  " + 
			                           "WHERE DepartmentId = ?   ORDER BY Name");
			
			ps.setInt(1, dep.getId());
		
			rs = ps.executeQuery();
			
			
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			
			while (rs.next()) {
				
				Department department = map.get( rs.getInt("DepartmentId") );
				
				if(department == null) {
					department = instantiateDepartment(rs);
					map.put( rs.getInt("DepartmentId") , department);
				}
				
				Seller s = instantiateSeller(rs, department);
				sellers.add(s);
			}
			
			return sellers;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
			
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		
		seller.setId( rs.getInt("Id") );
		seller.setName( rs.getString("Name") );
		seller.setEmail( rs.getString("Email") );	
		
		seller.setBirthDate(new java.util.Date(rs.getDate("BirthDate").getTime()));
		
		seller.setBaseSalary( rs.getDouble("BaseSalary") );
		seller.setDepartment(dep);

		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		
		dep.setId( rs.getInt("DepartmentId") );
		dep.setName( rs.getString("DepName") );
		
		return dep;
	}
	
	
}