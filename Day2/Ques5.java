abstract class DataPayload {

    abstract String getRawContent();
}

class JsonPayload extends DataPayload {

    String data;

    JsonPayload(String data) {
        this.data = data;
    }

    String getRawContent() {
        return data;
    }
}

class XmlPayload extends DataPayload {

    String data;

    XmlPayload(String data) {
        this.data = data;
    }

    String getRawContent() {
        return data;
    }
}

class PipelineProcessor<T extends DataPayload> {

    void process(T payload) {
        System.out.println(payload.getRawContent());
    }
}

public class Ques5 {

    public static void main(String[] args) {

        PipelineProcessor<JsonPayload> p1 = new PipelineProcessor<>();
        p1.process(new JsonPayload("{name:'John'}"));

        PipelineProcessor<XmlPayload> p2 = new PipelineProcessor<>();
        p2.process(new XmlPayload("<name>John</name>"));
    }
}