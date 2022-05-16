package DealershipModel;
import jakarta.persistence.*;
import java.util.*;

@Entity(name = "models")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"name", "year"})})
public class Model {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int modelID;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private int year;

    @JoinColumn
    @OneToMany(mappedBy = "model")
    private List<Trim> trims;

    @ManyToMany
    @JoinTable(
            name = "modelFeatures",
            joinColumns = @JoinColumn(name = "modelID"),
            inverseJoinColumns = @JoinColumn(name = "featureID")
    )
    private Set<Feature> features;

    public Model() {
    }

    public Model(int modelID, String name, int year) {
        this.modelID = modelID;
        this.name = name;
        this.year = year;
        this.trims = new ArrayList<>();
        this.features = new HashSet<>();
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Trim> getTrims() {
        return trims;
    }

    public void setTrims(List<Trim> trims) {
        this.trims = trims;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "| Model{ " +
                "modelID= " + modelID +
                ", name= '" + name + '\'' +
                ", year= " + year +
                " } |";
    }
}
