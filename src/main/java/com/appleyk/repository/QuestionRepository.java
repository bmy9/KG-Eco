package com.appleyk.repository;

import com.appleyk.node.Company;
import com.appleyk.node.Company;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends Neo4jRepository<Company,Long> {

	/**
	 * 0 对应问题模板0 == nm(公司) 市场类型
	 *
	 * @param title
	 *            公司名
	 * @return 返回公司的市场类型
	 */
	@Query("match(n:Company) where n.title={title} return n.markettype")
	String getMarkettype(@Param("title") String title);

	/**
	 * 1 对应问题模板1 == nm(公司) 股票代码
	 *
	 * @param title
	 *            公司名
	 * @return 返回公司的股票代码
	 */
	@Query("match(n:Movie) where n.title={title} return n.mid")
	String getCompanyCode(@Param("title") String title);

	/**
	 * 2 对应问题模板2 == nm(公司) 行业
	 *
	 * @param title
	 *            公司名
	 * @return 返回所属行业
	 */
	@Query("match(n:Movie)-[r:is]->(b:Genre) where n.title={title} return b.name")
	List<String> getCompanyIndustry(@Param("title") String title);

	/**
	 * 3 对应问题模板3 == nm(公司) 全称
	 *
	 * @param title
	 *            公司名
	 * @return 返回公司全称
	 */
	@Query("match(n:Movie) where n.title ={title} return n.introduction")
	String getCompanyFull(@Param("title") String title);

	/**
	 * 4 对应问题模板4 == nm(公司) 交易所
	 *
	 * @param title
	 *            公司名
	 * @return 返回公司所在的交易所
	 */
	@Query("match(n:Person)-[:isreportof]-(m:Movie) where m.title = {title} return n.name LIMIT 1")
	List<String> getCompanyTradePlace(@Param("title") String title);

	/**
	 * 5 对应问题模板5 == nnt(报告) 后利润
	 *
	 * @param title
	 *            报告
	 * @return 返回后利润
	 */
	@Query("match(n:Person)-[:isreportof]-(m:Movie) where m.title ={title} return n.birthplace LIMIT 1")
	String getCompanyMoney(@Param("title") String title);


	/**
	 * 6对应问题模板6 == nm(公司) 季度
	 *
	 * @param title
	 * @return
	 */
	@Query("match(n:Person)-[:isreportof]-(m:Movie) where m.title ={title} return n.biography LIMIT 1")
	List<String> getCompanySeason(@Param("title") String title);



	/**
	 * 7 对应问题模板7 == nm(公司) 年报数量
	 *
	 * @param title
	 *            公司名
	 * @return 返回年报数量
	 */
	@Query("match(n)-[:isreportof]-(m) where m.title ={title} return count(n)")
	Integer getReportNum(@Param("title") String title);

	/**
	 * 8 对应问题模板8 == nnt(报告) 年报日期
	 *
	 * @param title
	 *            报告
	 * @return 返回年报日期
	 */
	@Query("match(n)-[:isreportof]-(m) where m.title ={title} return n.birth LIMIT 1")
	String getReportDate(@Param("title") String title);

}
