package com.demo.client;

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
public class ClientRepository {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private UtilsRepository utilsRepo;
	
	public void save(ClientBean clientBean) {
		
		log.info("Entering saving.");
		
		utilsRepo.generateNextSeq(Constants.CLIENT_SEQ);
		
		String clientId = "CLT"+utilsRepo.getNextSeq(Constants.CLIENT_SEQ);
		
		String sql = null;
		
		sql = "INSERT INTO CLIENT_WORK (CLIENT_ID, ENTITY_ID, WORKING_SEQ, CLIENT_NAME, COMPANY_NAME, CONTACT_NO, EMAIL, TELEPHONE_NO, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'CREATED') ";
		
		jdbcTemplate.update(sql, clientId, clientBean.getEntityId(), "1", clientBean.getClientName(), clientBean.getCompanyName(), clientBean.getContactNo(),
				clientBean.getEmail(), clientBean.getTelephoneNo());
		
		log.info("Exiting saving.");
		
	}
	
	public void update(ClientBean clientBean) {
		
		log.info("Entering update.");
		
		String sql = null;
		
		sql = "DELETE FROM CLIENT_WORK WHERE CLIENT_ID = '"+clientBean.getClientId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO CLIENT_WORK (CLIENT_ID, ENTITY_ID, WORKING_SEQ, CLIENT_NAME, COMPANY_NAME, CONTACT_NO, EMAIL, TELEPHONE_NO, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'UPDATED') ";
		
		jdbcTemplate.update(sql, clientBean.getClientId(), clientBean.getEntityId(), "2", clientBean.getClientName(), clientBean.getCompanyName(), clientBean.getContactNo(),
				clientBean.getEmail(), clientBean.getTelephoneNo());
		
		log.info("Exiting update.");
		
	}
	
	public void delete(ClientBean clientBean) {
		
		log.info("Entering delete.");
		
		String sql = null;
		
		sql = "DELETE FROM CLIENT_WORK WHERE CLIENT_ID = '"+clientBean.getClientId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO CLIENT_WORK (CLIENT_ID, ENTITY_ID, WORKING_SEQ, CLIENT_NAME, COMPANY_NAME, CONTACT_NO, EMAIL, TELEPHONE_NO, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'DELETED') ";
		
		jdbcTemplate.update(sql, clientBean.getClientId(), clientBean.getEntityId(), "2", clientBean.getClientName(), clientBean.getCompanyName(), clientBean.getContactNo(),
				clientBean.getEmail(), clientBean.getTelephoneNo());
		
		log.info("Exiting delete.");
		
	}	
	
	public void authorize(ClientBean clientBean) {
		
		log.info("Entering authorize.");
		
		String sql = null;
		
		sql = "DELETE FROM CLIENT_MASTER WHERE CLIENT_ID = '"+clientBean.getClientId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		log.info("action :: "+clientBean.getAction());
		if(!clientBean.getAction().equalsIgnoreCase("DELETED")) {
		
			sql = "UPDATE CLIENT_WORK SET STATUS = 'AUTHORIZE', WORKING_SEQ = '1' WHERE CLIENT_ID = '"+clientBean.getClientId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
			
			sql = "INSERT INTO CLIENT_MASTER SELECT * FROM CLIENT_WORK WHERE CLIENT_ID = '"+clientBean.getClientId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		} else {
			sql = "DELETE FROM CLIENT_WORK WHERE CLIENT_ID = '"+clientBean.getClientId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		}
		
		log.info("Exiting authorize.");
		
	}
	
	public void decline(ClientBean clientBean) {
		
		log.info("Entering decline.");
		
		String sql = null;
		
		sql = "DELETE FROM CLIENT_WORK WHERE CLIENT_ID = '"+clientBean.getClientId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO CLIENT_WORK SELECT * FROM CLIENT_MASTER WHERE CLIENT_ID = '"+clientBean.getClientId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);		
		
		log.info("Exiting decline.");
		
	}
	
	public List<ClientBean> list() {
		
		log.info("Entering list.");
		
		List<ClientBean> clientBeanList = null;
		
		String sql = "SELECT * FROM CLIENT_WORK";
		log.info("sql :: "+sql);
		
		clientBeanList = (List<ClientBean>) jdbcTemplate.query(sql, new RowMapper<ClientBean>() {
			
			@Override
			public ClientBean mapRow(ResultSet rs, int rowNo)throws SQLException {
				ClientBean clientBean = new ClientBean();
				clientBean.setClientId(rs.getString("CLIENT_ID"));
				clientBean.setClientName(rs.getString("CLIENT_NAME"));
				clientBean.setCompanyName(rs.getString("COMPANY_NAME"));
				clientBean.setContactNo(rs.getString("CONTACT_NO"));
				clientBean.setEmail(rs.getString("EMAIL"));
				clientBean.setStatus(rs.getString("STATUS"));
				clientBean.setAction(rs.getString("ACTION"));
				return clientBean;
			}
		});
		
		log.info("Exiting list.");
		
		return clientBeanList;
		
	}
	
	public ClientBean get(String clientID) {
		
		log.info("Entering get.");
		
		String sql = "SELECT * FROM CLIENT_WORK WHERE CLIENT_ID = '"+clientID+"'";
		
		ClientBean clientBean = jdbcTemplate.query(sql, new ResultSetExtractor<ClientBean>(){
			@Override
			public ClientBean extractData(ResultSet rs)throws SQLException {
				ClientBean clientBean = new ClientBean();
				if(rs.next()) {
					clientBean.setEntityId(rs.getString("ENTITY_ID"));
					clientBean.setClientId(rs.getString("CLIENT_ID"));
					clientBean.setClientName(rs.getString("CLIENT_Name"));
					clientBean.setCompanyName(rs.getString("COMPANY_NAME"));
					clientBean.setContactNo(rs.getString("CONTACT_NO"));
					clientBean.setTelephoneNo(rs.getString("TELEPHONE_NO"));
					clientBean.setEmail(rs.getString("EMAIL"));
					clientBean.setStatus(rs.getString("STATUS"));
					clientBean.setAction(rs.getString("ACTION"));
				}
				return clientBean;
			}
		});
		
		log.info("Exiting get.");
		
		return clientBean;
		
	}

}
