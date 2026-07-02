import java.util.*;
import java.util.stream.Collectors;

abstract class Cargo {

    String containerId;
    double valueInCredits;
    boolean isHazardous;

    Cargo(String id, double value, boolean hazard) {
        containerId = id;
        valueInCredits = value;
        isHazardous = hazard;
    }
}

class StandardCargo extends Cargo {

    StandardCargo(String id, double value, boolean hazard) {
        super(id, value, hazard);
    }
}

class BiologicalCargo extends Cargo {

    boolean isShielded;

    BiologicalCargo(String id, double value, boolean hazard, boolean shield) {
        super(id, value, hazard);
        isShielded = shield;
    }
}

@FunctionalInterface
interface CargoInspector {
    boolean check(Cargo c);
}

@FunctionalInterface
interface CargoCompressor {
    String compress(Cargo c);
}

class ManifestProcessor {

    public List<String> processManifest(List<Cargo> manifest,
                                        CargoInspector inspector,
                                        CargoCompressor compressor) {

        return manifest.stream()
                .filter(inspector::check)
                .filter(c -> c.valueInCredits >= 1000)
                .map(compressor::compress)
                .collect(Collectors.toList());
    }
}

public class Q1 {

    public static void main(String[] args) {

        List<Cargo> list = new ArrayList<>();

        list.add(new StandardCargo("ALPHA-99", 5000.5, false));
        list.add(new BiologicalCargo("BETA-12", 3000, true, false));
        list.add(new BiologicalCargo("GAMA-55", 6000, true, true));
        list.add(new StandardCargo("OMEGA-77", 500, false));

        CargoInspector inspector = c -> {

            if (c.isHazardous && c instanceof BiologicalCargo) {
                BiologicalCargo b = (BiologicalCargo) c;

                if (!b.isShielded) {
                    return false;
                }
            }

            return true;
        };

        CargoCompressor compressor =
                c -> c.containerId.substring(0, 4) + "-" + (int) c.valueInCredits;

        ManifestProcessor p = new ManifestProcessor();

        List<String> result = p.processManifest(list, inspector, compressor);

        for (String s : result) {
            System.out.println(s);
        }
    }
}