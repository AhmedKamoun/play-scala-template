package controllers.web.actors.spring_integration_config;


import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.IndirectActorProducer;
import controllers.web.actors.spring_integration_config.SimpleActor;
import org.springframework.context.ApplicationContext;
import play.api.libs.json.JsObject;

/**
 * An actor producer that lets Spring create the Actor instances.
 */
public class SpringActorProducer implements IndirectActorProducer {
    final ApplicationContext applicationContext;
    final String actorBeanName;
    final ActorRef out;
    final JsObject response;

    public SpringActorProducer(ApplicationContext applicationContext,
                               String actorBeanName, ActorRef out, JsObject response) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
        this.out = out;
        this.response = response;

    }

    @Override
    public Actor produce() {

        SimpleActor actor = (SimpleActor) applicationContext.getBean(actorBeanName);
        actor.setOut(out);
        actor.setResponse(response);
        return actor;
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
    }
}