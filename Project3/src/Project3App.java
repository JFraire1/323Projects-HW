import DealershipModel.*;
import DealershipModel.Package;
import jakarta.persistence.*;
import java.text.DecimalFormat;
import java.util.*;

public class Project3App {
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dealershipDB");
    private static final EntityManager em = entityManagerFactory.createEntityManager();
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        int input = -1;

        while(input != 0){
            System.out.println("""
                    1 Initiate Model
                    2 Automobile Lookup
                    3 Feature Lookup
                    0 Exit            
                    """);

            input = validateIntInput();
            switch (input) {
                case 1 -> initiateModel();
                case 2 -> autoLookUp();
                case 3 -> featureLookUp();
                case 0 -> System.out.println("\nHave a good day.\n");
                default -> System.out.println("No Such Option\n");
            }
        }

    }

    public static void autoLookUp(){
        Automobile auto;
        try{
            auto = getAutomobile();
        }catch (NoResultException e){
            return;
        }

        StringBuilder features = new StringBuilder();
        features.append("Features:\n");
        for (Feature feature: getAutoFeatures(auto)){
            features.append(feature.getName()).append("\n");
        }
        String trim  = auto.getTrim().getName();
        String model = auto.getTrim().getModel().getName();
        int year = auto.getTrim().getModel().getYear();

        System.out.printf("%d %s %s\n$ %s\n%s\n\n", year, trim, model, df.format(getStickerPrice(auto)), features.toString());
    }

    public static Automobile getAutomobile() throws NoResultException{
        scan.nextLine();
        System.out.println("Enter VIN:");
        String vin = scan.nextLine();
        vin = vin.replaceAll("[^a-zA-Z0-9]", " ");
        /* This is if we're using real 17-digit vins
        if (vin.length() != 17){
            System.out.println("Invalid VIN\n");
            throw new NoResultException();
        }
        */
        try {

            var query = em.createQuery("SELECT auto FROM automobiles auto WHERE auto.vin = ?1", Automobile.class).setParameter(1, vin);
            return query.getSingleResult();

        }catch (NoResultException e){

            System.out.println("No Such Automobile\n");
            throw e;

        }
    }

    public static Set<Feature> getAutoFeatures(Automobile auto){
        var features = new TreeSet<Feature>();
        features.addAll(auto.getTrim().getFeatures());
        features.addAll(auto.getTrim().getModel().getFeatures());
        for (AvailablePackage aPack : auto.getChosenPackages()){
            features.addAll(aPack.getaPackage().getFeatures());
        }
        return features;
    }

    public static float getStickerPrice(Automobile auto){
        float stickerPrice = 0.0f;
        stickerPrice += auto.getTrim().getCost();
        for (AvailablePackage aPackage: auto.getChosenPackages()){
            stickerPrice += aPackage.getCost();
        }
        return stickerPrice;
    }

    public static void featureLookUp(){
        Feature feature;
        try {
            feature = getFeature();
        }catch(NoResultException e){
            return;
        }

        StringBuilder autos = new StringBuilder();
        autos.append(String.format("Automobiles With %s:\n", feature.getName()));
        for (Automobile auto: getAutosWithFeature(feature)){
            autos.append(auto.getVin()).append("\n");
        }

        System.out.printf("%s\n\n", autos);
    }

    public static Feature getFeature() throws NoResultException{
        scan.nextLine();
        System.out.println("Enter Name Of Feature:");
        String name = scan.nextLine();

        try {

            var query = em.createQuery("SELECT feature FROM features feature WHERE feature.name = ?1", Feature.class).setParameter(1, name);
            return query.getSingleResult();

        }catch (NoResultException e){

            System.out.println("No Such Feature\n");
            throw e;

        }
    }

    public static Set<Automobile> getAutosWithFeature(Feature feature){
        String jpaquery = ("Select a FROM automobiles a LEFT JOIN a.trim t LEFT JOIN t.features tf " +
                "LEFT JOIN a.trim.model m LEFT JOIN m.features mf " +
                "LEFT JOIN a.chosenPackages chosenPack LEFT JOIN chosenPack.aPackage aPackage LEFT JOIN aPackage.features pf " +
                "WHERE tf.name = ?1 OR mf.name = ?1 OR pf.name = ?1");

        return new HashSet<>(em.createQuery(jpaquery, Automobile.class).setParameter(1, feature.getName()).getResultList());
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

    public static void initiateModel(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("dealershipDB");
        EntityManager em = factory.createEntityManager();
        System.out.println("Generating Models:");
        //Begin 
        em.getTransaction().begin();

        Model pacifica22 = new Model();
        pacifica22.setName("Pacifica");
        pacifica22.setYear(2022);
        em.persist(pacifica22);

        Model pacificaHybrid22 = new Model();
        pacificaHybrid22.setName("Pacifica Hybrid");
        pacificaHybrid22.setYear(2022);
        em.persist(pacificaHybrid22);

        Model pacificaHybrid21 = new Model();
        pacificaHybrid21.setName("Pacifica Hybrid");
        pacificaHybrid21.setYear(2021);
        em.persist(pacificaHybrid21);

        em.getTransaction().commit();


        em.getTransaction().begin();
        System.out.println("Generating Trims:");
        // Trims Pacifica 2022 
        Trim p22Touring = new Trim();
        p22Touring.setName("Touring");
        p22Touring.setCost(30000);
        p22Touring.setModel(pacifica22);
        em.persist(p22Touring);
        Trim p22Limited = new Trim();
        p22Limited.setName("Limited");
        p22Limited.setCost(34000);
        p22Limited.setModel(pacifica22);
        em.persist(p22Limited);
        Trim p22Pinnacle = new Trim();
        p22Pinnacle.setName("Pinnacle");
        p22Pinnacle.setCost(42000);
        p22Pinnacle.setModel(pacifica22);
        em.persist(p22Pinnacle);

        // Trims for Pacifica Hybrid 2022 
        Trim ph22Touring = new Trim();
        ph22Touring.setName("Touring");
        ph22Touring.setCost(43000);
        ph22Touring.setModel(pacificaHybrid22);
        em.persist(ph22Touring);
        Trim ph22Limited = new Trim();
        ph22Limited.setName("Limited");
        ph22Limited.setCost(48000);
        ph22Limited.setModel(pacificaHybrid22);
        em.persist(ph22Limited);
        Trim ph22Pinnacle = new Trim();
        ph22Pinnacle.setName("Pinnacle");
        ph22Pinnacle.setCost(54000);
        ph22Pinnacle.setModel(pacificaHybrid22);
        em.persist(ph22Pinnacle);

        // Trims for Model 3: Pacifica Hybrid 2021  
        Trim ph21Touring = new Trim();
        ph21Touring.setName("Touring");
        ph21Touring.setCost(41000);
        ph21Touring.setModel(pacificaHybrid21);
        em.persist(ph21Touring);
        Trim ph21Limited = new Trim();
        ph21Limited.setName("Limited");
        ph21Limited.setCost(46000);
        ph21Limited.setModel(pacificaHybrid21);
        em.persist(ph21Limited);
        Trim ph21Pinnacle = new Trim();
        ph21Pinnacle.setName("Pinnacle");
        ph21Pinnacle.setCost(52000);
        ph21Pinnacle.setModel(pacificaHybrid21);
        em.persist(ph21Pinnacle);

        em.getTransaction().commit();

        em.getTransaction().begin();

        System.out.println("Generating Features:");
        Feature leatherSeats = new Feature();
        leatherSeats.setName("Leather Seats");
        em.persist(leatherSeats);
        Feature plugInHybridEngine = new Feature();
        plugInHybridEngine.setName("Plug-In Hybrid Engine");
        em.persist(plugInHybridEngine);
        Feature powerSlideDoor = new Feature();
        powerSlideDoor.setName("Power Sliding Doors");
        em.persist(powerSlideDoor);
        Feature handsFreeSlidingDoor = new Feature();
        handsFreeSlidingDoor.setName("Hands-Free Sliding Doors");
        em.persist(handsFreeSlidingDoor);
        Feature amazonFireTV = new Feature();
        amazonFireTV.setName("Amazon FireTV");
        em.persist(amazonFireTV);
        Feature rearSeatScreens = new Feature();
        rearSeatScreens.setName("Rear-Seat Entertainment Screens");
        em.persist(rearSeatScreens);
        Feature AWD = new Feature();
        AWD.setName("All-Wheel Drive");
        em.persist(AWD);
        Feature adaptiveCruiseControl = new Feature();
        adaptiveCruiseControl.setName("Adaptive Cruise Control");
        em.persist(adaptiveCruiseControl);

        em.getTransaction().commit();

        em.getTransaction().begin();

        System.out.println("Generating Packages:");
        Package theaterPack = new Package();
        theaterPack.setName("Theater Package");
        em.persist(theaterPack);
        Package amazonPack = new Package();
        amazonPack.setName("Amazon Theater Package");
        em.persist(amazonPack);
        Package safetyPack = new Package();
        safetyPack.setName("Safety Package");
        em.persist(safetyPack);

        em.getTransaction().commit();

        em.getTransaction().begin();

        System.out.println("Generating Automobiles:");
        // Automobile 1: Pacifica 2022 
        Automobile auto1 = new Automobile();
        auto1.setVin("12345abcde");
        auto1.setTrim(p22Limited);
        em.persist(auto1);
        // Automobile 2: Pacifica Hybrid 2022 no pack
        Automobile auto2 = new Automobile();
        auto2.setVin("67890abcde");
        auto2.setTrim(ph22Pinnacle);
        em.persist(auto2);
        // Automobile 3: Pacifica Hybrid 2021 no pack
        Automobile auto3 = new Automobile();
        auto3.setVin("99999aaaaa");
        auto3.setTrim(ph21Pinnacle);
        em.persist(auto3);
        // Automobile 4: Pacifica Hybrid 2021 safety pack
        Automobile auto4 = new Automobile();
        auto4.setVin("aaaaa88888");
        auto4.setTrim(ph21Touring);
        em.persist(auto4);
        // Automobile 5: Pacifica Hybrid 2021 safety and theater pack
        Automobile auto5 = new Automobile();
        auto5.setVin("bbbbb77777");
        auto5.setTrim(ph21Limited);
        em.persist(auto5);
        
        em.getTransaction().commit();

        em.getTransaction().begin();

        System.out.println("Adding Features to Packages:");
        // Adding features to Packages
        theaterPack.getFeatures().add(rearSeatScreens);
        em.persist(theaterPack);

        amazonPack.getFeatures().add(rearSeatScreens);
        amazonPack.getFeatures().add(amazonFireTV);
        em.persist(amazonPack);

        safetyPack.getFeatures().add(adaptiveCruiseControl);
        em.persist(safetyPack);

        //Committing will finish/push the change
        em.getTransaction().commit();

        em.getTransaction().begin();
        System.out.println("Adding Features to Models:");
        // Adding features to Models
        pacifica22.getFeatures().add(powerSlideDoor);
        em.persist(pacifica22);

        pacificaHybrid22.getFeatures().add(powerSlideDoor);
        pacificaHybrid22.getFeatures().add(plugInHybridEngine);
        em.persist(pacificaHybrid22);

        pacificaHybrid21.getFeatures().add(powerSlideDoor);
        pacificaHybrid21.getFeatures().add(plugInHybridEngine);
        em.persist(pacificaHybrid21);

        em.getTransaction().commit();

        em.getTransaction().begin();
        System.out.println("Adding Features to Trims:");
        // Adding features to Trims
        p22Limited.getFeatures().add(leatherSeats);
        p22Limited.getFeatures().add(handsFreeSlidingDoor);
        em.persist(p22Limited);

        p22Pinnacle.getFeatures().add(leatherSeats);
        p22Pinnacle.getFeatures().add(handsFreeSlidingDoor);
        p22Pinnacle.getFeatures().add(rearSeatScreens);
        p22Pinnacle.getFeatures().add(amazonFireTV);
        p22Pinnacle.getFeatures().add(AWD);
        em.persist(p22Pinnacle);

        ph22Limited.getFeatures().add(leatherSeats);
        ph22Limited.getFeatures().add(handsFreeSlidingDoor);
        em.persist(ph22Limited);

        ph22Pinnacle.getFeatures().add(leatherSeats);
        ph22Pinnacle.getFeatures().add(handsFreeSlidingDoor);
        ph22Pinnacle.getFeatures().add(rearSeatScreens);
        ph22Pinnacle.getFeatures().add(amazonFireTV);
        em.persist(ph22Pinnacle);

        ph21Limited.getFeatures().add(leatherSeats);
        ph21Limited.getFeatures().add(handsFreeSlidingDoor);
        em.persist(ph21Limited);

        ph21Pinnacle.getFeatures().add(leatherSeats);
        ph21Pinnacle.getFeatures().add(handsFreeSlidingDoor);
        ph21Pinnacle.getFeatures().add(rearSeatScreens);
        ph21Pinnacle.getFeatures().add(adaptiveCruiseControl);
        em.persist(ph21Pinnacle);

        em.getTransaction().commit();

        em.getTransaction().begin();

        System.out.println("Generating Available Packages:");

        AvailablePackage p22TouringAvailablePack = new AvailablePackage();
        p22TouringAvailablePack.setTrim(p22Touring);
        p22TouringAvailablePack.setaPackage(safetyPack);
        p22TouringAvailablePack.setCost(3000);
        em.persist(p22TouringAvailablePack);

        AvailablePackage p22LimitedAvailablePack = new AvailablePackage();
        p22LimitedAvailablePack.setTrim(p22Limited);
        p22LimitedAvailablePack.setaPackage(amazonPack);
        p22LimitedAvailablePack.setCost(2500);
        em.persist(p22LimitedAvailablePack);

        // p22Pinnacle has no compatible packages
        // ph22Touring has no compatible packages

        AvailablePackage ph22LimitedAvailablePack = new AvailablePackage();
        ph22LimitedAvailablePack.setTrim(ph22Limited);
        ph22LimitedAvailablePack.setaPackage(amazonPack);
        ph22LimitedAvailablePack.setCost(2500);
        em.persist(ph22LimitedAvailablePack);

        // ph22Pinnacle has no compatible packages

        AvailablePackage ph21TouringAvailablePack = new AvailablePackage();
        ph21TouringAvailablePack.setTrim(ph21Touring);
        ph21TouringAvailablePack.setaPackage(safetyPack);
        ph21TouringAvailablePack.setCost(3000);
        em.persist(ph21TouringAvailablePack);

        AvailablePackage ph21LimitedAvailablePack1 = new AvailablePackage();
        ph21LimitedAvailablePack1.setTrim(ph21Limited);
        ph21LimitedAvailablePack1.setaPackage(theaterPack);
        ph21LimitedAvailablePack1.setCost(2500);
        em.persist(ph21LimitedAvailablePack1);

        AvailablePackage ph21LimitedAvailablePack2 = new AvailablePackage();
        ph21LimitedAvailablePack2.setTrim(ph21Limited);
        ph21LimitedAvailablePack2.setaPackage(safetyPack);
        ph21LimitedAvailablePack2.setCost(2000);
        em.persist(ph21LimitedAvailablePack2);

        // ph21Pinnacle has no capatible packages
        em.getTransaction().commit();

        em.getTransaction().begin();

        System.out.println("Adding Available Packages to Automobiles:\n");

        auto1.getChosenPackages().add(p22LimitedAvailablePack);
        p22LimitedAvailablePack.getAddedAutos().add(auto1);
        em.persist(auto1);

        // auto 2 has no packages chosen
        // auto 3 has no packages chosen

        auto4.getChosenPackages().add(ph21TouringAvailablePack);
        ph21TouringAvailablePack.getAddedAutos().add(auto4);
        em.persist(auto4);

        auto5.getChosenPackages().add(ph21LimitedAvailablePack1);
        auto5.getChosenPackages().add(ph21LimitedAvailablePack2);
        em.persist(auto5);
        
        em.getTransaction().commit();

    }
}

