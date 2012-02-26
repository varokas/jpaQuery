[![Build Status](https://secure.travis-ci.org/varokas/jpaQuery.png)](http://travis-ci.org/varokas/jpaQuery)

    context = new Context()
    context.add( Deps.from(Child_.class).field(childId).to(Parent_).on(parentId))

    context.create(Child.class)

    result = context.create(Root.class).with(Root_.status).eq("Active").whichContains(Leaf.class).with(Leaf_.status).

    Root("active")
        - ....
        - ...
           - Leaf(status="Active")

    root = result.getEntity(Root.class)[0]
    leaf = result.getEntity(Leaf.class)[0]

