package com.demo.labelValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class LabelValueRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public ArrayList<LabelValue> getAllEntities(){
		
		log.info("Entering getAllEntities.");
		
		String sql = "SELECT * FROM ENTITY_MASTER";
		
		ArrayList<LabelValue> allEntities = null;
		
		allEntities = (ArrayList<LabelValue>) jdbcTemplate.query(sql, new RowMapper<LabelValue>() {
			@Override
			public LabelValue mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				LabelValue lblValue = new LabelValue();
				lblValue.setLabel(rs.getString("ENTITY_NAME"));
				lblValue.setValue(rs.getString("ENTITY_ID"));
				
				return lblValue;
			}
		});
		
		log.info("allEntities :: "+allEntities);
		
		log.info("Entering getAllEntities.");
		
		return allEntities;
		
	}
	
	public ArrayList<LabelValue> getAllCounties(){
		
		log.info("Entering getAllCounties.");
		
		String sql = "SELECT * FROM COUNTRIES";
		
		ArrayList<LabelValue> countries = null;
		
		countries = (ArrayList<LabelValue>) jdbcTemplate.query(sql, new RowMapper<LabelValue>() {
			@Override
			public LabelValue mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				LabelValue lblValue = new LabelValue();
				lblValue.setLabel(rs.getString("COUNTRY"));
				lblValue.setValue(rs.getString("CODE"));
				
				return lblValue;
			}
		});
		
		log.info("countries :: "+countries);
		
		log.info("Entering getAllCounties.");
		
		return countries;
		
	}
	
	public ArrayList<LabelValue> getAllStates(String countryCode){
		
		log.info("Entering getAllStates.");
		
		log.info("countryCode :: "+countryCode);
		
		String sql = "SELECT * FROM STATES WHERE COUNTRY_CODE = '"+countryCode+"'";
		
		ArrayList<LabelValue> states = null;
		
		states = (ArrayList<LabelValue>) jdbcTemplate.query(sql, new RowMapper<LabelValue>() {
			@Override
			public LabelValue mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				LabelValue lblValue = new LabelValue();
				lblValue.setLabel(rs.getString("STATE"));
				lblValue.setValue(rs.getString("STATE_CODE"));
				
				return lblValue;
			}
		});
		
		log.info("countries :: "+states);
		
		log.info("Entering getAllStates.");
		
		return states;
		
	}
	
	public ArrayList<LabelValue> getAllCities(String stateCode){
		
		log.info("Entering getAllCities.");
		
		log.info("stateCode :: "+stateCode);
		
		String sql = "SELECT * FROM CITIES WHERE STATE_CODE = '"+stateCode+"'";
		
		ArrayList<LabelValue> countries = null;
		
		countries = (ArrayList<LabelValue>) jdbcTemplate.query(sql, new RowMapper<LabelValue>() {
			@Override
			public LabelValue mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				LabelValue lblValue = new LabelValue();
				lblValue.setLabel(rs.getString("CITY"));
				
				return lblValue;
			}
		});
		
		log.info("countries :: "+countries);
		
		log.info("Entering getAllCities.");
		
		return countries;
		
	}
	
	public ArrayList<String> getClientNameLabelValues() throws Exception {
		log.info("Entering getClientNameLabelValues");

		String sql = null;
		ArrayList<String> listLblVal = null;

		try {
			sql = "SELECT CLIENT_NAME FROM CLIENT_MASTER ";
			log.info("sql :: " + sql);

			listLblVal = (ArrayList<String>) jdbcTemplate.query(sql, new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String labelValue = "'"+rs.getString("CLIENT_NAME")+"'";
					return labelValue;
				}
			});

//			Collections.sort(listLblVal);

		} catch (Exception e) {
			log.info("Could not getClientNameLabelValues data in getClientNameLabelValues of "
					+ LabelValueRepository.class.getName() + e.toString());
			log.error("Could not getClientNameLabelValues data in getClientNameLabelValues of "
					+ LabelValueRepository.class.getName() + e.toString(), e);
			throw new Exception("Could not getClientNameLabelValues data in getClientNameLabelValues of "
					+ LabelValueRepository.class.getName() + e.toString());
		}

		log.info("listLblVal :: " + listLblVal);
		log.info("Exiting getClientNameLabelValues");

		return listLblVal;
	}
	
	public ArrayList<LabelValue> getAllYarnTypes(){
		
		log.info("Entering getAllYarnTypes.");
		
		String sql = "SELECT * FROM YARN_TYPE_MASTER";
		
		ArrayList<LabelValue> yarnTypes = null;
		
		yarnTypes = (ArrayList<LabelValue>) jdbcTemplate.query(sql, new RowMapper<LabelValue>() {
			@Override
			public LabelValue mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				LabelValue lblValue = new LabelValue();
				lblValue.setLabel(rs.getString("YARN_TYPE"));
				lblValue.setValue(rs.getString("YARN_TYPE_ID"));
				
				return lblValue;
			}
		});
		
		log.info("yarnTypes :: "+yarnTypes);
		
		log.info("Entering getAllYarnTypes.");
		
		return yarnTypes;
		
	}
	
	public ArrayList<LabelValue> getAllColorList(){
		
		log.info("Entering getAllColorList.");
		
		String sql = "SELECT * FROM COLOR_MASTER";
		
		ArrayList<LabelValue> yarnColorList = null;
		
		yarnColorList = (ArrayList<LabelValue>) jdbcTemplate.query(sql, new RowMapper<LabelValue>() {
			@Override
			public LabelValue mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				LabelValue lblValue = new LabelValue();
				lblValue.setLabel(rs.getString("COLOR_NAME"));
				lblValue.setValue(rs.getString("COLOR_ID"));
				
				return lblValue;
			}
		});
		
		log.info("yarnColorList :: "+yarnColorList);
		
		log.info("Entering getAllColorList.");
		
		return yarnColorList;
		
	}
}
