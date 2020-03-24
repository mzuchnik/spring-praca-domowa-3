package pl.mzuchnik.springpracadomowa3.model;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.mzuchnik.springpracadomowa3.api.CarApi;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarWithManagerResourceAssembler implements SimpleRepresentationModelAssembler<Car>{

    @Override
    public void addLinks(EntityModel<Car> resource) {
        resource.add(linkTo(methodOn(CarApi.class).getCarById(resource.getContent().getId())).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Car>> resources) {
        resources.add(linkTo(methodOn(CarApi.class).getCars()).withSelfRel());
        resources.add(linkTo(methodOn(CarApi.class).getCarsByColor(new String())).withSelfRel());
    }

}
