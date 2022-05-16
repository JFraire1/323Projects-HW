package model;

import java.time.*;
import jakarta.persistence.*;

@Entity(name = "viewings")
public class Viewing {
    @Id
    @ManyToOne
    @JoinColumn(name = "artpiece_id")
    private ArtPiece artPiece;

    @Id
    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @Id
    @Column(name = "view_date")
    private LocalDate viewDate;

    @Column(name = "score")
    private int score;

    public Viewing() {
    }

    public Viewing(ArtPiece artPiece, Visitor visitor, LocalDate viewDate, int score) {
        this.artPiece = artPiece;
        this.visitor = visitor;
        this.viewDate = viewDate;
        this.score = score;
    }

    public ArtPiece getArtPiece() { return artPiece; }

    public void setArtPiece(ArtPiece artPiece) { this.artPiece = artPiece; }

    public Visitor getVisitor() { return visitor; }

    public void setVisitor(Visitor visitor) { this.visitor = visitor; }

    public LocalDate getViewDate() { return viewDate; }

    public void setViewDate(LocalDate viewDate) { this.viewDate = viewDate; }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    @Override
    public String toString() {
        return "Viewing{" +
                "artPiece=" + artPiece +
                ", visitor=" + visitor +
                ", viewDate=" + viewDate +
                ", score=" + score +
                '}';
    }
}
