import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
class ImageRenderingEngine {

    ImageRenderingEngine() {
        System.out.println("Rendering Engine Created");
    }

    public void renderImage() {
        System.out.println("Rendering MRI Image");
    }
}

@Component
@Scope("prototype")
class PatientContext {

    PatientContext() {
        System.out.println("New Patient Context");
    }

    public void startScan() {
        System.out.println("Patient Scan Started");
    }
}

@Component
class ScanOrchestrator {

    private final ImageRenderingEngine engine;
    private final ObjectProvider<PatientContext> provider;

    ScanOrchestrator(ImageRenderingEngine engine,
                     ObjectProvider<PatientContext> provider) {

        this.engine = engine;
        this.provider = provider;
    }

    public void processScan() {

        PatientContext patient = provider.getObject();

        patient.startScan();

        engine.renderImage();
    }
}