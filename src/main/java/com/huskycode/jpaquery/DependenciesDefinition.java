package com.huskycode.jpaquery;

import com.huskycode.jpaquery.link.Link;

/**
 * Define dependencies between entity class
 */
public class DependenciesDefinition {
    private Link[] links;

    private DependenciesDefinition() {
    }

    public static DependenciesDefinition fromLinks(Link[] links) {
        DependenciesDefinition deps = new DependenciesDefinition();
        deps.links = links;
        return deps;
    }

    public Link[] getLinks() {
        return links;
    }
}
