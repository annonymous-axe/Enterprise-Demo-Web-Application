package com.demo.color;

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
public class ColorRepository {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private UtilsRepository utilsRepo;
	
	public void save(String colorName) {
		
		log.info("Entering saving.");
		
		utilsRepo.generateNextSeq(Constants.COLOR_SEQ);
		
		String colorId = "CLR"+utilsRepo.getNextSeq(Constants.COLOR_SEQ);
		
		String sql = null;
		
		sql = "INSERT INTO COLOR_WORK (COLOR_ID, WORKING_SEQ, COLOR_NAME, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'CREATED') ";
		
		jdbcTemplate.update(sql, colorId, "1", colorName);
		
		authorize(get(colorId));
		
		log.info("Exiting saving.");
		
	}
	
	public void update(ColorBean colorBean) {
		
		log.info("Entering update.");
		
		String sql = null;
		
		sql = "DELETE FROM COLOR_WORK WHERE COLOR_ID = '"+colorBean.getColorId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO COLOR_WORK (COLOR_ID, WORKING_SEQ, COLOR_NAME, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'UPDATED') ";
		
		jdbcTemplate.update(sql, colorBean.getColorId(), "2", colorBean.getColorName());
		
		authorize(get(colorBean.getColorId()));
		
		log.info("Exiting update.");
		
	}
	
	public void delete(ColorBean colorBean) {
		
		log.info("Entering delete.");
		
		String sql = null;
		
		sql = "DELETE FROM COLOR_WORK WHERE COLOR_ID = '"+colorBean.getColorId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO COLOR_WORK (COLOR_ID, WORKING_SEQ, COLOR_NAME, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, 'INITIATED', 'DELETED') ";
		
		jdbcTemplate.update(sql, colorBean.getColorId(), "2", colorBean.getColorName());
		
		authorize(get(colorBean.getColorId()));
		
		log.info("Exiting delete.");
		
	}	
	
	public void authorize(ColorBean colorBean) {
		
		log.info("Entering authorize.");
		
		String sql = null;
		
		sql = "DELETE FROM COLOR_MASTER WHERE COLOR_ID = '"+colorBean.getColorId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		log.info("action :: "+colorBean.getAction());
		if(!colorBean.getAction().equalsIgnoreCase("DELETED")) {
		
			sql = "UPDATE COLOR_WORK SET STATUS = 'AUTHORIZE', WORKING_SEQ = '1' WHERE COLOR_ID = '"+colorBean.getColorId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
			
			sql = "INSERT INTO COLOR_MASTER SELECT * FROM COLOR_WORK WHERE COLOR_ID = '"+colorBean.getColorId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		} else {
			sql = "DELETE FROM COLOR_WORK WHERE COLOR_ID = '"+colorBean.getColorId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
		}
		
		log.info("Exiting authorize.");
		
	}
	
	public void decline(ColorBean colorBean) {
		
		log.info("Entering decline.");
		
		String sql = null;
		
		sql = "DELETE FROM COLOR_WORK WHERE COLOR_ID = '"+colorBean.getColorId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO COLOR_WORK SELECT * FROM COLOR_MASTER WHERE COLOR_ID = '"+colorBean.getColorId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);		
		
		log.info("Exiting decline.");
		
	}
	
	public List<ColorBean> list() {
		
		log.info("Entering list.");
		
		List<ColorBean> clientBeanList = null;
		
		String sql = "SELECT * FROM COLOR_WORK";
		log.info("sql :: "+sql);
		
		clientBeanList = (List<ColorBean>) jdbcTemplate.query(sql, new RowMapper<ColorBean>() {
			
			@Override
			public ColorBean mapRow(ResultSet rs, int rowNo)throws SQLException {
				ColorBean clientBean = new ColorBean();
				clientBean.setColorId(rs.getString("COLOR_ID"));
				clientBean.setColorName(rs.getString("COLOR_NAME"));
				clientBean.setStatus(rs.getString("STATUS"));
				clientBean.setAction(rs.getString("ACTION"));
				return clientBean;
			}
		});
		
		log.info("Exiting list.");
		
		return clientBeanList;
		
	}
	
	public ColorBean get(String clientID) {
		
		log.info("Entering get.");
		
		String sql = "SELECT * FROM COLOR_WORK WHERE COLOR_ID = '"+clientID+"'";
		
		ColorBean clientBean = jdbcTemplate.query(sql, new ResultSetExtractor<ColorBean>(){
			@Override
			public ColorBean extractData(ResultSet rs)throws SQLException {
				ColorBean clientBean = new ColorBean();
				if(rs.next()) {
					clientBean.setColorId(rs.getString("COLOR_ID"));
					clientBean.setColorName(rs.getString("COLOR_Name"));
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
