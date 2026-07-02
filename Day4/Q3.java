import java.util.*;

abstract class EngineLog {

    String timestamp;
    double coreTemperature;
    boolean isAnomaly;

    EngineLog(String time, double temp, boolean anomaly) {
        timestamp = time;
        coreTemperature = temp;
        isAnomaly = anomaly;
    }
}

class NominalLog extends EngineLog {

    NominalLog(String time, double temp, boolean anomaly) {
        super(time, temp, anomaly);
    }
}

class CriticalLog extends EngineLog {

    String errorCode;

    CriticalLog(String time, double temp, boolean anomaly, String code) {
        super(time, temp, anomaly);
        errorCode = code;
    }
}

@FunctionalInterface
interface LogAuditor {
    boolean check(EngineLog log);
}

@FunctionalInterface
interface HeatExtractor {
    double getTemp(EngineLog log);
}

class TelemetryProcessor {

    public double getPeakCriticalTemp(List<EngineLog> logs,
                                      LogAuditor auditor,
                                      HeatExtractor extractor) {

        return logs.stream()
                .filter(auditor::check)
                .mapToDouble(extractor::getTemp)
                .max()
                .orElse(0.0);
    }
}

public class Q3 {

    public static void main(String[] args) {

        List<EngineLog> list = new ArrayList<>();

        list.add(new NominalLog("10:00", 500, false));
        list.add(new CriticalLog("10:05", 900, false, "OVERHEAT"));
        list.add(new CriticalLog("10:10", 850, true, "ERROR"));
        list.add(new NominalLog("10:15", 600, false));

        LogAuditor auditor = log -> {

            if (log.isAnomaly) {
                return true;
            }

            if (log instanceof CriticalLog) {
                CriticalLog c = (CriticalLog) log;

                if (c.errorCode.equals("OVERHEAT")) {
                    return true;
                }
            }

            return false;
        };

        HeatExtractor extractor = log -> log.coreTemperature;

        TelemetryProcessor t = new TelemetryProcessor();

        double max = t.getPeakCriticalTemp(list, auditor, extractor);

        System.out.println("Highest Temperature : " + max);
    }
}