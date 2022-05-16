package DealershipModel;
import jakarta.persistence.*;
import java.util.*;

@Entity(name = "automobiles")
public class Automobile {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int autoID;

    @Column(unique = true, length = 17, nullable = false)
    private String vin;

    @JoinColumn(name = "trimID")
    @ManyToOne
    private Trim trim;

    @ManyToMany
    @JoinTable(
            name = "chosenPackages",
            joinColumns = @JoinColumn(name = "autoID"),
            inverseJoinColumns = @JoinColumn(name = "availPackID")
    )
    private Set<AvailablePackage> chosenPackages;

    public Automobile() {
    }

    public Automobile(int autoID, String vin, Trim trim) {
        this.autoID = autoID;
        this.vin = vin;
        this.trim = trim;
        this.chosenPackages = new HashSet<>();
    }

    public int getAutoID() {
        return autoID;
    }

    public void setAutoID(int autoID) {
        this.autoID = autoID;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Trim getTrim() {
        return trim;
    }

    public void setTrim(Trim trim) {
        this.trim = trim;
    }

    public Set<AvailablePackage> getChosenPackages() {
        return chosenPackages;
    }

    public void setChosenPackages(Set<AvailablePackage> chosenPackages) {
        this.chosenPackages = chosenPackages;
    }

    @Override
    public String toString() {
        return "| Automobile{ " +
                "autoID= " + autoID +
                ", vin= '" + vin + '\'' +
                ", trim= " + trim +
                " } |";
    }
}
