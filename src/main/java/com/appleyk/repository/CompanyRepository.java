package com.appleyk.repository;

import com.appleyk.node.Company;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends Neo4jRepository<Company, Long>{

	default List<Company> findByTitle(@Param("stknme") String stknme) {
		return null;
	}

	@Query("match(n:Person)-[:actedin]->(m:Movie) where n.name={} return m.title")

	List<String> getCompanyTitles();
}
