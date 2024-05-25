package com.worksuite.integration.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Song {

	@Id
	@Column(name = "ID")
	private Long Id;
	
	@Column(name = "NAME")
	private String songName;
	
	@Column(name = "ARTIST")
	private String artist;

	public Long getId() {
		return Id;
	}

	public Song setId(Long id) {
		Id = id;
		return this;
	}

	public String getSongName() {
		return songName;
	}

	public Song setSongName(String songName) {
		this.songName = songName;
		return this;
	}

	public String getArtist() {
		return artist;
	}

	public Song setArtist(String artist) {
		this.artist = artist;
		return this;
	}
}
