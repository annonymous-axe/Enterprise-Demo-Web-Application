package com.demo.utility;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UtilsRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void generateNextSeq(String module) {
		log.info("Entering getNextSeq.");
		
		
		String sql = "INSERT INTO "+module+" (TIMESTAMP) VALUE(NOW())";
		
		jdbcTemplate.update(sql);
		
		
		log.info("Exiting getNextSeq.");
	}
	
	public String getNextSeq(String module) {
		log.info("Entering getNextSeq.");
		
		
		String sql = "SELECT SEQ FROM "+module+" ORDER BY SEQ DESC LIMIT 1";
		log.info("sql :: "+sql);
		
		String nextSeq = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException {
				if(rs.next()) {
					String seq = rs.getString("SEQ");
					log.info("seq :: "+seq);
					return seq;
				}else {
					return "1";
				}
			}
		});
		
		log.info("nextSeq :: "+nextSeq);
		
		
		log.info("Exiting getNextSeq.");
		
		return nextSeq;
	}	

}
