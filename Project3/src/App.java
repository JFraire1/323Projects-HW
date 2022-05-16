import DealershipModel.Package;
import jakarta.persistence.*;
import java.util.*;
import DealershipModel.*;

public class App{
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dealershipDB");
    private static final EntityManager em = entityManagerFactory.createEntityManager();
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args){
        int input = -1;

        while(input != 0){
            System.out.println("""
                    1 Add
                    2 Find 
                    3 Get Sticker Price
                    0 Exit            
                    """);

            input = validateIntInput();
            switch (input) {
                case 1 -> addItem();
                case 2 -> findItem();
                case 3 -> getStickerPrice();
                case 0 -> System.out.println("\nHave a good day.\n");
                default -> System.out.println("No Such Option\n");
            }
        }
    }

    public static void getStickerPrice(){
        System.out.print("Enter Automobile's ID: ");
        int input = validateIntInput();
        Automobile auto = em.find(Automobile.class, input);

        if (auto == null){
            System.out.printf("No Such Automobile With ID %d%n\n", input);
            return;
        }

        System.out.println("Your Selected Automobile:\n" + auto);
        float stickerPrice = 0.0f;
        stickerPrice += auto.getTrim().getCost();
        for (AvailablePackage aPackage: auto.getChosenPackages()){
            stickerPrice += aPackage.getCost();
        }

        System.out.printf("Sticker Price: %f%n\n", stickerPrice);
    }

    public static void addItem(){
        int input = -1;
        while (input != 0){
            System.out.println("""
                    1 Add Automobile
                    2 Add Automobile-Package (ChosenPackage)
                    3 Add Model
                    4 Add Model-Feature 
                    5 Add Trim
                    6 Add Trim-Feature
                    7 Add Trim-Package (AvailablePackage) 
                    8 Add Package
                    9 Add Package-Feature 
                    10 Add Feature
                    0 Back          
                    """);

            input = validateIntInput();
            switch (input) {
                case 1 -> addAutomobile();
                case 2 -> addAutomobilePackage();
                case 3 -> addModel();
                case 4 -> addModelFeature();
                case 5 -> addTrim();
                case 6 -> addTrimFeature();
                case 7 -> addTrimPackage();
                case 8 -> addPackage();
                case 9 -> addPackageFeature();
                case 10 -> addFeature();
                case 0 -> System.out.println("");
                default -> System.out.println("No Such Option\n");

            }
        }
    }

    public static void findItem(){
        int input = -1;
        while (input != 0){
            System.out.println("""
                    1 Find An Automobile
                    2 Find An Automobile's Packages
                    3 Find An Automobile's Features
                    4 Find A Model
                    5 Find a Model's Features
                    5 Find A Trim
                    8 Find A Trim's Features
                    9 Find A Trim's Available Packages
                    6 Find A Package
                    10 Find A Package's Features
                    11 Find A Package's Compatible Trims
                    0 Back          
                    """);

            input = validateIntInput();
            switch (input) {
                case 1 -> findAutomobile();
                case 2 -> findAutomobilePackages();
                case 3 -> findAutomobileFeatures();
                case 4 -> findModel();
                case 5 -> findModelFeatures();
                case 6 -> findTrim();
                case 7 -> findTrimFeatures();
                case 8 -> findTrimPackages();
                case 9 -> findPackage();
                case 10 -> findPackageFeatures();
                case 11 -> findPackageTrims();
                case 0 -> System.out.println("");
                default -> System.out.println("No Such Option\n");

            }
        }

    }

    private static void addAutomobile() {
        scan.nextLine();
        System.out.println("Enter VIN:");
        String vin = scan.nextLine();
        vin = vin.replaceAll("[^a-zA-Z0-9]", "");
        if (vin.length() != 17){
            System.out.println("Invalid VIN\n");
            return;
        }

        System.out.println("Enter ID of Automobile's Trim: ");
        int trimID = validateIntInput();
        Trim trim;
        if ((trim = em.find(Trim.class, trimID)) == null){
            System.out.println("Invalid Trim ID\n");
            return;
        }

        Automobile auto = new Automobile();
        auto.setVin(vin);
        auto.setTrim(trim);

        em.getTransaction().begin();
        em.persist(auto);
        em.getTransaction().commit();

        System.out.printf("Successfully added Automobile:\n%s\n", auto);
    }

    private static void addAutomobilePackage() {
        scan.nextLine();
        System.out.println("Enter ID of Automobile to Add To: ");
        int automobileID = validateIntInput();
        Automobile automobile;
        if ((automobile = em.find(Automobile.class, automobileID)) == null){
            System.out.println("Invalid Automobile ID\n");
            return;
        }

        System.out.println("Enter ID of Package To Add: ");
        int aPackageID = validateIntInput();
        Package aPackage;
        if ((aPackage = em.find(Package.class, aPackageID)) == null){
            System.out.println("Invalid Package ID\n");
            return;
        }

        String jpaQuery = "SELECT availPack FROM availablePackages availPack join availPack.trim carTrim, availPack.aPackage pack " +
                "WHERE carTrim.trimID = " + automobile.getTrim().getTrimID() + " AND pack.packageID = " + aPackageID;

        try {
            AvailablePackage availPack = em.createQuery(jpaQuery, AvailablePackage.class).getSingleResult();
            automobile.getChosenPackages().add(availPack);
            availPack.getAddedAutos().add(automobile);
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }

        em.getTransaction().begin();
        em.persist(automobile);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Package: %s\nTo Automobile: %s", aPackage, automobile);
    }

    private static void addModel() {
        scan.nextLine();
        System.out.println("Enter Name:");
        String name = scan.nextLine();
        if (name.length() > 20){
            System.out.println("Name Too Long, Must be Maximum 20 Characters\n");
            return;
        }

        System.out.println("Enter Year: ");
        int year = validateIntInput();
        if (year < 1884){
            System.out.println("Invalid Year\n");
            return;
        }

        Model model = new Model();
        model.setName(name);
        model.setYear(year);

        em.getTransaction().begin();
        em.persist(model);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Model: \n%s\n", model);
    }

    private static void addModelFeature() {
        scan.nextLine();
        System.out.println("Enter ID of Model to Add To: ");
        int modelID = validateIntInput();
        Model model;
        if ((model = em.find(Model.class, modelID)) == null){
            System.out.println("Invalid Model ID\n");
            return;
        }

        System.out.println("Enter ID of Feature To Add: ");
        int featureID = validateIntInput();
        Feature feature;
        if ((feature = em.find(Feature.class, featureID)) == null){
            System.out.println("Invalid Feature ID\n");
            return;
        }

        model.getFeatures().add(feature);

        em.getTransaction().begin();
        em.persist(model);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Feature: %s\nTo Model: %s\n", feature, model);
    }

    private static void addTrim() {
        scan.nextLine();
        System.out.println("Enter Name:");
        String name = scan.nextLine();
        if (name.length() > 20){
            System.out.println("Name Too Long, Must be Maximum 20 Characters\n");
            return;
        }

        System.out.println("Enter Cost: ");
        float cost = validateFloatInput();
        if (cost <= 0.0f){
            System.out.println("Invalid Cost\n");
            return;
        }

        System.out.println("Enter ID of Trim's Model: ");
        int modelID = validateIntInput();
        Model model;
        if ((model = em.find(Model.class, modelID)) == null){
            System.out.println("Invalid Model ID\n");
            return;
        }

        Trim trim = new Trim();
        trim.setName(name);
        trim.setCost(cost);
        trim.setModel(model);
        model.getTrims().add(trim);

        em.getTransaction().begin();
        em.persist(trim);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Trim: \n%s\n", trim);
    }

    private static void addTrimFeature() {
        scan.nextLine();
        System.out.println("Enter ID of Trim to Add To: ");
        int trimID = validateIntInput();
        Trim trim;
        if ((trim = em.find(Trim.class, trimID)) == null){
            System.out.println("Invalid Trim ID\n");
            return;
        }

        System.out.println("Enter ID of Feature To Add: ");
        int featureID = validateIntInput();
        Feature feature;
        if ((feature = em.find(Feature.class, featureID)) == null){
            System.out.println("Invalid Feature ID\n");
            return;
        }
        
        trim.getFeatures().add(feature);

        em.getTransaction().begin();
        em.persist(trim);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Feature: %s\nTo Trim: %s\n", feature, trim);

    }

    private static void addPackage() {
        scan.nextLine();
        System.out.println("Enter Name:");
        String name = scan.nextLine();
        if (name.length() > 20){
            System.out.println("Name Too Long, Must be Maximum 20 Characters\n");
            return;
        }

        Package aPackage = new Package();
        aPackage.setName(name);

        em.getTransaction().begin();
        em.persist(aPackage);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Package: \n%s\n", aPackage);

    }

    private static void addPackageFeature() {
        scan.nextLine();
        System.out.println("Enter ID of Package to Add To: ");
        int packageID = validateIntInput();
        Package aPackage;
        if ((aPackage = em.find(Package.class, packageID)) == null){
            System.out.println("Invalid Package ID\n");
            return;
        }

        System.out.println("Enter ID of Feature To Add: ");
        int featureID = validateIntInput();
        Feature feature;
        if ((feature = em.find(Feature.class, featureID)) == null){
            System.out.println("Invalid Feature ID\n");
            return;
        }

        aPackage.getFeatures().add(feature);

        em.getTransaction().begin();
        em.persist(aPackage);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Feature: %s\nTo Package: %s\n", feature, aPackage);

    }

    private static void addTrimPackage() {
        scan.nextLine();
        System.out.println("Enter ID of Trim to Add To: ");
        int trimID = validateIntInput();
        Trim trim;
        if ((trim = em.find(Trim.class, trimID)) == null){
            System.out.println("Invalid Trim ID\n");
            return;
        }

        System.out.println("Enter ID of Package To Add: ");
        int packageID = validateIntInput();
        Package aPackage;
        if ((aPackage = em.find(Package.class, packageID)) == null){
            System.out.println("Invalid Package ID\n");
            return;
        }

        System.out.println("Enter Cost: ");
        float cost = validateFloatInput();
        if (cost <= 0.0f){
            System.out.println("Invalid Cost\n");
            return;
        }

        AvailablePackage availPack = new AvailablePackage();
        availPack.setaPackage(aPackage);
        availPack.setCost(cost);
        availPack.setTrim(trim);

        em.getTransaction().begin();
        em.persist(availPack);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Package: %s\nTo Trim: %s\n", aPackage, trim);
    }

    private static void addFeature() {
        scan.nextLine();
        System.out.println("Enter Name:");
        String name = scan.nextLine();
        if (name.length() > 20){
            System.out.println("Name Too Long, Must be Maximum 20 Characters\n");
            return;
        }

        Feature feature = new Feature();
        feature.setName(name);

        em.getTransaction().begin();
        em.persist(feature);
        em.getTransaction().commit();

        System.out.printf("Successfully Added Feature: \n %s \n", feature);

    }

    private static void findAutomobile() {
        String jpaQuery = "SELECT auto FROM automobiles auto ";
        scan.nextLine();
        System.out.println("Enter the requested values or an empty line if unknown");

        System.out.println("Enter VIN: ");
        String vin = scan.nextLine();
        if (vin.length() != 0){
            jpaQuery += "WHERE auto.vin = ?1";
            var query = em.createQuery(jpaQuery, Automobile.class);
            query.setParameter(1, vin);
            try{
            Automobile auto = query.getSingleResult();
            System.out.printf("Automobile With VIN: %s: %s\n\n", vin, auto);
            }catch (Exception e){
                System.out.println("No Such Automobile\n");
            }
            return;
        }

        System.out.println("Enter Trim Name: ");
        String trim = scan.nextLine();
        if (trim.length() != 0){
            jpaQuery += "WHERE auto.trim.name = ?1";
            String result = getAutoResultString(jpaQuery, trim);
            System.out.printf("Automobile(s) With Trim Name %s:\n%s\n\n", trim, result);
            return;
        }

        System.out.println("Enter Model Name: ");
        String model = scan.nextLine();
        if (model.length() != 0){
            jpaQuery += "WHERE auto.model.name = ?1";
            String result = getAutoResultString(jpaQuery, model);
            System.out.printf("Automobile(s) With Model Name %s:\n%s\n\n", model, result);
        }


    }


    private static void findAutomobilePackages() {
        String jpaQuery = "SELECT auto FROM automobiles auto WHERE auto.vin = ?1";
        scan.nextLine();

        System.out.println("Enter VIN: ");
        String vin = scan.nextLine();
        var query = em.createQuery(jpaQuery, Automobile.class);
        query.setParameter(1, vin);

        try{

            var packages = query.getSingleResult().getChosenPackages();
            StringBuilder result = new StringBuilder();
            if (packages.size() == 0){throw new NoResultException();}
            for (AvailablePackage aPackage: packages){
                result.append(aPackage.getaPackage()).append("\n");
            }
            System.out.printf("Packages added to VIN %s: \n%s\n\n", vin, result.toString());

        }catch (NoResultException e) {
            System.out.printf("No Packages Found For VIN: %s%n\n\n", vin);
        }

    }

    private static void findAutomobileFeatures() {
        String jpaQuery = "SELECT auto FROM automobiles auto WHERE auto.vin = ?1";
        scan.nextLine();

        System.out.println("Enter VIN: ");
        String vin = scan.nextLine();
        var query = em.createQuery(jpaQuery, Automobile.class);
        query.setParameter(1, vin);

        try{
            Automobile auto = query.getSingleResult();
            var features = auto.getTrim().getFeatures();
            features.addAll(auto.getTrim().getModel().getFeatures());
            for (AvailablePackage aPack : auto.getChosenPackages()){
                features.addAll(aPack.getaPackage().getFeatures());
            }
            StringBuilder result = new StringBuilder();
            if (features.size() == 0){throw new NoResultException();}
            for (Feature feature: features){
                result.append(feature).append("\n");
            }
            System.out.printf("Features included with VIN %s: \n%s\n\n", vin, result.toString());
        }catch (NoResultException e) {
            System.out.printf("No Features Found For VIN: %s\n\n", vin);
        }

    }

    private static void findModel() {
        String jpaQuery = "SELECT m FROM models m ";
        scan.nextLine();
        System.out.println("Enter Model Name: ");
        String modelName = scan.nextLine();
        if (modelName.length() != 0){
            jpaQuery += "WHERE m.name = ?1";
            try {
                var models = em.createQuery(jpaQuery, Model.class).setParameter(1, modelName).getResultList();
                StringBuilder result = new StringBuilder();
                if (models.size() == 0){throw new NoResultException();}
                for (Model model: models){
                    result.append(model).append("\n");
                }
                System.out.printf("Model(s) with name %s:\n%s\n\n", modelName, result.toString());
                return;
            }catch(NoResultException e){
                System.out.println("No Such Model\n");
                return;
            }
        }

        System.out.println("Enter Model Year: ");
        int input = validateIntInput();
        if (input < 1884){
            System.out.println("Invalid Year\n");
            return;
        }
        jpaQuery += "WHERE m.year = ?1";
        try {
            var models = em.createQuery(jpaQuery, Model.class).setParameter(1, input).getResultList();
            StringBuilder result = new StringBuilder();
            if (models.size() == 0){throw new NoResultException();}
            for (Model model: models){
                result.append(model).append("\n");
                return;
            }
            System.out.printf("Model(s) with year %s:\n%s\n\n", input, result.toString());
        }catch(NoResultException e){
            System.out.println("No Such Model\n");
        }
    }

    private static void findModelFeatures() {
        scan.nextLine();
        String jpaQuery = "SELECT m FROM models m WHERE m.name = ?1 AND m.year = ?2";
        var query = em.createQuery(jpaQuery, Model.class);


        System.out.println("Enter Model Name: ");
        String modelName = scan.nextLine();
        if (modelName.length() == 0){
            return;
        }
        query.setParameter(1, modelName);

        System.out.println("Enter Year: ");
        int year = validateIntInput();
        query.setParameter(2, year);

        Model model = query.getSingleResult();
        var features = model.getFeatures();

        StringBuilder result = new StringBuilder();
        if (features.size() == 0){
            System.out.println("Model Has No Features");
            return;
        }
        for (Feature feature: features){
            result.append(feature).append("\n");
        }
        System.out.printf("Features included with model %d %s: \n%s\n\n", year, modelName, result.toString());


    }

    private static void findTrim() {
        

    }

    private static void findTrimFeatures() {

    }

    private static void findTrimPackages() {

    }

    private static void findPackage() {

    }

    private static void findPackageFeatures() {
    }

    private static void findPackageTrims() {
    }

    public static int validateIntInput(){
        int input = -1;

        if (scan.hasNextInt()){
            input = scan.nextInt();
        }
        else{
            scan.nextLine();
        }

        return input;
    }

    public static float validateFloatInput(){
        float input = -1f;

        if (scan.hasNextFloat()){
            input = scan.nextFloat();
        }
        else{
            scan.nextLine();
        }

        return input;
    }

    private static String getAutoResultString(String jpaQuery, String searchParam) {
        var query = em.createQuery(jpaQuery, Automobile.class);
        query.setParameter(1, searchParam);
        var autos = query.getResultList();
        StringBuilder result = new StringBuilder();
        for (Automobile auto: autos){
            result.append(auto).append("\n");
        }
        return result.toString();
    }

}