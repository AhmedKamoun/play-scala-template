package repositories.person

import entity.person.Man
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.stereotype.Repository

@Repository
trait ManRepository extends JpaRepository[Man, String] {
  //IF WE WANT TO CHECK BY SUB CLASS TYPE:  EXISTS (SELECT x FROM SUB_CLASS x WHERE x = a.sub_class_type)
  @Query("SELECT COUNT(m) FROM Man m  WHERE EXISTS (SELECT x FROM Artist x WHERE x = m.job) ")
  def findArtistMen(): Long


}
