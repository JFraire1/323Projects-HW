package model;

import jakarta.persistence.*;
import java.util.*;
import javax.swing.text.View;
import java.time.*;

@Entity(name = "artpieces")
@Table(uniqueConstraints =
    @UniqueConstraint(name = "artpiece_uk", columnNames = { "building_id", "title", "artist", "creationdate"}))
public class ArtPiece {
    @Id
    @Column(name = "artpiece_id")
    private int artpieceID;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "creationdate")
    private LocalDate creationdate;

    @OneToMany(mappedBy = "artPiece")
    private Set<Viewing> viewings;

    public ArtPiece(){}

    public ArtPiece(int artpieceID, Building building, String title, String artist, LocalDate creationdate) {
        this.artpieceID = artpieceID;
        this.building = building;
        this.title = title;
        this.artist = artist;
        this.creationdate = creationdate;
    }

    public Set<Viewing> getViewings() { return viewings; }

    public void setViewings(Set<Viewing> viewings) { this.viewings = viewings; }

    public int getArtpieceID() { return artpieceID; }

    public void setArtpieceID(int artpieceID) { this.artpieceID = artpieceID; }

    public Building getBuilding() { return building; }

    public void setBuilding(Building building) { this.building = building; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }

    public void setArtist(String artist) { this.artist = artist; }

    public LocalDate getCreationdate() { return creationdate; }

    public void setCreationdate(LocalDate creationdate) { this.creationdate = creationdate; }

    @Override
    public String toString() {
        return "ArtPiece{" +
                "artpieceID=" + artpieceID +
                ", building=" + building +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", creationdate=" + creationdate +
                '}';
    }

}
