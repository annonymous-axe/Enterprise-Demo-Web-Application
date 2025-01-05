package com.demo.employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.demo.utility.Constants;
import com.demo.utility.UtilsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EmployeeRepository {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private UtilsRepository utilsRepo;
	
	public void save(EmployeeBean employeeBean) {
		
		log.info("Entering saving.");
		
		utilsRepo.generateNextSeq(Constants.EMPLOYEE_SEQ);
		
		String employeeId = "EMP"+utilsRepo.getNextSeq(Constants.EMPLOYEE_SEQ);
		
		String sql = null;
		
		sql = "INSERT INTO EMPLOYEE_WORK (EMPLOYEE_ID, ENTITY_ID, WORKING_SEQ, EMPLOYEE_NAME, CONTACT_NO, EMAIL, TELEPHONE_NO, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'CREATED') ";
		
		jdbcTemplate.update(sql, employeeId, employeeBean.getEntityId(), "1", employeeBean.getEmpName(), employeeBean.getContactNo(),
				employeeBean.getEmail(), employeeBean.getTelephoneNo());
		
		log.info("Exiting saving.");
		
	}
	
	public void update(EmployeeBean employeeBean) {
		
		log.info("Entering update.");
		
		String sql = null;
		
		sql = "DELETE FROM EMPLOYEE_WORK WHERE EMPLOYEE_ID = '"+employeeBean.getEmpId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO EMPLOYEE_WORK (EMPLOYEE_ID, ENTITY_ID, WORKING_SEQ, EMPLOYEE_NAME, CONTACT_NO, EMAIL, TELEPHONE_NO, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'UPDATED') ";
		
		jdbcTemplate.update(sql, employeeBean.getEmpId(), employeeBean.getEntityId(), "2", employeeBean.getEmpName(), employeeBean.getContactNo(),
				employeeBean.getEmail(), employeeBean.getTelephoneNo());
		
		log.info("Exiting update.");
		
	}
	
	public void delete(EmployeeBean employeeBean) {
		
		log.info("Entering delete.");
		
		String sql = null;
		
		sql = "DELETE FROM EMPLOYEE_WORK WHERE EMPLOYEE_ID = '"+employeeBean.getEmpId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO EMPLOYEE_WORK (EMPLOYEE_ID, ENTITY_ID, WORKING_SEQ, EMPLOYEE_NAME, CONTACT_NO, EMAIL, TELEPHONE_NO, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'DELETED') ";
		
		jdbcTemplate.update(sql, employeeBean.getEmpId(), employeeBean.getEntityId(), "2", employeeBean.getEmpName(), employeeBean.getContactNo(),
				employeeBean.getEmail(), employeeBean.getTelephoneNo());
		
		log.info("Exiting delete.");
		
	}	
	
	public void authorize(EmployeeBean employeeBean) {
		
		log.info("Entering authorize.");
		
		String sql = null;
		
		sql = "DELETE FROM EMPLOYEE_MASTER WHERE EMPLOYEE_ID = '"+employeeBean.getEmpId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		if(!employeeBean.getAction().equalsIgnoreCase("DELETED")) {
		
			sql = "UPDATE EMPLOYEE_WORK SET STATUS = 'AUTHORIZE', WORKING_SEQ = '1' WHERE EMPLOYEE_ID = '"+employeeBean.getEmpId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
			
			sql = "INSERT INTO EMPLOYEE_MASTER SELECT * FROM EMPLOYEE_WORK WHERE EMPLOYEE_ID = '"+employeeBean.getEmpId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		} else {
			sql = "DELETE FROM EMPLOYEE_WORK WHERE EMPLOYEE_ID = '"+employeeBean.getEmpId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		}
		
		log.info("Exiting authorize.");
		
	}
	
	public void decline(EmployeeBean employeeBean) {
		
		log.info("Entering decline.");
		
		String sql = null;
		
		sql = "DELETE FROM EMPLOYEE_WORK WHERE EMPLOYEE_ID = '"+employeeBean.getEmpId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO EMPLOYEE_WORK SELECT * FROM EMPLOYEE_MASTER WHERE EMPLOYEE_ID = '"+employeeBean.getEmpId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);		
		
		log.info("Exiting decline.");
		
	}
	
	public List<EmployeeBean> list() {
		
		log.info("Entering list.");
		
		List<EmployeeBean> employeeBeanList = null;
		
		String sql = "SELECT * FROM EMPLOYEE_WORK";
		log.info("sql :: "+sql);
		
		employeeBeanList = (List<EmployeeBean>) jdbcTemplate.query(sql, new RowMapper<EmployeeBean>() {
			
			@Override
			public EmployeeBean mapRow(ResultSet rs, int rowNo)throws SQLException {
				EmployeeBean employeeBean = new EmployeeBean();
				employeeBean.setEmpId(rs.getString("EMPLOYEE_ID"));
				employeeBean.setEmpName(rs.getString("EMPLOYEE_NAME"));
				employeeBean.setContactNo(rs.getString("CONTACT_NO"));
				employeeBean.setEmail(rs.getString("EMAIL"));
				employeeBean.setStatus(rs.getString("STATUS"));
				employeeBean.setAction(rs.getString("ACTION"));
				return employeeBean;
			}
		});
		
		log.info("Exiting list.");
		
		return employeeBeanList;
		
	}
	
	public EmployeeBean get(String employeeID) {
		
		log.info("Entering get.");
		
		String sql = "SELECT * FROM EMPLOYEE_WORK WHERE EMPLOYEE_ID = '"+employeeID+"'";
		
		EmployeeBean clientBean = jdbcTemplate.query(sql, new ResultSetExtractor<EmployeeBean>(){
			@Override
			public EmployeeBean extractData(ResultSet rs)throws SQLException {
				EmployeeBean employeeBean = new EmployeeBean();
				if(rs.next()) {
					employeeBean.setEntityId(rs.getString("ENTITY_ID"));
					employeeBean.setEmpId(rs.getString("EMPLOYEE_ID"));
					employeeBean.setEmpName(rs.getString("EMPLOYEE_Name"));
					employeeBean.setContactNo(rs.getString("CONTACT_NO"));
					employeeBean.setTelephoneNo(rs.getString("TELEPHONE_NO"));
					employeeBean.setEmail(rs.getString("EMAIL"));
					employeeBean.setStatus(rs.getString("STATUS"));
					employeeBean.setAction(rs.getString("ACTION"));
				}
				return employeeBean;
			}
		});
		
		log.info("Exiting get.");
		
		return clientBean;
		
	}

}
