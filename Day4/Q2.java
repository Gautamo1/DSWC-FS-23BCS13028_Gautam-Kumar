import java.util.*;
import java.util.stream.Collectors;

abstract class MemoryEngram {

    String engramId;
    double clarityScore;
    boolean isCorrupted;

    MemoryEngram(String id, double score, boolean corrupted) {
        engramId = id;
        clarityScore = score;
        isCorrupted = corrupted;
    }
}

class StandardEngram extends MemoryEngram {

    StandardEngram(String id, double score, boolean corrupted) {
        super(id, score, corrupted);
    }
}

class ClassifiedEngram extends MemoryEngram {

    int securityClearanceLevel;

    ClassifiedEngram(String id, double score, boolean corrupted, int level) {
        super(id, score, corrupted);
        securityClearanceLevel = level;
    }
}

@FunctionalInterface
interface EngramValidator {
    boolean check(MemoryEngram m);
}

@FunctionalInterface
interface EngramTranslator {
    String translate(MemoryEngram m);
}

class CortexProcessor {

    public List<String> processMemories(List<MemoryEngram> engrams,
                                        EngramValidator validator,
                                        EngramTranslator translator) {

        return engrams.stream()
                .filter(validator::check)
                .filter(m -> m.clarityScore >= 50)
                .map(translator::translate)
                .collect(Collectors.toList());
    }
}

public class Q2 {

    public static void main(String[] args) {

        List<MemoryEngram> list = new ArrayList<>();

        list.add(new StandardEngram("001", 90, false));
        list.add(new StandardEngram("002", 40, false));
        list.add(new ClassifiedEngram("003", 80, false, 5));
        list.add(new ClassifiedEngram("004", 75, false, 2));
        list.add(new StandardEngram("005", 85, true));

        EngramValidator validator = m -> {

            if (m.isCorrupted) {
                return false;
            }

            if (m instanceof ClassifiedEngram) {
                ClassifiedEngram c = (ClassifiedEngram) m;

                if (c.securityClearanceLevel > 3) {
                    return false;
                }
            }

            return true;
        };

        EngramTranslator translator =
                m -> "ENGRAM-" + m.engramId + " | CLARITY: " + (int)m.clarityScore + "%";

        CortexProcessor p = new CortexProcessor();

        List<String> result = p.processMemories(list, validator, translator);

        for (String s : result) {
            System.out.println(s);
        }
    }
}