package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
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
		
	}
	
	
	@Override
	public void update(Seller obj) {	
		
	}
	
	
	@Override
	public void deleteById(Integer id) {
		
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
			// Não precisa continuar a conexão
			// o objeto DAO permite realizar mais de uma operação
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
		seller.setBirthDate( rs.getDate(4) );
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