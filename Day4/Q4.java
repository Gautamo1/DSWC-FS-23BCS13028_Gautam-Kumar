import java.util.*;
import java.util.stream.Collectors;

abstract class TemporalEntity {

    String entityName;
    int originYear;

    TemporalEntity(String name, int year) {
        entityName = name;
        originYear = year;
    }
}

class HumanEntity extends TemporalEntity {

    HumanEntity(String name, int year) {
        super(name, year);
    }
}

class ArtifactEntity extends TemporalEntity {

    boolean isRadioactive;

    ArtifactEntity(String name, int year, boolean radio) {
        super(name, year);
        isRadioactive = radio;
    }
}

class HistoricalEvent {

    List<TemporalEntity> entities;
    int eventYear;

    HistoricalEvent(int year, List<TemporalEntity> entities) {
        this.eventYear = year;
        this.entities = entities;
    }
}

@FunctionalInterface
interface ParadoxChecker {
    boolean check(TemporalEntity e, int year);
}

@FunctionalInterface
interface ThreatMapper {
    String map(TemporalEntity e);
}

class Item {

    TemporalEntity entity;
    int year;

    Item(TemporalEntity entity, int year) {
        this.entity = entity;
        this.year = year;
    }
}

class ParadoxAnalyzer {

    public List<String> detectParadoxes(List<HistoricalEvent> timeline,
                                        ParadoxChecker checker,
                                        ThreatMapper mapper) {

        return timeline.stream()
                .flatMap(h -> h.entities.stream()
                        .map(e -> new Item(e, h.eventYear)))
                .filter(x -> checker.check(x.entity, x.year))
                .map(x -> mapper.map(x.entity))
                .collect(Collectors.toList());
    }
}

public class Q4 {

    public static void main(String[] args) {

        List<HistoricalEvent> list = new ArrayList<>();

        List<TemporalEntity> e1 = new ArrayList<>();
        e1.add(new HumanEntity("John", 2050));
        e1.add(new ArtifactEntity("Stone", 1800, false));

        List<TemporalEntity> e2 = new ArrayList<>();
        e2.add(new HumanEntity("Alice", 1990));
        e2.add(new ArtifactEntity("Gun", 2100, true));

        list.add(new HistoricalEvent(2000, e1));
        list.add(new HistoricalEvent(2020, e2));

        ParadoxChecker checker = (e, year) -> e.originYear > year;

        ThreatMapper mapper = e -> e.entityName + " detected out of time!";

        ParadoxAnalyzer p = new ParadoxAnalyzer();

        List<String> result = p.detectParadoxes(list, checker, mapper);

        for (String s : result) {
            System.out.println(s);
        }
    }
}