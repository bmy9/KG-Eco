package com.appleyk.node;

import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonProperty;

@NodeEntity
public class Company extends BaseEntity{

	private Long stkcd;//mid
	private Double markettype; //rating
	private String stknme; //title
	private String conme; //introduction

	@Relationship(type = "is")
	@JsonProperty("公司行业")
	private List<Genre> genres;
	

	public Company() {

	}

	public Long getStkcd() {
		return stkcd;
	}

	public void setStkcd(Long stkcd) {
		this.stkcd = stkcd;
	}

	public Double getMarkettype() {
		return markettype;
	}

	public void setMarkettype(Double markettype) {
		this.markettype = markettype;
	}


	public String getStknme() {
		return stknme;
	}

	public void setStknme(String stknme) {
		this.stknme = stknme;
	}

	public String getConme() {
		return conme;
	}

	public void setConme(String conme) {
		this.conme = conme;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

}
