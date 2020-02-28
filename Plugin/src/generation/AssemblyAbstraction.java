package generation;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.system.System;

/**
 * Abstraction for handling related to Assembly/System
 * 
 * @author Thomas Lieb
 *
 */
public class AssemblyAbstraction {
    private final System system;

    /**
     * Constructor
     * 
     * @param system
     */
    public AssemblyAbstraction(final System system) {
        this.system = system;
    }

    public boolean isOperationProvidedRoleMatch(OperationProvidedRole target, OperationProvidedRole opr) {
        // return target == opr;
        // Direct compare doesn't always seem to work, e.g. in testcases,
        // seems to be related to how data is loaded (path to file)

        return target.getId().equalsIgnoreCase(opr.getId());
    }

    public EList<ProvidedDelegationConnector> getListOfProvidedDelegationConnector() {
        EList<ProvidedDelegationConnector> list = new BasicEList<>();
        for (Connector connector : system.getConnectors__ComposedStructure()) {
            if (connector instanceof ProvidedDelegationConnector) {
                list.add((ProvidedDelegationConnector) connector);
            }
        }
        return list;
    }

    public EList<AssemblyConnector> getListOfAssemblyConnectors() {
        EList<AssemblyConnector> list = new BasicEList<>();
        for (Connector connector : system.getConnectors__ComposedStructure()) {
            if (connector instanceof AssemblyConnector) {
                list.add((AssemblyConnector) connector);
            }
        }
        return list;
    }
}
