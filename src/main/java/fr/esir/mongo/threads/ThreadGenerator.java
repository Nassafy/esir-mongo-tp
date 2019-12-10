package fr.esir.mongo.threads;

import fr.esir.mongo.text.TextGenerator;
import fr.esir.mongo.users.User;
import fr.esir.mongo.users.UserGenerator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 *
 * @author lboutros
 */
@Component
@AllArgsConstructor
@Slf4j
public class ThreadGenerator implements Processor {

  private final static Random RANDOM = new Random(System.currentTimeMillis());

  // TODO initialize/read values in mongo
  // This is a dummy example, you should NEVER do that for a production app
  private final AtomicInteger id = new AtomicInteger(0);
  private final ConcurrentHashMap<String, Thread> knownThreads = new ConcurrentHashMap<>();

  private final TextGenerator textGenerator;

  private final UserGenerator userGenerator;

  @Override
  public void process(Exchange exchange) throws Exception {
    exchange.getIn().setBody(generateThread());
  }

  // TODO manage post/thread/user relashionship
  private Thread generateThread() {
    User randomKnownUser = userGenerator.getRandomKnownUser();
    if (randomKnownUser != null) {
      String idString = Long.toString(id.getAndIncrement());
      Thread newThread = Thread.builder()
              ._id(idString)
              .title(textGenerator.generateText(1))
              .tags(getRandomTags())
              .build();

      knownThreads.put(idString, newThread);

      return newThread;
    } else {
      log.warn("Cannot create thread, no user created yet.");
      return null;
    }
  }

  public List<String> getRandomTags() {
    Random rand = new Random();
    List<String> tags = new LinkedList<String>(Arrays.asList("mea" ,"comic books", "rage", "memes", "troll", "fonction de hachage parfaite"));
    List<String> tagsOut = new ArrayList<>();
    int size = rand.nextInt(tags.size());
    for (int i = 0 ; i < size ; i++) {
      int index = rand.nextInt(tags.size());
      tagsOut.add(tags.get(index));
      tags.remove(index);
    }
    return tagsOut;
  }

  public Thread getRandomThread() {
    if (knownThreads.isEmpty()) {
      return null;
    } else {
      return knownThreads.get(Long.toString(RANDOM.nextInt(id.get())));
    }
  }
}
