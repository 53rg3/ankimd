package english.models;

import java.util.ArrayList;
import java.util.List;

public class MerriamDefinition {

    public String definition;
    public List<String> examples = new ArrayList<>();

    public boolean isValid() {
        return this.definition != null;
    }

}
