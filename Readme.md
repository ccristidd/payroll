Summary
Throughout this tutorial, you have engaged in various tactics to build REST APIs. As it turns out, REST isn’t just about pretty URIs and returning JSON instead of XML.

Instead, the following tactics help make your services less likely to break existing clients you may or may not control:

Don’t remove old fields. Instead, support them.

Use rel-based links so clients don’t have to hard code URIs.

Retain old links as long as possible. Even if you have to change the URI, keep the rels so older clients have a path onto the newer features.

Use links, not payload data, to instruct clients when various state-driving operations are available.

It may appear to be a bit of effort to build up RepresentationModelAssembler implementations for each resource type and to use these components in all of your controllers. But this extra bit of server-side setup (made easy thanks to Spring HATEOAS) can ensure the clients you control (and more importantly, those you don’t) can upgrade with ease as you evolve your API.

This concludes our tutorial on how to build RESTful services using Spring. Each section of this tutorial is managed as a separate subproject in a single github repo:

nonrest — Simple Spring MVC app with no hypermedia

rest — Spring MVC + Spring HATEOAS app with HAL representations of each resource

evolution — REST app where a field is evolved but old data is retained for backward compatibility

links — REST app where conditional links are used to signal valid state changes to clients

To view more examples of using Spring HATEOAS see https://github.com/spring-projects/spring-hateoas-examples.