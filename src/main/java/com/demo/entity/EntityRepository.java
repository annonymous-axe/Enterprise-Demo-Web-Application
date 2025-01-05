package com.demo.entity;

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
public class EntityRepository {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private UtilsRepository utilsRepo;
	
	public void save(EntityBean entityBean) {
		
		log.info("Entering saving.");
		
		utilsRepo.generateNextSeq(Constants.ENTITY_SEQ);
		
		String entityId = "ENT"+utilsRepo.getNextSeq(Constants.ENTITY_SEQ);
		
		String sql = null;
		
		sql = "INSERT INTO ENTITY_WORK (ENTITY_ID, ENTITY_NAME, WORKING_SEQ, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'CREATED') ";
		
		jdbcTemplate.update(sql, entityId, entityBean.getEntityName(), "1");
		
		entityBean.setEntityId(entityId);
		authorize(entityBean);
		
		log.info("Exiting saving.");
		
	}
	
	public void update(EntityBean entityBean) {
		
		log.info("Entering update.");
		
		String sql = null;
		
		sql = "DELETE FROM ENTITY_WORK WHERE ENTITY_ID = '"+entityBean.getEntityId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO ENTITY_WORK (ENTITY_ID, ENTITY_NAME, WORKING_SEQ, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'UPDATED') ";
		
		jdbcTemplate.update(sql, entityBean.getEntityId(), entityBean.getEntityName(), "2");
		
		log.info("Exiting update.");
		
	}
	
	public void delete(EntityBean entityBean) {
		
		log.info("Entering delete.");
		
		String sql = null;
		
		sql = "DELETE FROM ENTITY_WORK WHERE ENTITY_ID = '"+entityBean.getEntityId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO ENTITY_WORK (ENTITY_ID, ENTITY_NAME, WORKING_SEQ, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'DELETED') ";
		
		jdbcTemplate.update(sql, entityBean.getEntityId(), entityBean.getEntityName(), "2");
		
		log.info("Exiting delete.");
		
	}	
	
	public void authorize(EntityBean entityBean) {
		
		log.info("Entering authorize.");
		
		String sql = null;
		
		sql = "DELETE FROM ENTITY_MASTER WHERE ENTITY_ID = '"+entityBean.getEntityId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		if(!entityBean.getAction().equalsIgnoreCase("DELETED")) {
		
			sql = "UPDATE ENTITY_WORK SET STATUS = 'AUTHORIZE', WORKING_SEQ = '1' WHERE ENTITY_ID = '"+entityBean.getEntityId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
			
			sql = "INSERT INTO ENTITY_MASTER SELECT * FROM ENTITY_WORK WHERE ENTITY_ID = '"+entityBean.getEntityId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		} else {
			sql = "DELETE FROM ENTITY_WORK WHERE ENTITY_ID = '"+entityBean.getEntityId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		}
		
		log.info("Exiting authorize.");
		
	}
	
	public void decline(EntityBean entityBean) {
		
		log.info("Entering decline.");
		
		String sql = null;
		
		sql = "DELETE FROM ENTITY_WORK WHERE ENTITY_ID = '"+entityBean.getEntityId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO ENTITY_WORK SELECT * FROM ENTITY_MASTER WHERE ENTITY_ID = '"+entityBean.getEntityId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);		
		
		log.info("Exiting decline.");
		
	}
	
	public List<EntityBean> list() {
		
		log.info("Entering list.");
		
		List<EntityBean> entityBeanList = null;
		
		String sql = "SELECT * FROM ENTITY_WORK";
		log.info("sql :: "+sql);
		
		entityBeanList = (List<EntityBean>) jdbcTemplate.query(sql, new RowMapper<EntityBean>() {
			
			@Override
			public EntityBean mapRow(ResultSet rs, int rowNo)throws SQLException {
				EntityBean employeeBean = new EntityBean();
				employeeBean.setEntityId(rs.getString("ENTITY_ID"));
				employeeBean.setEntityName(rs.getString("ENTITY_NAME"));
				employeeBean.setStatus(rs.getString("STATUS"));
				employeeBean.setAction(rs.getString("ACTION"));
				return employeeBean;
			}
		});
		
		log.info("Exiting list.");
		
		return entityBeanList;
		
	}
	
	public EntityBean get(String employeeID) {
		
		log.info("Entering get.");
		
		String sql = "SELECT * FROM ENTITY_WORK WHERE ENTITY_ID = '"+employeeID+"'";
		
		EntityBean clientBean = jdbcTemplate.query(sql, new ResultSetExtractor<EntityBean>(){
			@Override
			public EntityBean extractData(ResultSet rs)throws SQLException {
				EntityBean employeeBean = new EntityBean();
				if(rs.next()) {
					employeeBean.setEntityId(rs.getString("ENTITY_ID"));
					employeeBean.setEntityName(rs.getString("ENTITY_NAME"));
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
