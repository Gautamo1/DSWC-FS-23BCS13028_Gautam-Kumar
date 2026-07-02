import java.util.*;
import java.util.stream.Collectors;

abstract class DNASample {

    String sampleId;
    double purityPercentage;

    DNASample(String id, double purity) {
        sampleId = id;
        purityPercentage = purity;
    }
}

class HumanSample extends DNASample {

    String bloodType;

    HumanSample(String id, double purity, String bloodType) {
        super(id, purity);
        this.bloodType = bloodType;
    }
}

class AlienSample extends DNASample {

    boolean isSiliconBased;

    AlienSample(String id, double purity, boolean silicon) {
        super(id, purity);
        isSiliconBased = silicon;
    }
}

@FunctionalInterface
interface ViabilityChecker {
    boolean check(DNASample s);
}

@FunctionalInterface
interface GenomeMapper {
    String map(DNASample s);
}

class Sequencer {

    public Map<String, List<String>> classifyGenomes(List<DNASample> samples,
                                                     ViabilityChecker checker,
                                                     GenomeMapper mapper) {

        return samples.stream()
                .filter(checker::check)
                .collect(Collectors.groupingBy(
                        s -> s.getClass().getSimpleName(),
                        Collectors.mapping(mapper::map, Collectors.toList())
                ));
    }
}

public class Q5 {

    public static void main(String[] args) {

        List<DNASample> list = new ArrayList<>();

        list.add(new HumanSample("001", 90, "O+"));
        list.add(new HumanSample("002", 70, "A+"));
        list.add(new AlienSample("003", 95, true));
        list.add(new AlienSample("004", 85, false));

        ViabilityChecker checker = s -> s.purityPercentage >= 80;

        GenomeMapper mapper = s -> {

            if (s instanceof HumanSample) {
                HumanSample h = (HumanSample) s;
                return "ID: " + h.sampleId + " (Type: " + h.bloodType + ")";
            }

            AlienSample a = (AlienSample) s;
            return "ID: " + a.sampleId + " (Silicon: " + a.isSiliconBased + ")";
        };

        Sequencer seq = new Sequencer();

        Map<String, List<String>> result =
                seq.classifyGenomes(list, checker, mapper);

        System.out.println(result);
    }
}