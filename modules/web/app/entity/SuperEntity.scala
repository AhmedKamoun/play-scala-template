package entity

import javax.persistence.{Column, GeneratedValue, Id, MappedSuperclass}
import org.hibernate.annotations.GenericGenerator

/**
 * Created by root on 10/12/14.
 */
@MappedSuperclass
abstract class SuperEntity extends Serializable {
  /*
	 * the technical database and object key.
	 */

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "id", length = 40)
  var id: String = _

}
