package DealershipModel;
import jakarta.persistence.*;
import java.util.*;

@Entity(name = "trims")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"modelID", "name"})})
public class Trim {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trimID;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(columnDefinition = "Numeric(10, 2)", nullable = false)
    private float cost;

    @JoinColumn(name = "modelID")
    @ManyToOne
    private Model model;

    @JoinColumn
    @OneToMany(mappedBy = "trim")
    private Set<AvailablePackage> availablePackages;

    @ManyToMany
    @JoinTable(
            name = "trimFeatures",
            joinColumns = @JoinColumn(name = "trimID"),
            inverseJoinColumns = @JoinColumn(name = "featureID")
    )
    private Set<Feature> features;

    public Trim() {
    }

    public Trim(int trimID, String name, float cost, Model model) {
        this.trimID = trimID;
        this.name = name;
        this.cost = cost;
        this.model = model;
        this.features = new HashSet<>();
        this.availablePackages = new HashSet<>();
    }

    public int getTrimID() {
        return trimID;
    }

    public void setTrimID(int trimID) {
        this.trimID = trimID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    public Set<AvailablePackage> getAvailablePackages() {
        return availablePackages;
    }

    public void setAvailablePackages(Set<AvailablePackage> availablePackages) {
        this.availablePackages = availablePackages;
    }

    @Override
    public String toString() {
        return "| Trim{ " +
                "trimID= " + trimID +
                ", name= '" + name + '\'' +
                ", cost= " + cost +
                ", model= " + model +
                " } |";
    }
}
