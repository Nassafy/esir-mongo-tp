package fr.esir.mongo.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author lboutros
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
public class Post {
  @EqualsAndHashCode.Include
  private final String _id;
  
  private final String title;
  
  private final String content;

  private final String user_id;

  private final String thread_id;

  private final int user_age;
}
