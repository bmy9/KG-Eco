package com.appleyk.repository;

import com.appleyk.node.Report;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends Neo4jRepository<Report, Long>{
	 List<Report> findByName(@Param("name") String name);
}
