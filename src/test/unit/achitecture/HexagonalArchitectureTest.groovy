package achitecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures
import spock.lang.Specification

class HexagonalArchitectureTest extends Specification {

    static final JavaClasses classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages(
            'com.sportphotos.domain', 'com.sportphotos.interfaces', 'com.sportphotos.infrastructure')

    def 'infrastructure and interfaces should be separated from domains'() {
        given: 'application layers'
            def domainLayer = 'domain'
            def interfacesLayer = 'interfaces'
            def infrastructureLayer = 'infrastructure'
        and: 'layer packages'
            def domainPackages = ['..com.sportphotos.domain..'] as String[]
            def interfacesPackages = ['..com.sportphotos.interfaces..'] as String[]
            def infrastructurePackages = ['..com.sportphotos.infrastructure..'] as String[]
        expect:
            Architectures.layeredArchitecture()
                .layer(domainLayer).definedBy(domainPackages)
                .layer(interfacesLayer).definedBy(interfacesPackages)
                .layer(infrastructureLayer).definedBy(infrastructurePackages)
                .whereLayer(domainLayer).mayOnlyBeAccessedByLayers(interfacesLayer, infrastructureLayer)
                .whereLayer(interfacesLayer).mayNotBeAccessedByAnyLayer()
                .whereLayer(infrastructureLayer).mayOnlyBeAccessedByLayers(interfacesLayer)
                .check(classes)
    }
}
