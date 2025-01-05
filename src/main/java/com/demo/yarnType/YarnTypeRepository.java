package com.demo.yarnType;

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
public class YarnTypeRepository {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private UtilsRepository utilsRepo;
	
	public void save(String yarnType) {
		
		log.info("Entering saving.");
		
		utilsRepo.generateNextSeq(Constants.YARN_TYPE_SEQ);
		
		String yarnTypeId = "YRNT"+utilsRepo.getNextSeq(Constants.YARN_TYPE_SEQ);
		
		String sql = null;
		
		sql = "INSERT INTO YARN_TYPE_WORK (YARN_TYPE_ID, WORKING_SEQ, YARN_TYPE, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'CREATED') ";
		
		jdbcTemplate.update(sql, yarnTypeId, "1", yarnType);
		
		authorize(get(yarnTypeId));
		
		log.info("Exiting saving.");
		
	}
	
	public void update(YarnTypeBean yarnTypeBean) {
		
		log.info("Entering update.");
		
		String sql = null;
		
		sql = "DELETE FROM YARN_TYPE_WORK WHERE YARN_TYPE_ID = '"+yarnTypeBean.getYarnTypeId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO YARN_TYPE_WORK (YARN_TYPE_ID, WORKING_SEQ, YARN_TYPE, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'UPDATED') ";
		
		jdbcTemplate.update(sql, yarnTypeBean.getYarnTypeId(), "2", yarnTypeBean.getYarnType());
		
		authorize(get(yarnTypeBean.getYarnTypeId()));
		
		log.info("Exiting update.");
		
	}
	
	public void delete(YarnTypeBean YarnTypeBean) {
		
		log.info("Entering delete.");
		
		String sql = null;
		
		sql = "DELETE FROM YARN_TYPE_WORK WHERE YARN_TYPE_ID = '"+YarnTypeBean.getYarnTypeId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO YARN_TYPE_WORK (YARN_TYPE_ID, WORKING_SEQ, YARN_TYPE, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'DELETED') ";
		
		jdbcTemplate.update(sql, YarnTypeBean.getYarnTypeId(), "2", YarnTypeBean.getYarnType());
		
		authorize(get(YarnTypeBean.getYarnTypeId()));
		
		log.info("Exiting delete.");
		
	}	
	
	public void authorize(YarnTypeBean YarnTypeBean) {
		
		log.info("Entering authorize.");
		
		String sql = null;
		
		sql = "DELETE FROM YARN_TYPE_MASTER WHERE YARN_TYPE_ID = '"+YarnTypeBean.getYarnTypeId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		log.info("action :: "+YarnTypeBean.getAction());
		if(!YarnTypeBean.getAction().equalsIgnoreCase("DELETED")) {
		
			sql = "UPDATE YARN_TYPE_WORK SET STATUS = 'AUTHORIZE', WORKING_SEQ = '1' WHERE YARN_TYPE_ID = '"+YarnTypeBean.getYarnTypeId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
			
			sql = "INSERT INTO YARN_TYPE_MASTER SELECT * FROM YARN_TYPE_WORK WHERE YARN_TYPE_ID = '"+YarnTypeBean.getYarnTypeId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		} else {
			sql = "DELETE FROM YARN_TYPE_WORK WHERE YARN_TYPE_ID = '"+YarnTypeBean.getYarnTypeId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		}
		
		log.info("Exiting authorize.");
		
	}
	
	public void decline(YarnTypeBean YarnTypeBean) {
		
		log.info("Entering decline.");
		
		String sql = null;
		
		sql = "DELETE FROM YARN_TYPE_WORK WHERE YARN_TYPE_ID = '"+YarnTypeBean.getYarnTypeId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO YARN_TYPE_WORK SELECT * FROM YARN_TYPE_MASTER WHERE YARN_TYPE_ID = '"+YarnTypeBean.getYarnTypeId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);		
		
		log.info("Exiting decline.");
		
	}
	
	public List<YarnTypeBean> list() {
		
		log.info("Entering list.");
		
		List<YarnTypeBean> clientBeanList = null;
		
		String sql = "SELECT * FROM YARN_TYPE_WORK";
		log.info("sql :: "+sql);
		
		clientBeanList = (List<YarnTypeBean>) jdbcTemplate.query(sql, new RowMapper<YarnTypeBean>() {
			
			@Override
			public YarnTypeBean mapRow(ResultSet rs, int rowNo)throws SQLException {
				YarnTypeBean clientBean = new YarnTypeBean();
				clientBean.setYarnTypeId(rs.getString("YARN_TYPE_ID"));
				clientBean.setYarnType(rs.getString("YARN_TYPE"));
				clientBean.setStatus(rs.getString("STATUS"));
				clientBean.setAction(rs.getString("ACTION"));
				return clientBean;
			}
		});
		
		log.info("Exiting list.");
		
		return clientBeanList;
		
	}
	
	public YarnTypeBean get(String clientID) {
		
		log.info("Entering get.");
		
		String sql = "SELECT * FROM YARN_TYPE_WORK WHERE YARN_TYPE_ID = '"+clientID+"'";
		
		YarnTypeBean clientBean = jdbcTemplate.query(sql, new ResultSetExtractor<YarnTypeBean>(){
			@Override
			public YarnTypeBean extractData(ResultSet rs)throws SQLException {
				YarnTypeBean clientBean = new YarnTypeBean();
				if(rs.next()) {
					clientBean.setYarnTypeId(rs.getString("YARN_TYPE_ID"));
					clientBean.setYarnType(rs.getString("YARN_TYPE"));
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
