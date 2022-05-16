package DealershipModel;
import jakarta.persistence.*;
import java.util.*;

@Entity(name = "packages")
public class Package {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int packageID;

    @Column(nullable = false, length = 40)
    private String name;

    @JoinColumn
    @OneToMany(mappedBy = "aPackage")
    private Set<AvailablePackage> availablePackages;

    @ManyToMany
    @JoinTable(
            name = "packageFeatures",
            joinColumns = @JoinColumn(name = "packageID"),
            inverseJoinColumns = @JoinColumn(name = "featureID")
    )
    private Set<Feature> features;

    public Package() {
    }

    public Package(int packageID, String packageName) {
        this.packageID = packageID;
        this.name = packageName;
        this.features = new HashSet<>();
        this.availablePackages = new HashSet<>();
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AvailablePackage> getAvailablePackages() {
        return availablePackages;
    }

    public void setAvailablePackages(Set<AvailablePackage> availablePackages) {
        this.availablePackages = availablePackages;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "| Package{ " +
                "packageID= " + packageID +
                ", name= '" + name + '\'' +
                "} |";
    }
}
