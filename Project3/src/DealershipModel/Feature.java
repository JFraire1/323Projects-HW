package DealershipModel;
import jakarta.persistence.*;

import java.util.Comparator;

@Entity(name = "features")
public class Feature implements Comparator<Feature>, Comparable<Feature>{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int featureID;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    public Feature(){
    }

    public Feature(int featureID, String name) {
        this.featureID = featureID;
        this.name = name;
    }

    public int getFeatureID() {
        return featureID;
    }

    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "| Feature{ " +
                "featureID= " + featureID +
                ", name= '" + name + '\'' +
                "} |";
    }

    @Override
    public int compare(Feature f1, Feature f2) {
        int compare = f1.name.compareTo(f2.name);
        return Integer.compare(compare, 0);
    }

    @Override
    public int compareTo(Feature o) {
        return this.name.compareTo(o.name);
    }
}
