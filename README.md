JPAQuery
========
Create a database hierarchy using simple command.

[![Build Status](https://secure.travis-ci.org/varokas/jpaQuery.png)](http://travis-ci.org/varokas/jpaQuery)

    [How we visioned it to get used]
    context = new Context()
    context.add( Deps.from(Child_.class).field(childId).to(Parent_).on(parentId))

    context.create(Child.class)

    result = context.create(Root.class).with(Root_.status,"Active").whichContains(Leaf.class).with(Leaf_.status,"Active").

    [What is created on Database]
    Root("active")
        - .... >> Random value for things in between
           - ...
              - Leaf(status="Active")

    [How to retrieve it]
    root = result.getEntity(Root.class)[0]
    leaf = result.getEntity(Leaf.class)[0]

