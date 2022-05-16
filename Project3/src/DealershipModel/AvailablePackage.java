package DealershipModel;
import jakarta.persistence.*;
import java.util.*;

@Entity(name = "availablePackages")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"trimID", "packageID"})})
public class AvailablePackage {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int availPackID;

    @Column(columnDefinition = "Numeric(7,2)", nullable = false)
    private float cost;

    @JoinColumn(name = "trimID")
    @ManyToOne
    private Trim trim;

    @JoinColumn(name = "packageID")
    @ManyToOne
    private Package aPackage;

    @ManyToMany(mappedBy = "chosenPackages")
    private Set<Automobile> addedAutos = new HashSet<>();

    public AvailablePackage() {
    }

    public AvailablePackage(int availPackID, Trim trim, Package aPackage, float cost) {
        this.availPackID = availPackID;
        this.trim = trim;
        this.aPackage = aPackage;
        this.cost = cost;
        this.addedAutos = new HashSet<>();
    }

    public int getAvailPackID() {
        return availPackID;
    }

    public void setAvailPackID(int availPackID) {
        this.availPackID = availPackID;
    }

    public Trim getTrim() {
        return trim;
    }

    public void setTrim(Trim trim) {
        this.trim = trim;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Set<Automobile> getAddedAutos() {
        return addedAutos;
    }

    public void setAddedAutos(Set<Automobile> addedAutos) {
        this.addedAutos = addedAutos;
    }

    @Override
    public String toString() {
        return "| AvailablePackage{ " +
                "availPackID= " + availPackID +
                ", trim= " + trim +
                ", aPackage= " + aPackage +
                ", cost= " + cost +
                " } |";
    }
}
