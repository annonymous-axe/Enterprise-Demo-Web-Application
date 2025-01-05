package com.demo.yarn;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class YarnRepository {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private UtilsRepository utilsRepo;
	
	public void save(YarnBean yarnBean) {
		
		log.info("Entering saving.");
		
		utilsRepo.generateNextSeq(Constants.YARN_SEQ);
		
		String yarnId = "YRN"+utilsRepo.getNextSeq(Constants.YARN_SEQ);
		
		String sql = null;
		
		sql = "INSERT INTO YARN_WORK (YARN_ID, ENTITY_ID, WORKING_SEQ, YARN_NAME, DENIER, TYPE, REMARKS, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'CREATED') ";
		
		jdbcTemplate.update(sql, yarnId, yarnBean.getEntityId(), "1", yarnBean.getYarnName(), yarnBean.getDenier(), yarnBean.getType(),
				yarnBean.getRemarks());
		
		sql = "INSERT INTO ITEM_DETAIL_WORK (ITEM_ID, COLOR_ID, RATE) "
				+ " VALUES (?, ?, ?) ";
		
		for(ItemDetailBean itemDetailBean : yarnBean.getItemDetailBeanList()) {
			if(itemDetailBean.getRate().compareTo(BigDecimal.ZERO) == 1) {
				jdbcTemplate.update(sql, yarnId, itemDetailBean.getColorId(), itemDetailBean.getRate());
			}
		}
		
		log.info("Exiting saving.");
		
	}
	
	public void update(YarnBean yarnBean) {
		
		log.info("Entering update.");
		
		String sql = null;
		
		sql = "DELETE FROM YARN_WORK WHERE YARN_ID = '"+yarnBean.getYarnId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO YARN_WORK (YARN_ID, ENTITY_ID, WORKING_SEQ, YARN_NAME, DENIER, TYPE, REMARKS, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'UPDATED') ";
		
		jdbcTemplate.update(sql, yarnBean.getYarnId(), yarnBean.getEntityId(), "2", yarnBean.getYarnName(), yarnBean.getDenier(), yarnBean.getType(),
				yarnBean.getRemarks());
		
		sql = "INSERT INTO ITEM_DETAIL_WORK (ITEM_ID, COLOR_ID, RATE) "
				+ " VALUES (?, ?, ?) ";
		
		for(ItemDetailBean itemDetailBean : yarnBean.getItemDetailBeanList()) {
			if(itemDetailBean.getRate().compareTo(BigDecimal.ZERO) == 1) {
				jdbcTemplate.update(sql, yarnBean.getYarnId(), itemDetailBean.getColorId(), itemDetailBean.getRate());
			}
		}		
		
		log.info("Exiting update.");
		
	}
	
	public void delete(YarnBean yarnBean) {
		
		log.info("Entering delete.");
		
		String sql = null;
		
		sql = "DELETE FROM YARN_WORK WHERE YARN_ID = '"+yarnBean.getYarnId()+"'";
		
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO YARN_WORK (YARN_ID, ENTITY_ID, WORKING_SEQ, YARN_NAME, DENIER, TYPE, REMARKS, STATUS, ACTION) "
				+ " VALUE(?, ?, ?, ?, ?, ?, ?, 'INITIATED', 'DELETED') ";
		
		jdbcTemplate.update(sql, yarnBean.getYarnId(), yarnBean.getEntityId(), "2", yarnBean.getYarnName(), yarnBean.getDenier(), yarnBean.getType(),
				yarnBean.getRemarks());
		
		sql = "INSERT INTO ITEM_DETAIL_WORK (ITEM_ID, COLOR_ID, RATE) "
				+ " VALUES (?, ?, ?) ";
		
		for(ItemDetailBean itemDetailBean : yarnBean.getItemDetailBeanList()) {
			if(itemDetailBean.getRate().compareTo(BigDecimal.ZERO) == 1) {
				jdbcTemplate.update(sql, yarnBean.getYarnId(), itemDetailBean.getColorId(), itemDetailBean.getRate());
			}
		}			
		
		log.info("Exiting delete.");
		
	}	
	
	public void authorize(YarnBean yarnBean) {
		
		log.info("Entering authorize.");
		
		String sql = null;
		
		sql = "DELETE FROM YARN_MASTER WHERE YARN_ID = '"+yarnBean.getYarnId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		sql = "DELETE FROM ITEM_DETAIL_MASTER WHERE ITEM_ID = '"+yarnBean.getYarnId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		log.info("action :: "+yarnBean.getAction());
		if(!yarnBean.getAction().equalsIgnoreCase("DELETED")) {
		
			sql = "UPDATE YARN_WORK SET STATUS = 'AUTHORIZE', WORKING_SEQ = '1' WHERE YARN_ID = '"+yarnBean.getYarnId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
			
			sql = "INSERT INTO YARN_MASTER SELECT * FROM YARN_WORK WHERE YARN_ID = '"+yarnBean.getYarnId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
			
			sql = "INSERT INTO ITEM_DETAIL_MASTER SELECT * FROM ITEM_DETAIL_WORK WHERE ITEM_ID = '"+yarnBean.getYarnId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);			
		} else {
			sql = "DELETE FROM YARN_WORK WHERE YARN_ID = '"+yarnBean.getYarnId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);
			
			sql = "DELETE FROM ITEM_DETAIL_WORK WHERE ITEM_ID = '"+yarnBean.getYarnId()+"'";
			log.info("SQL :: "+sql);
			jdbcTemplate.update(sql);			
		}
		
		log.info("Exiting authorize.");
		
	}
	
	public void decline(YarnBean yarnBean) {
		
		log.info("Entering decline.");
		
		String sql = null;
		
		sql = "DELETE FROM YARN_WORK WHERE YARN_ID = '"+yarnBean.getYarnId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO YARN_WORK SELECT * FROM YARN_MASTER WHERE YARN_ID = '"+yarnBean.getYarnId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);	
		
		sql = "DELETE FROM ITEM_DETAIL_WORK WHERE ITEM_ID = '"+yarnBean.getYarnId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);
		
		sql = "INSERT INTO ITEM_DETAIL_WORK SELECT * FROM ITEM_DETAIL_MASTER WHERE ITEM_ID = '"+yarnBean.getYarnId()+"'";
		log.info("SQL :: "+sql);
		jdbcTemplate.update(sql);			
		
		log.info("Exiting decline.");
		
	}
	
	public List<YarnBean> list() {
		
		log.info("Entering list.");
		
		List<YarnBean> yarnBeanList = null;
		
		String sql = "SELECT * FROM YARN_WORK";
		log.info("sql :: "+sql);
		
		yarnBeanList = (List<YarnBean>) jdbcTemplate.query(sql, new RowMapper<YarnBean>() {
			
			@Override
			public YarnBean mapRow(ResultSet rs, int rowNo)throws SQLException {
				YarnBean yarnBean = new YarnBean();
				yarnBean.setYarnId(rs.getString("YARN_ID"));
				yarnBean.setYarnName(rs.getString("YARN_NAME"));
				yarnBean.setDenier(rs.getBigDecimal("DENIER"));
				yarnBean.setType(rs.getString("TYPE"));
				yarnBean.setStatus(rs.getString("STATUS"));
				yarnBean.setAction(rs.getString("ACTION"));
				return yarnBean;
			}
		});
		
		log.info("Exiting list.");
		
		return yarnBeanList;
		
	}
	
	public YarnBean get(String yarnID) {
		
		log.info("Entering get.");
		
		String sql = "SELECT * FROM YARN_WORK WHERE YARN_ID = '"+yarnID+"'";
		
		YarnBean yarnBean = jdbcTemplate.query(sql, new ResultSetExtractor<YarnBean>(){
			@Override
			public YarnBean extractData(ResultSet rs)throws SQLException {
				YarnBean yarnBean = new YarnBean();
				if(rs.next()) {
					yarnBean.setEntityId(rs.getString("ENTITY_ID"));
					yarnBean.setYarnId(rs.getString("YARN_ID"));
					yarnBean.setYarnName(rs.getString("YARN_NAME"));
					yarnBean.setDenier(rs.getBigDecimal("DENIER"));
					yarnBean.setType(rs.getString("TYPE"));
					yarnBean.setRemarks(rs.getString("REMARKS"));
					yarnBean.setStatus(rs.getString("STATUS"));
					yarnBean.setAction(rs.getString("ACTION"));
				}
					
				yarnBean.setItemDetailBeanList(getItemList(yarnBean.getYarnId()));
				
				return yarnBean;
			}
		});
		
		log.info("Exiting get.");
		
		return yarnBean;
		
	}
	
	public ArrayList<ItemDetailBean> getItemList(String yarnID) {
		
		log.info("Entering getItemList.");
		
		String sql = "SELECT IDW.*, CLR.COLOR_NAME FROM ITEM_DETAIL_WORK IDW "
				+ " LEFT JOIN COLOR_MASTER CLR ON CLR.COLOR_ID=IDW.COLOR_ID WHERE IDW.ITEM_ID = '"+yarnID+"'";
		log.info("sql :: "+sql);
		ArrayList<ItemDetailBean> itemDetailBeanList = (ArrayList<ItemDetailBean>) jdbcTemplate.query(sql, new RowMapper<ItemDetailBean>(){
			@Override
			public ItemDetailBean mapRow(ResultSet rs, int rowNo)throws SQLException {
				ItemDetailBean itemDetailBean = new ItemDetailBean();
				itemDetailBean.setColorId(rs.getString("COLOR_ID"));
				itemDetailBean.setColorName(rs.getString("COLOR_NAME"));
				itemDetailBean.setItemId(rs.getString("ITEM_ID"));
				itemDetailBean.setRate(rs.getBigDecimal("RATE"));

				return itemDetailBean;
			}
		});
		
		log.info("Exiting getItemList.");
		
		return itemDetailBeanList;
		
	}	

}
