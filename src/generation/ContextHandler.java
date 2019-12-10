package generation;

import java.util.Objects;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import util.MyLogger;

public class ContextHandler {
    private final String id;
    private final DataSpecification dataContainer;
	private final MyLogger logger;

    public ContextHandler(final String id, final DataSpecification dataSpec, MyLogger logger) {
        this.id = Objects.requireNonNull(id);
        this.dataContainer = dataSpec;
        this.logger = logger;
    }

    public void createPolicySet() throws IllegalStateException {
        this.dataContainer.getRelatedCharacteristics().stream().forEach(e -> {
        	logger.info(e.toString());
        });
    }
}
