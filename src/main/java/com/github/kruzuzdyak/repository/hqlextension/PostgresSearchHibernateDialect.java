package com.github.kruzuzdyak.repository.hqlextension;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgresPlusDialect;
import org.hibernate.type.StandardBasicTypes;

public class PostgresSearchHibernateDialect extends PostgresPlusDialect {

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        functionContributions.getFunctionRegistry()
                .registerPattern(
                        "fts",
                        "?1 @@ websearch_to_wildcard_tsquery(?2)",
                        functionContributions.getTypeConfiguration().getBasicTypeRegistry()
                                .resolve(StandardBasicTypes.BOOLEAN)
                );
    }
}
