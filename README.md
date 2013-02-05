JPAQuery
========
Create a database hierarchy using simple command.

[![Build Status](https://secure.travis-ci.org/varokas/jpaQuery.png)](http://travis-ci.org/varokas/jpaQuery)

    [How to use it]
    
    import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
	import static com.huskycode.jpaquery.command.CommandNodesFactory.ns;
    
    @Autowired EntityManager entityManager;
    
    DependenciesDefinition definition = new DepsBuilder()
						    			.withLink(Link.from(Descendant.class, Descendant_.parentId)
						    						.to(Parent.class, Parent_.parentId))
						    					...
						    			.withLink(Link.from(Parent.class, Parent_.ancestorId)
						    						.to(Ancestor.class, Ancestor_.ancestorId))	
						    					...		
						    			.build();

    context = JPAQueryContext.newInstance(entityManager, definition);
		
	CommandNodes commands = ns(n(Ancestor.class).withValue(Ancestor_.status, "active")
									with(n(Descendant.class).withValue(Descendant_.status, "cancelled"),
										 n(Descendant.class).withValue(Descendant_.status, "unknown")));
	PersistedResult result = context.create(commands);

    [What is created on Database]
    Root
    - ....
    	-...
		    Ancestor("active")
		        - .... >> Random value for things in between such as Parent.class
		           - ...
		              - Descendant(status="cancelled")
		              - Descendant(status="unknown")

    [How to retrieve it]
    ancestor = result.getForClassByCommandIndex(Ancestor.class,0)
    cancelledDescendant = result.getForClassByCommandIndex(Descendant.class, 0)
    unknownDescendant = result.getForClassByCommandIndex(Descendant.class, 1)

