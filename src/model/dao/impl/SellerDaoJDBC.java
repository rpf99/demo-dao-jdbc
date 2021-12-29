package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
			                           "FROM seller INNER JOIN department " +
					                   "ON seller.DepartmentId = department.Id " +
			                           "WHERE seller.Id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				
				Department dep = new Department();
				
				dep.setId( rs.getInt("DepartmentId") );
				dep.setName( rs.getString("DepName") );
				
				
				Seller seller = new Seller();
				
				seller.setId( rs.getInt("Id") );
				seller.setName( rs.getString("Name") );
				seller.setEmail( rs.getString("Email") );	
				seller.setBirthDate( rs.getDate("BirthDate") );
				seller.setBaseSalary( rs.getDouble("BaseSalary") );
				seller.setDepartment(dep);
				
				
				return seller;
			}else {
				return null;
			}
		
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
			// DB.closeConnection(); // Não precisa continuar a conexão
			// o objeto DAO permite realizar mais de uma operação
		}
	}
	
	
	@Override
	public List<Seller> findAll() {
		return null;
	}

}